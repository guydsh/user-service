#!/bin/bash
docker-compose --compatibility down
docker rmi user-service
mvn clean package -P docker
docker build -t user-service .
docker-compose --compatibility up -d

