## User Management System

---

### Goals:

Create a web-service in Scala using FP as main paradigm.
It's oversimplified Restfull application that provide basic CRUD operation to manipulate User data with
HTTP endpoints down below:

- <b>POST /user/</b>        to create the User.
- <b>GET /user/id/</b>      to receive specific User by his ID.
- <b>PUT /user/id/</b>      to update specific User.
- <b>DELETE /user/id</b>    to delete specific User.
- <b>GET /user/</b>         to search all Users with specific parameters.


### Tech-stack

- Scala 2.13    - As main programming language.
- SBT           - Mainstream build tool for scala projects.
- Spray         - Framework that simplifies building RESTful web-services.
- Akka          - Mainstream library for creating high-performance applications. 
- Slick         - Most used ORM library in scala environment.
- H2            - Simple and lightweight Database.
- Lift-json     - Library for simplifying work with JSON.
- Logback       - Classic logging utility.

