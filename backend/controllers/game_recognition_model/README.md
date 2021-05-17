This part of the project has been built using two popular models - Yolov3 and Google's MobileNet.

This repository has been built upon the original repository for Google's codelab series "Tensorflow for poets 2" (Link: https://github.com/googlecodelabs/tensorflow-for-poets-2 )

How to use?

This portion requires two main installations:

1. Yolov3 weights in ./game_recognition_model (Link: https://drive.google.com/file/d/1BjNKgL5NeZ_SMvksO0XBQLm0lvH74yTG/view?usp=sharing )

2. Tensorflow (preferably above 2.0, needs slight modifications with 1.8)

3. PyTorch, which our recommender model is built with

The folder scripts contains all code used for training.

The file integrated.py is the core of the Machine Learning components of this project. It has integrated all our models (YOLO model, classifier model and recommendation model) and computes all the results in one go after accepting inputs. This backend is running on the Google Cloud Platform. If the server does not respond, refer to the final video (starting at 12:56) for instructions on how to start a server locally.

