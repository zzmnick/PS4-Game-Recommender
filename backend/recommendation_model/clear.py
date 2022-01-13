import os
# script run by server to set up the environment for the application to work
root = os.getcwd()

folder = os.listdir(root+"/uploads/")

for fn in folder:
    os.remove(root+"/uploads/"+fn)

print("DONE")
