# Feedbacktool
This is a questionare tool build for the website of the TU Vienna. It contains a application where questions
for a questionaire can be defined in a json file. Because the questionaire is anonymous there is build in protection
that one user can only submit there filled out forms after a timeout period. This is done by IP-Address and cookies.

The Docker containers can be built with `docker-compose build` and then executed with `docker-compose up`. 

The Docker container is then accessible at the following URL: http://localhost:8888/feedbacktool/

After starting, questions should first be read from the JSON file. To do this, go to the evaluation page and
click on the button `Load Questions from JSON`. After that the questionnaire can already be filled out.

![image](https://user-images.githubusercontent.com/14179713/180604967-8d47a5d3-584b-49b0-a23d-0eb3ec3cea68.png)

![image](https://user-images.githubusercontent.com/14179713/180604996-fb3a3a78-e5fe-4d94-9e24-45bfa19f48b9.png)

![image](https://user-images.githubusercontent.com/14179713/180605006-7a17a419-50d3-4550-988d-d0c089827134.png)
![image](https://user-images.githubusercontent.com/14179713/180605031-bec4a36d-2de7-4c6f-9b91-cf54906bb9cb.png)
