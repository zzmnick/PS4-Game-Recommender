import torch, torchtext, numpy as np
import pandas as pd, csv
from torch import nn, optim
from tqdm.auto import tqdm
import random
import pickle
import io

torch.manual_seed(0)
np.random.seed(0)
torch.set_deterministic(True)


class GameDataset(torch.utils.data.Dataset):
  def __init__(self, fn):
    df = pd.read_csv(fn)
    u2n = { u: n for n, u in enumerate(df['username'].unique()) }
    g2n = { g: n for n, g in enumerate(df['gameid'].unique()) }
    df['username'] = df['username'].apply(lambda u: u2n[u])
    df['gameid'] = df['gameid'].apply(lambda g: g2n[g])
    self.coords = torch.LongTensor(df[['username','gameid']].values)
    self.ratings = torch.FloatTensor(df['rating'].values)
    self.n_users = df['username'].nunique()
    self.n_games = df['gameid'].nunique()

  def __len__(self):
      return len(self.coords)

  def __getitem__(self, i):
      return (self.coords[i], self.ratings[i])

class GameRecs(nn.Module):
  def __init__(self, n_users, n_games, emb_dim):
    super(GameRecs, self).__init__()
    self.user_emb = nn.Embedding(n_users, emb_dim)
    self.game_emb = nn.Embedding(n_games, emb_dim)
    nn.init.xavier_uniform_(self.user_emb.weight)
    nn.init.xavier_uniform_(self.game_emb.weight)
  
  def forward(self, samples):
    users = self.user_emb(samples[:,0])
    games = self.game_emb(samples[:,1])
    return (users * games).sum(1)

class GameRecsBias(nn.Module):
  def __init__(self, n_users, n_games, emb_dim):
    super(GameRecsBias, self).__init__()
    self.user_emb = nn.Embedding(n_users, emb_dim)
    self.user_bias = nn.Embedding(n_users, 1)
    self.game_emb = nn.Embedding(n_games, emb_dim)
    self.game_bias = nn.Embedding(n_games, 1)
    nn.init.xavier_uniform_(self.user_emb.weight)
    nn.init.xavier_uniform_(self.game_emb.weight)
    nn.init.zeros_(self.user_bias.weight)
    nn.init.zeros_(self.game_bias.weight)
    
  def forward(self, samples):
    users = self.user_emb(samples[:,0])
    games = self.game_emb(samples[:,1])
    dot = (users * games).sum(1)
    user_b = self.user_bias(samples[:,0]).squeeze()
    game_b = self.game_bias(samples[:,1]).squeeze()
    return dot + user_b + game_b


device = torch.device('cpu')

def run_test(model, ldr, crit):
  total_loss, total_count = 0, 0
  model.eval()
  tq_iters = tqdm(ldr, leave=False, desc='test iter')
  with torch.no_grad():
    for coords, labels in tq_iters:
      coords, labels = coords.to(device), labels.to(device)
      preds = model(coords)
      loss = crit(preds, labels)
      total_loss += loss.item() * labels.size(0)
      total_count += labels.size(0)
      tq_iters.set_postfix({'loss': total_loss/total_count}, refresh=True)
  return total_loss / total_count

def run_train(model, ldr, crit, opt, sched):
  model.train()
  total_loss, total_count = 0, 0
  tq_iters = tqdm(ldr, leave=False, desc='train iter')
  for (coords, labels) in tq_iters:
    opt.zero_grad()
    coords, labels = coords.to(device), labels.to(device)
    preds = model(coords)
    loss = crit(preds, labels)
    loss.backward()
    opt.step()
    sched.step()
    total_loss += loss.item() * labels.size(0)
    total_count += labels.size(0)
    tq_iters.set_postfix({'loss': total_loss/total_count}, refresh=True)
  return total_loss / total_count

def run_all(model, ldr_train, ldr_test, crit, opt, sched, n_epochs=10):
  best_loss = np.inf
  tq_epochs = tqdm(range(n_epochs), desc='epochs', unit='ep')
  for epoch in tq_epochs:
    train_loss = run_train(model, ldr_train, crit, opt, sched)
    test_loss = run_test(model, ldr_test, crit)
    tqdm.write(f'epoch {epoch}   train loss {train_loss:.6f}    test loss {test_loss:.6f}')
    if test_loss < best_loss:
      best_loss = test_loss
      tq_epochs.set_postfix({'bE': epoch, 'bL': best_loss}, refresh=True)


class GameDatasetTest(torch.utils.data.Dataset):
  def __init__(self, df):
    self.coords = torch.LongTensor(df[['username','gameid']].values)
    self.ratings = torch.FloatTensor(df['rating'].values)
    self.n_users = df['username'].nunique()
    self.n_games = df['gameid'].nunique()

  def __len__(self):
      return len(self.coords)

  def __getitem__(self, i):
      return (self.coords[i], self.ratings[i])

def get_recs_game(gameid, model, games_list, num_recs):
  if gameid in [477,526,620,626,842,1000,1127,1130,1181,1194,1224,1403]:
    gameid = gameid - 1
  if gameid == 1404:
    gameid = 1402
  index = games_list.index(gameid)
  user_pref = model.game_emb.weight[index]
  cos = nn.CosineSimilarity(dim = 0)
  sims = []
  for i in range(len(games_list)):
    if i != index:
      game_param = model.game_emb.weight[i]
      similarity = cos(user_pref, game_param)
      sims.append([i, similarity])
  return sims

def get_mean_recs(games,model,games_list,num_recs):
  one = get_recs_game(games[0], model, games_list, num_recs)
  two = get_recs_game(games[1], model, games_list, num_recs)
  three = get_recs_game(games[2], model, games_list, num_recs)
  newList = []
  for i in range(len(one)):
    newList.append([one[i][0],(one[i][1].item() + two[i][1].item() + three[i][1].item())/3])
    
  def sortFunc(p):
    return p[1] 
  newList.sort(reverse=True, key=sortFunc) 
  #print(sims[0][1].item())
  gameids = []
  for i in range(num_recs):
    gameid = games_list[newList[i][0]]
    gameids.append(gameid)
  return gameids

class CPU_Unpickler(pickle.Unpickler):
    def find_class(self, module, name):
        if module == 'torch.storage' and name == '_load_from_bytes':
            return lambda b: torch.load(io.BytesIO(b), map_location='cpu')
        else: return super().find_class(module, name)



df = pd.read_csv('../dataset/user_ratings_aug.csv')
unique_list = df['gameid'].unique().tolist()

#print(len(unique_list))
f = open("./model_param.pickle","rb")

model = CPU_Unpickler(f).load()
check = [1,137,1402]
recs = get_mean_recs(check, model, unique_list, 5)
df2 = pd.read_csv('../dataset/games_info.csv')
names = df2['game']
print('CHECKING FOR...')
print(names[check])
print('RESULTS...')
print(recs)
for r in recs:
    print(names[r])