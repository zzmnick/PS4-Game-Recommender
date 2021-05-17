This document gives direction to try this application.

1. Install and run Android Studio on your PC.
2. Open the directory GameRecommender in Android Studio.
3. Once Gradle Sync is done, run the app on a connected Android device.
4. The app will show buttons on screen if it successfully connects to the server and you can use the app as shown in the video.
5. If this isn't the case, it means that there are issues with the server running on Google Cloud. Follow steps ahead to fix this issue.
6. It might be the case that the server stops due to issues after the submission date. We will try our best to keep the server running for as long as possibe. Refer to the latest version of the README.md file in the home directory for the latest IP address of the VM.
7. Copy this IP over to the MainActivity.kt file in the app directory (for an example on how and where to do this, refer to the final video starting at 12:56). 
8. Now try running the app again.
9. If it still doesn't work, it might be because the server stopped unexpectedly and wasn't set up again, or because we ran out of credits on Google Cloud.
10. In such a case, try running the backend on localhost. To do this, download the yolo weights into the directory './backend/controllers/game_recognition_model' on your local computer (link in ./backend/controllers/game_recognition_model/README.md).
11. Then simply follow the steps in the final video starting at 12:56. 

Note: You might face an issue "Request failed" on uploading images. That is an unexpected error that sometimes occurs even when the server is running. In such a case, just switch to local.
 

