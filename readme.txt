It is true to say that I have implemented a test task with some limitations for some reasons.

1) I was not able to successfully start the angular application on docker.When docker starts all services I'm able to browse docker running backend application on my PC with the host 198.16899.100:9999, but I'm not able to start front end mapped, as I expect 198.16899.100:8080 host. I was not able to resolve this issue myself. 
2) Angular frontend project does not have unit tests. Only angular cli generated stubs. I was not able to provide projects with angular tests.
3) Docker file for the backend is using Gradle compilation, but maven compilation is also available for regular compile. 4) Docker Postgres image is not initialized with the init.sql. I was not able to figure out how to force the docker compilation user.sql file for the database. As the solution, I have used spring database initialization which is enabled in the docker-compose file environment variable for spring application (spring.jpa.hibernate.ddl-auto=create).
To compile a project with docker-compose you need to have running docker on the computer and docker-compose installed.
from the current directory you need to start docker-compose with the command:
docker-compose up
It will start to compile and start 3 services. 
app: it is a backend spring boot application. (mapped to the post 9999)
web: it is a frontend angular application.
db: the postgres database instance.

To compile and run backend project locally better to use Idea ide. For the backend, you have to open SpringCurrencyExchangeServer folder as a project in idea ide and navigate to file ServerApplication (form package com.currency.server). Then use run option for the project main method start.
To compile and run the frontend application locally you need to have Node installed. Then from the CurrencyExchangeAngularFrontend location, you need to install dependencies using the command npm install and after starting the angular compilation with the command npx ng serve. As soon as project is compiled then it is available on local host with port 4200  