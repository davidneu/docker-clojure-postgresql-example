# Setting Up a Docker Development and Production Environment for Clojure and Postgresql Applications


## Overview
This project shows how to use Docker and Docker Compose to develop and
deploy Clojure and Postgresql based applications.  It uses Emacs with
inf-clojure, Leiningen, and Clojure's socket repl.


## Setup
	Clone this repository, cd into its directory, and follow the
    remaining steps.  You may wish to copy this README.md file to a
    another directory since it will be overwritten in the steps below.


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


## Build Docker Java Image
	Follow the instructions at
    https://github.com/dartmouth-ic3d/docker-java to build a Docker
    image for Ubuntu Linux that includes the Java JDK.


##  Development Database Setup
	1. $ ./myapp build
	2. $ ./myapp up
	3. Using an editor on the host, add the following lines to the
       file /data/myapp_db_dev_volume/pg_hba.conf on the host
	   local    all      postgres                md5
	   local	all		 myapp		 			 md5
	   host 	all		 myapp		samenet		 md5
	   The lines should be below the line "Put your actual
       configuration here".  Be sure to comment out all other lines.
	4. $ ./myapp db
	5. $ psql -U postgres
	6. postgres=# alter user postgres with password 'postgrespw';
	7. select pg_reload_conf();
	   - Or, on the host run
	   $ docker exec --user postgres --workdir /usr/lib/postgresql/10/bin myapp_db_dev_container pg_ctl reload
	8. Exit psql
	9. $ createuser myapp -d -s -P -U postgres
	10. $ createdb -U myapp myapp
	11. Exit the db-dev container.
	12. $ ./myapp clj
	13. $ psql -U myapp -d myapp -h db-dev
	14. To avoid being prompted for a password, create the file
		resources/.pgpass, with permissions 600, and add the following line
		  db-dev:5432:myapp:myapp:myapppw
	    Now you can type
		  $ export PGPASSFILE=/myapp/resources/db/.pgpass; psql -U myapp -d myapp -h db-dev
	15. myapp=# \i ./resources/db/createdb.sql
	16. Exit the clj-dev container.
	17. Stop the containers
		- $ ./myapp down


##  Development Workflow
	1. $ ./myapp build
	2. $ ./myapp up
	3. $ ./myapp clj
	4. $ repl
	   - Dockerfile-dev puts a convenience script with this command in /usr/local/bin/repl.
	5. To connect to the repl from emacs
	   a. M-x cd
	      - Select the myapp project directory.
	   a. M-x inf-clojure
	   b. 0.0.0.0
	   c. 5555
	6. To load the application
	   - user => (reset)
	7. To load code into the repl in inf-clojure, while in a Clojure
       file, use commands such as inf-clojure-eval-buffer via C-c
       C-b.
	8. To run the sample application
	   - user => (myapp.core/-main)
	9. To access the database, you can open another terminal from the
       host and run ./myapp psql.  If you've run the sample
       application you can then see the results:
	   - # select * from myapp.sample;
	9. Exit both containers.
	10. Stop the containers
	   - $ ./myapp down


##  Multiple Environments
    Running multiple environments on a single machine requires changes
    to the docker-compose.yml file.  Each environment must have unique
    values for
	- the volumes key in the db-dev (db-prod) container, and
	- the ports keys in the clj-dev (clj-prod) container.
	Unique volume key values provide each environemt with its own
	Postgresql data directory, and unique ports keys are required to
	avoid conflicts between the web server and Clojure socket ports
	used by each environment.


##  Production Workflow
	1. Create the production image
	   - $ ./myapp build-prod
	2. Run the production container
	   - $ ./myapp up-prod
	3. To look inside the running container
	   - $ docker exec -it --detach-keys="ctrl-@" myapp_clj_prod_container "/bin/bash"
	   but note that you cannot connect to a repl from the host
	   because no ports are exposed.


##  Emacs
	1. To use sql-postgres mode to access the database from the host,
       put the following in your init.el file, substituting the
       correct path for the sql-postgres-program variable: 

	   (use-package sql-mode
		   :init
		   (setq sql-postgres-program "~/projects/myapp/myapp-psql")
		   (setq sql-postgres-login-params nil))

	   If you don't use use-package, simply setting the
	   sql-postgres-program and sql-postgres-login-params should
	   work.

	2. It can helpful to add a function such as the following to init.el:

	   (defun myapp-buffers ()
		   (interactive)
		   (cd "~/projects/myapp")
		   (ansi-term "/bin/bash" "docker-clj-psql")
		   (end-of-buffer)
		   (ansi-term "/bin/bash" "docker-clj-repl")
		   (end-of-buffer))


## Follow Up
	1. To change the name of the project
	   $ ./set-project-name.sh newname
	2. To handle SSL traffic see, [Setting Up an Nginx SSL Reverse Proxy in a Docker Container](https://github.com/davidneu/docker-nginx-ssl-reverse-proxy-example).

