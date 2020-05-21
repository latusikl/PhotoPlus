<img src="res/logo.png" align="right" height="60">

Photo Plus Shop
======================

[![GIF demo](res/about.gif)](https://www.youtube.com/watch?v=lcaKMNPizFM)



## Content
---

- [About](#about)
- [Run it !](#run-it)
- [Technology stack](#technology-stack)
- [What can it do?](#what-can-it-do)


## About
---

This application was created as student's project in 2020 at Silesian University of Technology. The purpouse of this project was to learn as much as it is possible. The project contains of headless service which can handle managing of simple online shop and example implementation of UI for phtotography shop.

**Contributors:**
- [margawron](https://github.com/margawron)
- [latusikl](https://github.com/latusikl)
- [Suchar230](https://github.com/Suchar230)
- [syphzur](https://github.com/syphzur)
- [tomjur2107](https://github.com/tomjur2107)
  
## Run it
---

**You can check it out [online](http://photoplus.cf/).**

**Or folow these steps and run it locally:**
```diff
! To run it docker and docker-compose is reqiured.
```
- Install [docker](https://docs.docker.com/get-docker/) and [docker-compose](https://docs.docker.com/compose/install/).
- Clone repository.
```shell
git clone https://github.com/latusikl/PhotoPlus.git
```
- Execute command to run it.
```shell
cd PhotoPlus && docker-compose up --build
```

Docker Compose will build containers for you and expose on defined ports of *localhost*.

```
Backend: localhost:8090
Frontend: localhost:80
PHPAdmin: localhost:8000
```

## Technology stack
---
  - Backend:
    - Java 11
    - Spring Boot
      - Hibernate
      - Web MVC
      - HATEOAS
    - Loombook
    - MySQL
    - JWT
  - Frontend:
    - Angular
    - Typescript
  - Docker
  - Docker-Compose

## What can it do?
---
Both in fronend and in backend we used generic solutions for CRUD operations on database models (entities). User is authenticated via JWT which is genetated during login.

Application provides 4 types of users with different possibilities which are shortly described below.

