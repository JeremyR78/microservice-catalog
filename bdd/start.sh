#! /bin/bash

export MYSQL_DATABASE="catalog-db"
export MYSQL_USER="root"
export MYSQL_PASSWORD="root"
export MYSQL_ROOT_PASSWORD="root"

echo "START DOCKER "

docker-compose up

echo "STOP"
