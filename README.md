# Introduction:
This is an Android application that recommends PS4 games given the cover images of a user's three most favorite games.

This project was initially built for CPEN 291 at UBC.

Refer to:

GameRecommender - for the frontend android application

backend - for the backend scripts and ML components of the application

dataset - for the datasets used for training and the code used to extract them

reports - for a presentation introducing the features and technical details of this app


# Instructions on how to use:
1. Currently the backend server is not deployed on any cloud platform. So the server must be run locally for the app to work. To do this, enter /backend directory and use the following commands:
```
$npm install
$node server.js
```
2. Open the /GameRecommender directory as an existing project in Android Studio

3. Check the IP address of the PC you use to run the server and change the SERVER_URL variable in file \GameRecommender\app\src\main\java\com\example\gamerecommender\MainActivity.kt to that IP address

4. Build and run the app on any android device you like. Tap the "camera" button to take a picture of your favorite game and then tap "upload image" to upload the image. Repeat this process for three different games and the recommendations will be generated in few seconds!
