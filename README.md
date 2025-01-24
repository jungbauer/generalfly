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
