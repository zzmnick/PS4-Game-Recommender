Milestone M2 report

Current State of the Project:
During this milestone, our team has made significant progress in the ML component of the project. We built upon the previously collated dataset of game metadata to get the image recognition model and game classifier model. We had begun working on this model towards the end of the previous Milestone and so it’s technicalities are described in the Milestone 1 report. The model performs quite well at recognizing user input pictures of the game covers. Our model achieves a test accuracy of around 90% and can recognize around 400 popular game titles with ease.

In addition to this, our team has also worked on the game recommender model. We realized that in addition to the game metadata dataset (games_info.csv) used by the image recognition/classification model, we required another dataset (user_ratings.csv) that contained user related data such as ratings per game for our recommender model. Hence, we worked together to retrieve scrape Metacritic pages for user info (such as their usernames and their game ratings) and manually entered the information for titles that could not be scraped automatically. Furthermore, we have begun the training process for our recommender model and since this is the last part of the project’s ML component, we hope to have it working with full accuracy before the end of Milestone 3 (along with our app frontend).

Finally, just as was planned in the proposal, we have begun research on app development with Android Studio. One of your teammates has already begun on the skeleton framework for the app’s UI which we will submit (a completed version) as the deliverable for Milestone 3.

Feature Changes to the Proposal:
N/A. We did not hit any dead ends/roadblocks that would require us to change any features from the proposal during this Milestone.

Current Challenges/Bottlenecks:
-    With regards to our image recognition model, it works extremely well for images taken from Google images. However, when we input images that we have taken using our phones, the lighting/glare causes some issues. We believe that with further augmentation and training, this issue can be overcome.
-    With regards to our game recommender model, we made a new dataset for user info. However, we have around 97000 entries with around 6000 unique users which led to some of the information being a bit sparse. This may/may not affect the training process but it would be a significant issue to overcome before Milestone 3 in order to achieve an acceptable accuracy for the model.

Team Member Responsibilities:
Aayush: Trained and tested the image recognition and classification model which is now working with an acceptable accuracy.
Harshil: Manual entry of Metacritic pages for the dataset, worked on the recommender model and wrote the M2 report.
Kolton: Manual entry of Metacritic pages for the dataset and began working on the skeleton UI for the app.
Nick: Web Scraping and retrieving Metacritic pages, collated the dataset of user info into a csv file and worked on the recommender model.
