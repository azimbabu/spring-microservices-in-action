#!/bin/sh
echo "********************************************************"
echo "Starting Configuration Server"
echo "********************************************************"
java -jar /usr/local/config-service/@project.build.finalName@.jar
