# Generalfly App
Generalised application to host on [Fly.io](https://fly.io/)

# Dev Environment
## Postgres
Taking versions from https://hub.docker.com/_/postgres/

### Initial setup
Create a directory in the project called `dbData`.
The database Docker container uses it as a volume.

Connect into the database Docker container and create the user, database and the needed schema.
```postgresql
create user generalfly with createdb login password 'localsecret';
create database generalfly with owner generalfly;
\c generalfly generalfly
create schema comics;
```

### Commands
Run development db in Docker: `docker compose up`

Connect terminal: `docker exec -it general-db bash`

Run psql when in terminal: `psql -U postgres`

Build Docker image: `docker build -t generalfly:first .`

Run Docker image with localhost networking: `docker run --network host -e "SPRING_PROFILES_ACTIVE=dockerdev" generalfly:first`

Initial Fly.io command: `fly launch --no-deploy --local-only --image generalfly:latest`

Attach a Fly app to a Fly db: `fly postgres attach <postgres app name> --app <app name>`

Deploy to Fly.io after building locally: `fly deploy --ha=false --local-only --image generalfly:latest`

Tail app logs: `fly logs -a generalfly-jung`

# Live deploy
- Run local build script: `./build-image.sh`
- Deploy to fly.io: `fly deploy --ha=false --local-only --image generalfly:latest`

# Live maintenance
Connect to db: 'fly postgres connect - <db-name>'

