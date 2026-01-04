# dungeons-n-dictionaries
A collection of digital D&amp;D tools for campaigning and character management.

# Running the software with Docker

This project is intended to be ran via Docker, sparing users the need to manage Java, Maven, Tomcat and Node.js/NPM installation / versioning.

To run the project via the Dockerfile provided, three steps are walked through:
1. [Database](#1-database)
2. [.env](#2-environment-variables)
3. [Docker](#3-docker)

## 1. Database Setup

This project operates with a PostgreSQL database - you can find the PostgreSQL download link [here](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads). Both the PostgreSQL Server and pgAdmin software are needed.

Within pgAdmin:
1. Create a server if one does not exist yet - name is not important here.
2. Create a user by right-clicking your chosen server and navigating `Create > Login/Group Role...` (*paths may different depending on pgAdmin version*). Assign this user a password under the `Definition` tab, and tick the "Can login?" privilege under the `Privileges` tab. Make note of this Name and Password for later.
3. Create the database now by right-clicking your chosen server and navigating `Create > Database...`. Set the user you just created as the database Owner. Make note of this Database name for later.

You do not need to initialise the database with any schema - the backend will do this automatically upon the first time a request is made.

## 2. Environment Variables

Copy / rename the root `.env.example` file to `.env` - this will be the singular source of all the additional environment variables the Docker container needs to run the code. Adjust the following three variables to match your database setup:
- `JDBC_URI=jdbc:postgresql://host.docker.internal:5432/<database>`
- `JDBC_USERNAME=<name>`
- `JDBC_PASSWORD=<password>`

You may try setting the `POPULATE_DATABASE` env flag to `true` to insert example data into the database at runtime. Alternatively, you may set `RESET_DATABASE` to `true` for a clean database state everytime the code is ran. Other environment variables should not need to be adjusted for the sake of running this docker setup.

## 3. Docker

To generate the Docker container and run it, the below commands should be ran from the root repository. Make sure the Docker daemon is running first. You can find the installation steps for Docker Desktop [here](https://docs.docker.com/desktop/).

> [!NOTE]
> Frontend environment variables found in the `.env` file created earlier are hardbaked into the Vite app, and thus the following build step will not succeed until said file has been generated.

*Building the container:*
```bash
docker build -t <container-name> .
```

*Running the container:*
```bash
docker run --env-file .env -p 8090:8080 <container-name>
```

Congrats! You can now access the code in your browser via http://localhost:8090.
