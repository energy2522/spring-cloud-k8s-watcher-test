#/bin/bash
./gradlew clean bootJar && eval $(minikube docker-env) && docker build -t server-app .
