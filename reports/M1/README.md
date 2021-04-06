Milestone M1 report

Current State of the Project:
Our team is building a game recommender Android Application that takes in user input images of up to 3 game title covers and recommends games according to the relative information of the games. It uses two ML models: one for image recognition to decipher the game title and a recommender model to recommend similar games. 

Our team has gone through the proposal together and have a fair understanding of what the final product should look like. Currently, we have collated a dataset which includes ‘PS4 Games’ from Kaggle along with additional information that we deem useful in the future. They include: genres, developers, publishers, release date and size of the games. These information were entered as additional columns into the 'games_info.csv' file. They were first collected by parsing the web pages relative to all the games, and then double-checked manually to ensure accuracy.

Furthermore, we have begun training the initial image recognition model that will be used to recognize the game title from the user input images and then cross-reference it with the dataset before it is processed by the second recommender model (that will be built over the coming weeks).

For the image recognition segment of the project, we decided to use YOLO which is an object-detection model in order to decipher the game title fragment of the game covers. The images dataset containing the title fragment of all the game covers is included in the 'titles.zip' file. We then used image augmentation techniques on each game title (such as rotation, etc.) to produce around 200 images (per game). Next, we pass these images of game titles into Google’s Inception model in order to train the model for classifying the titles into the corresponding games which we can cross-reference with our dataset.

The accuracy of the title detector model is approximately 85% while the recognition model is still being tested with different augmentation techniques.

Feature Changes to the Proposal:
-    (In the Risks/bakcup part) As a fallback plan, if the image classifier is not able to classify the image of game titles accurately, then we will switch to working on a model that performs Image Text to Text conversion which is more likely to give us accurate results if we can get it working. Alternatively, we could pick out a subset of the games which have a good chance of being someone's favourite PS4 game and train the classifier on those games only.

Current Challenges/Bottlenecks:
-    Firstly, even though the model extracts the fragments of game covers just including the title considerably well, it does not reach the levels of expected accuracy when it comes to classifying these images into the actual game titles that we can then pass into our future recommender model. We are thinking of using one of the backup plan mentioned above to solve this problem.
-    The next challenge is building the backend of the Android Application. Although this is a challenge that will be addressed in the later phase of the project, we as a team are well aware of how demanding it is as a task. More specifically, the team needs to find a way to set up a remote server on which the model will be stored. We will then establish a network connection and send requests and receive responses.


Team Member Contributions:

-    Aayush: Collating the images dataset and training the recognition model using YOLO and Inception to check for initial accuracy.
-    Harshil: Double Checking the insertion of additional information for accuracy and Milestone Report.
-    Kolton: Double Checking the insertion of additional information for accuracy.
-    Nick: Automating the process of collecting and inserting the additional information into the Dataset in the 'games_info.csv' file.
