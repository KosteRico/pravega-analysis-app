# Pravega Data Analysis application

#### The app is used for tweets' sentiment analysis.

### Main components:

- **REST API**. Developed with Spring Boot. API is used for getting events from Pravega
- **Frontend**. Developed with Vue.js. Frontend represents working process. Contains a list of processed tweets and a
  pie chart with sentimental types ratio
- **Generator**. This is a simple Java app which puts tweets at Pravega. Tweets are contained in CSV file.

## How to use the app?

1. Run `mvn spring-boot:run`
2. Run `cd frontend && npm run serve`