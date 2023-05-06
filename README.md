<h1 align="center">News Management System RESTful Web Service</h1>

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

---

### :zap: **Description**

This is a RESTful web service built with Spring Boot, Java, Gradle and PostgreSQL,
which provides functionality for managing news articles. The service includes support
for creating, updating and deleting news articles and comments.

---

### :white_check_mark: **The stack of technologies used**
:star: **API Technologies:**
- SOLID
- OOP
- DI
- REST
- AOP

:desktop_computer: **Backend technologies:**
- Java 17
- Spring Boot 3, Spring Security, Spring Cloud
- JWT
- Redis & LRU/LFU cache implementation

:hammer_and_wrench: **Build Tool:**
- Gradle 7.4

:floppy_disk: **DataBase:**
- PostgreSQL
- LiquiBase Migration

:heavy_check_mark: **Testing:**
- Junit 5
- Mockito
- TestContainers
- WireMock

:whale: **Containerization:**
- Docker

:globe_with_meridians: **Microservices**

The application follows a microservices architecture, with the following components:
1. ***Spring Cloud Config*** - stores configuration settings of microservices
2. ***Eureka Server*** - microservices discovery service
3. ***API Gateway*** - single point of entry for requests
4. ***News Service*** - provides functionality for managing news
5. ***Auth Service*** - provides functionality for authorization users

:pouting_man: **Profiles**

The application uses three different profiles:
1. ***prod*** - for production deployment
2. ***dev*** - for local development
3. ***test*** - for automated testing

---

### :rocket: **Get Started**

**Build with Gradle**

Build all sub-modules from parent module:

    $ ./gradlew clean build

If in case face any issue while building modules because of test cases, then try build with disabling test cases:

    $ ./gradlew clean build -x test

Run Application using Docker Compose:

    $ docker-compose up

Browse OpenAPI Documentation:  
`localhost:8765/swagger-ui.html`  
`localhost:8765/v3/api-docs/auth`  
`localhost:8765/v3/api-docs/news`


---

### :pushpin: **API Endpoints**

**Authentication**

| **HTTP METHOD** |         **ENDPOINT**         | **DESCRIPTION** |
|:---------------:|:----------------------------:|:---------------:|
|    **POST**     | `/auth/api/v0/auth/register` |  Register User  |
|    **POST**     |  `/auth/api/v0/auth/login`   | Authorize User  |

**Users**

| **HTTP METHOD** |                **ENDPOINT**                |      **DESCRIPTION**      |
|:---------------:|:------------------------------------------:|:-------------------------:|
|     **GET**     |            `/auth/api/v0/users`            |      Find all Users       |
|     **GET**     |         `/auth/api/v0/users/{id}`          |      Find User by ID      |
|     **GET**     | `/auth/api/v0/users/byUsername/{username}` |   Find User by username   |
|     **GET**     |  `/auth/api/v0/users/byToken/{username}`   |     Find User by JWT      |
|     **PUT**     |         `/auth/api/v0/users/{id}`          |     Update User by ID     |
|    **PATCH**    |         `/auth/api/v0/users/{id}`          | Partial Update User by ID |
|    **PATCH**    |      `/auth/api/v0/users/block/{id}`       |     Block User by ID      |
|    **PATCH**    |     `/auth/api/v0/users/unblock/{id}`      |    Unblock User by ID     |
|   **DELETE**    |         `/auth/api/v0/users/{id}`          |     Delete User by ID     |

**News**

| **HTTP METHOD** |         **ENDPOINT**         |      **DESCRIPTION**      |
|:---------------:|:----------------------------:|:-------------------------:|
|    **POST**     |     `/news/api/v0/news`      |       Save new News       |
|     **GET**     |     `/news/api/v0/news`      |       Find all News       |
|     **GET**     | `/news/api/v0/news/criteria` | Find all News by Criteria |
|     **GET**     |   `/news/api/v0/news/{id}`   |      Find News by ID      |
|     **PUT**     |   `/news/api/v0/news/{id}`   |     Update News by ID     |
|    **PATCH**    |   `/news/api/v0/news/{id}`   | Partial Update News by ID |
|   **DELETE**    |   `/news/api/v0/news/{id}`   |     Delete News by ID     |

**Comments**

| **HTTP METHOD** |           **ENDPOINT**           |        **DESCRIPTION**        |
|:---------------:|:--------------------------------:|:-----------------------------:|
|    **POST**     | `/news/api/v0/comments/{newsId}` |       Save new Comment        |
|     **GET**     |     `/news/api/v0/comments`      |       Find all Comments       |
|     **GET**     | `/news/api/v0/comments/criteria` | Find all Comments by Criteria |
|     **GET**     |   `/news/api/v0/comments/{id}`   |      Find Comment by ID       |
|     **PUT**     |   `/news/api/v0/comments/{id}`   |     Update Comment by ID      |
|    **PATCH**    |   `/news/api/v0/comments/{id}`   | Partial Update Comment by ID  |
|   **DELETE**    |   `/news/api/v0/comments/{id}`   |     Delete Comment by ID      |
