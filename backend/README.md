This is the backend of our application.

The directory controllers/game_recognition_model contains the ML component of the app and the scripts that run them (except the Yolo model which need to downloaded from Google Drive).

The file app.js is an intermediate test file used to explore Node.js as a viable option

The file multipart.js contains modified code from https://www.journaldev.com/23709/android-image-uploading-with-retrofit-and-node-js and is the main module that runs the server, handles requests,
and runs the python scripts.

The folder node_modules already include the dependencies required by Node.js.
