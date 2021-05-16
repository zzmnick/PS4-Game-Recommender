This is the backend of our application.

The directory controllers/game_recognition_model contains the ML component of the app and the scripts that run them (except the Yolo model which need to downloaded from Google Drive).

The file app.js is an intermediate test file used to explore Node.js as a viable option

The file multipart.js contains modified code from https://www.journaldev.com/23709/android-image-uploading-with-retrofit-and-node-js and is the main module that runs the server, handles requests,
and runs the python scripts.

The folder node_modules already include the dependencies required by Node.js.

The backend server has been deployed on google cloud platform. However if the app failed to connect to the server for some reason, the backend server can also be started locally in order to run the app. A demo for doing this is included in the final video (https://www.youtube.com/watch?v=Kh8eK9YB4-c), starting from 12:56.
