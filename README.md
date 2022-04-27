# School registration system

### To run:

Go to the main directory and execute: `./mvnw clean package`. This will create jar file in the `target` directory. Then execute `sudo ./mvnw spring-boot:build-image`. This will create the image. Finally, go to the `docker` directory  and run `docker-compose up`. This will create `db` and `app` container. `db` uses newest postgres image pulled from the docker repository if not found locally. Then initializes the database and run the app that depends on it

### Documentation:

You can find the documentation of the endpoints and payloads at `http://localhost:8080/swagger-ui.html` while the app is running. 