This is the backend of our application.

The directory recommendation_model contains the ML component of the app and the scripts that run them (except the Yolo model which need to be downloaded from Google Drive).

The file server.js contains modified code from https://www.journaldev.com/23709/android-image-uploading-with-retrofit-and-node-js and is the main module that runs the server, handles requests, and runs the python scripts.

The backend server has not been deployed on any cloud platform. The backend server needs to run locally for the app to work. A demo for doing this is included in the report video (https://www.youtube.com/watch?v=Kh8eK9YB4-c), starting from 12:56.
