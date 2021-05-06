# Copyright 2017 The TensorFlow Authors. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ==============================================================================

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
import pandas as pd
import os
print(os.getcwd())
import cv2

import argparse
import sys
import time

import numpy as np
#import tensorflow as tf
import tensorflow.compat.v1 as tf
tf.disable_v2_behavior()

import cv2
import numpy as np
import glob
import random

def get_title(path):
    net = cv2.dnn.readNet("./yolov3_training_last.weights", "./yolov3_testing.cfg")
    print(path)
    img = cv2.imread("./"+path)
    layer_names = net.getLayerNames()
    output_layers = [layer_names[i[0] - 1] for i in net.getUnconnectedOutLayers()]
    img = cv2.resize(img, None, fx=1, fy=1)
    height, width, channels = img.shape
    blob = cv2.dnn.blobFromImage(img, 0.00392, (416, 416), (0, 0, 0), True, crop=False)
    net.setInput(blob)
    outs = net.forward(output_layers)
    box = 0
    m = 0
    for out in outs:
        for detection in out:
            scores = detection[5:]
            class_id = np.argmax(scores)
            confidence = scores[class_id]
            if confidence > 0.3:
                # Object detected
                #print(class_id)
                center_x = int(detection[0] * width)
                center_y = int(detection[1] * height)
                w = int(detection[2] * width)
                h = int(detection[3] * height)

                # Rectangle coordinates
                x = int(center_x - w / 2)
                y = int(center_y - h / 2)

                if (width*height)>m:
                    m = width*height
                    box = [x,y,w,h]
    if box == 0:
        return [None],None,None            
    x,y,w,h = box
    portion = img[y:y+h, x:x+w]
    #portion = cv2.cvtColor(portion, cv2.COLOR_BGR2GRAY)
    #th3 = cv2.adaptiveThreshold(portion,255,cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
    #        cv2.THRESH_BINARY,11,2)
    #cv2.imshow("..",portion)
    #cv2.waitkey(0)
    height, width, channels = portion.shape
    #dim = max([height,width])
    #factor = 224/dim
    portion = cv2.resize(portion,None,fx = 224/width, fy = 224/height)
    return portion,height,width

def load_graph(model_file):
  graph = tf.Graph()
  graph_def = tf.GraphDef()

  with open(model_file, "rb") as f:
    graph_def.ParseFromString(f.read())
  with graph.as_default():
    tf.import_graph_def(graph_def)

  return graph

def read_tensor_from_image_file(file_name, input_height=299, input_width=299,
				input_mean=0, input_std=255):
  input_name = "file_reader"
  output_name = "normalized"
  file_reader = tf.read_file(file_name, input_name)
  if file_name.endswith(".png"):
    image_reader = tf.image.decode_png(file_reader, channels = 3,
                                       name='png_reader')
  elif file_name.endswith(".gif"):
    image_reader = tf.squeeze(tf.image.decode_gif(file_reader,
                                                  name='gif_reader'))
  elif file_name.endswith(".bmp"):
    image_reader = tf.image.decode_bmp(file_reader, name='bmp_reader')
  else:
    image_reader = tf.image.decode_jpeg(file_reader, channels = 3,
                                        name='jpeg_reader')
  float_caster = tf.cast(image_reader, tf.float32)
  dims_expander = tf.expand_dims(float_caster, 0);
  resized = tf.image.resize_bilinear(dims_expander, [input_height, input_width])
  normalized = tf.divide(tf.subtract(resized, [input_mean]), [input_std])
  sess = tf.Session()
  result = sess.run(normalized)

  return result

def load_labels(label_file):
  label = []
  proto_as_ascii_lines = tf.gfile.GFile(label_file).readlines()
  for l in proto_as_ascii_lines:
    label.append(l.rstrip())
  return label

def getGame(game_list):
  check = []
  for game in game_list:   
    file_name = game
    model_file = "tf_files/retrained_graph.pb"
    label_file = "tf_files/retrained_labels.txt"
    input_height = 224
    input_width = 224
    input_mean = 128
    input_std = 128
    input_layer = "input"
    output_layer = "final_result"


    graph = load_graph(model_file)
    p,_,_ = get_title(file_name)
    cv2.imwrite("tf_files/temp_xyz.jpeg",p)
    file_name = "tf_files/temp_xyz.jpeg"
    t = read_tensor_from_image_file(file_name,
                                  input_height=input_height,
                                  input_width=input_width,
                                  input_mean=input_mean,
                                  input_std=input_std)

    input_name = "import/" + input_layer
    output_name = "import/" + output_layer
    input_operation = graph.get_operation_by_name(input_name);
    output_operation = graph.get_operation_by_name(output_name);

    with tf.Session(graph=graph) as sess:
        start = time.time()
        results = sess.run(output_operation.outputs[0],
                      {input_operation.outputs[0]: t})
        end=time.time()
    results = np.squeeze(results)

    top_k = results.argsort()[-1:][::-1]
    labels = load_labels(label_file)

    print('\nEvaluation time (1-image): {:.3f}s\n'.format(end-start))
    template = "{} (score={:0.5f})"
    df = pd.read_csv("./games.csv")
    names = df['game']
    for i in top_k:
        ans = labels[i]
        st = ans.index(" ")
        en = ans.rindex(" ")
        ind = int(ans[st:en])
        print(template.format(names[ind], results[i]))
        check.append(ind)
  return check
    

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


#BACKEND (MAIN FUNCTION)

df = pd.read_csv('../dataset/user_ratings_aug.csv')
unique_list = df['gameid'].unique().tolist()

#print(len(unique_list))
f = open("./model_param.pickle","rb")
check = getGame(['./tf_files/sky.jfif','./tf_files/gta.jfif','./tf_files/b4.jfif'])
print(check)
model = CPU_Unpickler(f).load()
#check = [1,137,1402]
recs = get_mean_recs(check, model, unique_list, 5)
df2 = pd.read_csv('../dataset/games_info.csv')
names = df2['game']
print('CHECKING FOR...')
print(names[check])
print('RESULTS...')
print(recs)
for r in recs:
    print(names[r])