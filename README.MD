## User Management System

---

### Goals:

Create a web-service in Scala using FP as main paradigm.
It's oversimplified Restfull application that provide basic CRUD operation to manipulate some data with
HTTP endpoints down below:

H2 database:
- <b>POST /user/</b>        to create the User.
- <b>GET /user/id/</b>      to receive the specific User by his ID.
- <b>PUT /user/id/</b>      to update the specific User.
- <b>DELETE /user/id</b>    to delete the specific User.
- <b>GET /user/</b>         to search all Users with specific parameters.

MySQL database:
- <b>POST /book/</b>        to create a Book.
- <b>GET /book/id</b>       to receive the specific Book by its ID.
- <b>PUT /book/id</b>       to update the specific Book.
- <b>DELETE /book/id</b>    to delete the specific Book.
- <b>GET /book/</b>         to search all Books with input parameters.


````
$ curl -H "Content-Type: application/json" -X POST -d '{"id": 1, "firstName": "Todd", "lastName": "Howard", "birthday": "25-04-1971", "isAvailable": true}' http://0.0.0.0:8080/user
````

### Tech-stack

- Scala 2.13    - As main programming language.
- SBT           - Mainstream build tool for scala projects.
- Akka          - Mainstream library for creating high-performance applications. 
- Slick         - Most used ORM library in scala environment.
- H2            - Simple and lightweight Database.
- Spray-json    - Library for simplifying work with JSON.
- Logback       - Classic logging utility. 

