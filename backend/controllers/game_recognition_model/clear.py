import os
root = os.getcwd()

folder = os.listdir(root+"/uploads/")

for fn in folder:
    os.remove(root+"/uploads/"+fn)

print("DONE")
