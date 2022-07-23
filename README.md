# Feedbacktool
This is a questionare tool build for the website of the TU Vienna. It contains a application where questions
for a questionaire can be defined in a json file. Because the questionaire is anonymous there is build in protection
that one user can only submit there filled out forms after a timeout period. This is done by IP-Address and cookies.

The Docker containers can be built with `docker-compose build` and then executed with `docker-compose up`. 

The Docker container is then accessible at the following URL: http://localhost:8888/feedbacktool/

After starting, questions should first be read from the JSON file. To do this, go to the evaluation page and
click on the button `Load Questions from JSON`. After that the questionnaire can already be filled out.
