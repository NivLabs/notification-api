#!/bin/sh
## Server port number
export SERVER_PORT=8085

## ======== Locale Settings ======== ##
export ZONE_ID=America/Sao_Paulo

## ======== SMTP Settings ======== ##
## SMTP Server Host
export SMTP_HOST=
## SMTP Server Port
export SMTP_PORT=587
## SMTP Username
export SMTP_USERNAME=
## SMTP password
export SMTP_PASSWORD=

echo 'Starting NivLabs Notification Server...'

echo 'Zone Id: ' ${ZONE_ID}

echo 'SMTP Host: ' ${SMTP_HOST}
echo 'SMTP Port: ' ${SMTP_PORT}
echo 'SMTP user: ' ${SMTP_USERNAME}

mvn clean package -DskipTests=true

java -jar /root/repo/notification-api/target/notification-api.jar
