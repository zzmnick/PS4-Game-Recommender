This part of the project has been built using two popular models - Yolov3 and Google's MobileNet.

This repository has been built upon the original repository for Google's codelab series "Tensorflow for poets 2" (Link: https://github.com/googlecodelabs/tensorflow-for-poets-2 )

How to use?


This portion requires two main installations:

1. Yolov3 weights in ./game_recognition_model (Link: https://drive.google.com/file/d/1BjNKgL5NeZ_SMvksO0XBQLm0lvH74yTG/view?usp=sharing )

2. Tensorflow (preferably above 2.0, needs slight modifications with 1.8)

You might also need to install other libraries as required.

Once done, put a game image in ./game_recognition_model/tf_files. Then while having ./game_recognition_model as the current working directory, enter the following command:-


python -m scripts.label_image     --graph=tf_files/retrained_graph.pb      --image=tf_files/filename


One image has been provided as test.

