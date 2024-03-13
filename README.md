# Cinemania Website

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed Java 17.
- You have MySQL Server 8.0 or higher.
- You have Apache Maven 3.9.3 or higher.
- Your MySQL DBMS has two databases - one for production and another one for testing purposes.
- You have installed Node.js 20.11.1.

## Setting Up

1. Clone the project from GitHub.
2. Open the `backend` folder in any IDE (preferably Netbeans).
3. Clean and build the project so that all dependencies are present and the target folder appears.

## Configuring the Application

1. Change the following property values in `application.properties` and `application-test.properties` to reflect your database parameters:
    - `spring.datasource.url`
    - `spring.datasource.username`
    - `spring.datasource.password`<br />  
   Update these properties with the credentials of your production database in `application.properties`, and with your testing database in `application-test.properties`. Additionaly do the same in pom.xml file of backend folder under build->plugins->plugin->configuration so flyway could now where to migrate the database.

2. Position yourself in the `backend` folder with the command prompt, and type `mvn flyway:migrate` to initialize the structure of your production database.

## Running the Backend

You can start the backend of the website either by running it in the IDE with `--seed` as its argument, or by positioning yourself in the recently created target folder with the command prompt and typing `java -jar cinemania-website-backend-0.0.1-SNAPSHOT.jar --seed`. This starts your backend and runs a seeder before starting the app that creates a `cinemania_images` folder in your C drive, starts downloading movies and shows from "https://www.themoviedb.org/" and populates your database tables with their JSON data; the `cinemania_images` folder with image data and creates a user with administrator privilege that has username admin and password admin. Note, you only need to pass `--seed` the first time you run the backend. Every other time you either run it in the IDE without any arguments, or by typing `java -jar cinemania-website-backend-0.0.1-SNAPSHOT.jar` in the command prompt. This is so that your production database doesn't get overwritten.

## Configuring the Frontend

After configuring the backend, position yourself in the `frontend` folder with the command prompt and type `npm install`. This will install all necessary dependencies for the frontend.

## Running the Website

If you wish to see how it all looks, run the backend first either in the IDE without arguments or by the command prompt `java -jar cinemania-website-backend-0.0.1-SNAPSHOT.jar` while being in the target folder of `backend`, and after that type `npm run dev` in the command prompt while being in the `frontend` folder.

## Stopping the Application

If you wish to stop the backend from running, just type `Ctrl+C` in the same command prompt where the backend is running. Similarly for the frontend - if you wish to stop the frontend from running, type `q+Enter` in the same command prompt where the frontend is running.

## Available API routes

If you wish to see the documentation of all available backend API routes, you can do so by running backend, and typing `http://localhost:8080/api/docs` in your browser of choise