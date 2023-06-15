#!/bin/bash

cd ../

mvn clean package -DskipTests

cd local

docker-compose up --build
