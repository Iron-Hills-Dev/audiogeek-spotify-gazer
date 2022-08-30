# AudioGeek Spotify Gazer: The Great Spy Of Spotify
## How to use dockerized Spotify Gazer

### Prerequisites
* Docker and docker-compose

### First setup
To set up Spotify Gazer first time, you just need to:

* Go to .docker folder inside repo
```
cd [path to repo]/.docker
```

* Now you just need to perform one command to start program
```
docker-compose up --build
```

**Here you are!** You completed first setup of dockerized Spotify Gazer!

### Start docker after first setup
If you want to start program again after first setup, 
and you changed nothing in code or properties of Spotify Gazer, you can start program like this:
```
cd [path to repo]/.docker
docker-compose up
```

### Docker ports (how to gain access to dockerized Spotify Gazer)
* `localhost:6080/api/v1` - API
* `localhost:6080/doc` - Docs in ***Swagger*** (OpenAPI 3.0)
* `localhost:6050` - PGADMIN to get access to application database


### Database credentials
* **PGADMIN EMAIL:** codebusters@ironhills.dev
* **PGADMIN PASSWD:** postgres
* **POSTGRES HOST:** gazer-postgres:5432 (only accessible inside docker network or through PGADMIN)
* **POSTGRES USERNAME:** gazer
* **POSTGRES PASSWD:** gazer