# AudioGeek Spotify Gazer: The Great Spy Of Spotify
## How to use dockerized Spotify Gazer (DEV)

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

## Environment variables and their purpose
### Obligatory variables (You have no choice here)
* `GAZER_SPOTIFY_CLIENT_ID` Client ID of your Spotify app.
* `GAZER_SPOTIFY_CLIENT_SECRET` Client SECRET of your Spotify app.

* `GAZER_DATABASE_DATASOURCE_URL` Database URL (ex. jdbc:postgresql://localhost:5432/dbName).
* `GAZER_DATABASE_USER` Database username.
* `GAZER_DATABASE_PASSWORD` Database password.\
<small>**Pro tip:** Client ID and SECRET are accessible through\
Spotify Dashboard (https://developer.spotify.com/dashboard)</small>

### Suggested variables (I would but no force)
* `GAZER_PORT` (*6000*) Port on which Gazer is accessible. 
* `GAZER_FLOW_SCHEDULER_CRON` (_0 0 3 \* \* \*_) Cron sentence describing how often Gazer will update his new releases database.
* `GAZER_RELEASES_CHAR_WHITELIST` (*empty array*) Gazer will only accept this symbols in genres given through rest.\
<small>**Pro tip:** No human ever understood Cron so on this 
website you can easily create one: https://crontab.guru</small>

### Whatever variables (Whatever bro)
* `GAZER_LOG_LEVEL` (*info*) Logs this level and higher will show themselves on console.
* `GAZER_DATABASE_POOL_SIZE` (*5*) How many database processes will be started.
* `GAZER_DATABASE_CONNECTION_TIMEOUT` (*5000*) How long (ms) process will wait for connection to database.
* `GAZER_RELEASES_CHAR_BLACKLIST` (*empty array*) **!Alternative to whitelist!** Symbols on this list will not be accepted 
in genres given through rest.
* `GAZER_RELEASES_MAX_GENRES_AMOUNT` (*30*) How many genres you can give in rest request.
* `GAZER_RELEASES_MAX_GENRE_SIZE` (*50*) How long can be one genre in rest request.
* `GAZER_RELEASES_MAX_PAGE_SIZE` (*20*) How big page rest can give in return.
* `GAZER_FLOW_ON_STARTUP` (*false*) Some developer thing. Set to true if you want to update new releases database every
time after application startup.