# Generalfly App
Generalised application to host on [Fly.io](https://fly.io/)

# Dev Environment
## Postgres
Taking versions from https://hub.docker.com/_/postgres/

### Initial setup
Create a directory in the project called `dbData`.
The database Docker container uses it as a volume.

### Commands
Run development db in Docker: `docker compose up`

Connect terminal: `docker exec -it general-db bash`

Run psql when in terminal: `psql -U postgres`
