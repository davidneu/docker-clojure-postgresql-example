# Setting Up a Docker Development and Production Environment for Clojure and Postgresql


## Overview
This project shows how to use docker and docker compose to develop and
deploy Clojure and Postgresql based applications.  It uses Emacs with
inf-clojure, Leiningen, and Clojure's socket repl.


## Setup
	Clone this repository, cd into its directory, and follow the
    remaining steps.  You may wish to copy this README.md file to a
    another directory since it will be overwritten in the On Rails
    section.


## Install Docker 
	1. See https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu

	2. $ sudo apt-get install linux-image-extra-$(uname -r) linux-image-extra-virtual

	3. $ sudo apt-get install apt-transport-https  ca-certificates  curl  software-properties-common

	4. $ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

	5. $ sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu `lsb_release -cs` stable"

	6. $ sudo apt-get update

	7. This will install the lastest stable version.  In production we'll want to install a specific version.
	   - $ sudo apt-get install docker-ce

	8. Confirm that docker is working.
	   - $ sudo docker run hello-world


## Linux Post-installation
	1. See https://docs.docker.com/engine/installation/linux/linux-postinstall/

	2. sudo groupadd docker

	3. sudo usermod -aG docker $USER

	4. Log out, log back in, and confirm that you can run docker
	   commands without sudo
	   - $ docker run hello-world


## Install Docker Compose
	1. See https://github.com/docker/compose/releases
	
	2. $ cd /tmp
	
	3. $ curl -L https://github.com/docker/compose/releases/download/$dockerComposeVersion/docker-compose-`uname -s`-`uname -m` > docker-compose 
		 where $dockerComposeVersion is the latest version as shown on https://github.com/docker/compose/releases.

	4. $ chmod 755 docker-compose

	5. $ sudo mv docker-compose /usr/local/bin

	6. $ docker-compose --version


##  Development Database Setup
	1. $ docker-compose build --force-rm clj-dev
	2. $ docker-compose up --remove-orphans clj-dev
	3. $ docker exec -it --detach-keys="ctrl-@" myapp_db_dev_container "/bin/bash"
	4. $ psql -U postgres
	5. postgres=# alter user postgres with password 'postgrespw';
	6. Using an editor on the host, add the following lines to the
       file /data/myapp_db_dev_volume/pg_hba.conf on the host
	   local    all      postgres                md5
	   local	all		 myapp		 			 md5
	   host 	all		 myapp		samenet		 md5	 
	   The lines should be below the line "Put your actual
       configuration here".  Be sure to comment out all other lines.
	7. select pg_reload_conf();
	   - Or, on the host run
	   $ docker exec --user postgres --workdir /usr/lib/postgresql/10/bin myapp_db_dev_container pg_ctl reload
	8. Exit psql
	9. $ createuser myapp -d -s -P -U postgres
	10. $ createdb -U myapp myapp
	11. Exit the db-dev container.
	12. $ docker exec -it --detach-keys="ctrl-@" myapp_clj_dev_container "/bin/bash"	
	13. $ psql -U myapp -d myapp -h db-dev
	14. To avoid being prompted for a password, create the file
		resources/.pgpass, and add the following line 
		  db-dev:5432:myapp:myapp:myapppw
	   Now you can type
		 $ export PGPASSFILE=/myapp/resources/.pgpass; psql -U myapp -d myapp -h db-dev
	15. myapp=# \i /myapp/resources/createdb.sql


##  Development Workflow
	1. $ docker-compose build --force-rm clj-dev
	2. $ docker-compose up --remove-orphans clj-dev 
	3. $ docker exec -it --detach-keys="ctrl-@" myapp_clj_dev_container "/bin/bash"
	4. $ lein trampoline run -m clojure.main
	   - Dockerfile-dev puts a convenience script with this command in in /usr/local/bin/repl.
	5. To connect to the repl from emacs 
	   a. M-x inf-clojure
	   b. 0.0.0.0
	   c. 5555
	6. To load the application
	   - user => (refresh-all)	   
	7. To load code into the repl in inf-clojure, while in a Clojure
       file, use commands such as inf-clojure-eval-buffer via C-c
       C-b.
	8. To run the sample application
	   - user => (myapp.core/-main)
	8. To access the database, you can open another terminal from the
       host and then run psql.
	9. To stop the container
	   a. Exit the container
	   b. $ docker-compose down --volumes


##  Production Workflow
	1. Create the production image
	   - $ docker-compose build --force-rm clj-prod
	2. Run the production container
	   - $ docker-compose up --remove-orphans clj-prod
	3. To look inside the running container
	   - $ docker exec -it --detach-keys="ctrl-@" myapp_clj_prod_container "/bin/bash"
	   but note that you connect to a repl from the host  because no
	   ports are exposed.


## To Do
	1. Get SSL working for psql.  For example, 
	   $ psql postgresql://db-dev:5432/myapp?sslmode=require 
	2. Get SSL working with JDBC connection.
	
