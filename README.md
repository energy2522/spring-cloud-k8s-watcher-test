# About this project
This project shows how to use the spring-cloud-kubernetes configmap in integration with the spring-watcher to load properties from a kubernetes configmap and refresh them without restarting the pod.

# How to use
To deploy this demo you need to have a Kubernetes cluster or minikube local cluster.
This project has bash files like:
- `start-minikube.sh` to spin up a minikube cluster with proper configuration and deploys minikube dashboard.
- `build-image.sh` builds a docker image of the service and propagates it to the minikube.

## Kubernetes files
Kubernetes config files are located in the `k8s` folder. There are:
- `first-app.yaml` - this file contains a configmap for the service, NodePort service, and ClusterIp service for the spring-watcher, and deployment.
- `second-app.yaml` - this file contains the same as `first-app.yaml` but with different properties and can be used for the shared configmap demo.
- `configmap.yaml` - this file contains a shared Kubernetes configmap that can be used by `first-app.yaml` and `second-app.yaml`.
- `roles.yaml` - this file contains a list of roles that are required for the watcher and spring application to be able to read a configmap and identifies changes in it.
- `watcher.yaml` - this file contains a configuration to deploy the spring-watcher.

NOTES: all these files are configured to work in the `default` namespace.

## Deploy application
To deploy the application you need to execute:
1. ./start-minikube.sh
2. ./build-image.sh
3. kubectl apply -f k8s/roles.yaml
4. kubectl apply -f k8s/watcher.yaml
5. (optional) kubectl apply -f  k8s/configmap.yaml
6. (optional) kubectl apply -f k8s/secret.yaml
7. kubectl apply -f k8s/first-app.yaml
8. (optional) kubectl apply -f k8s/second-app.yaml

### Logs
After deploying the application you can review pod logs to see which properties each application has on startup.

## Configmap update (one application)
To demo how spring boot refresh works after configmap are changed you need to update configmap value in the `first-app.yaml` file.
Then execute `kubectl apply -f k8s/first-app.yaml`.
Then you can go to the watcher pod logs and find the log that the refresh request is scheduled in 12000 milliseconds (2 mins)
After 2 mins you can go to the service pod logs and find a log that refreshes the event that was executed and see the value.

## Shared configmap update
To demo how 2 services are updated after changes in one shared configmap you need to have deployed `second-app.yaml` and `configmap.yaml`.
When they are deployed, go to the `configmap.yaml` and change the value there, then execute `kubectl apply -f k8s/configmap.yaml`.
After that, you can go to the watcher pod logs to see that the refresh request is scheduled in 12000 milliseconds (2 mins).
In 2 mins, you can go to services pod logs and see that refresh was executed.

## Secret update (one application)
To demo how spring boot refresh works after secrets are changed you need to update secret value in the `first-app.yaml` file.
Then execute `kubectl apply -f k8s/first-app.yaml`.
Then you can go to the watcher pod logs and find the log that the refresh request is scheduled in 12000 milliseconds (2 mins)
After 2 mins you can go to the service pod logs and find a log that refreshes the event that was executed and see the value.

## Shared secret update
To demo how 2 services are updated after changes in one shared secret you need to have deployed `second-app.yaml` and `secret.yaml`.
When they are deployed, go to the `secret.yaml` and change the value there, then execute `kubectl apply -f k8s/secret.yaml`.
After that, you can go to the watcher pod logs to see that the refresh request is scheduled in 12000 milliseconds (2 mins).
In 2 mins, you can go to services pod logs and see that refresh was executed.

## Test by HTTP request
The service provides an endpoint to return some data based on properties. Using the GET request by the `/` path you can check if your configuration is changed.
If you are using minikube then you need to get an URL to be able to execute that request.
The commands are next:
- `minikube service first-server-app-service --url` - for the first-app
- `minikube service second-server-app-service --url` - for the second-app
  After executing this command, in the console, you will see the URL to the NodePort service.

## Useful commands
- `minikube service <service-name> --url` - get url to the service
- `kubectl apply -f <file>.yaml` - deploy kubernetes configfile
- `kubectl delete -f <file>.yaml` - delete kubernetes configfile
- `kubectl rollout restart deployment <deployment-name>` - re-deploy
- `minikube delete --all=false --purge=false` - delete minikube cluster. P.S. execute `minikube stop` before running this command.

## Useful links
- [Spring Cloud Kubernetes README](https://github.com/spring-cloud/spring-cloud-kubernetes/blob/main/README.adoc)
- [Spring Cloud Kubernetes Watcher image](https://hub.docker.com/r/springcloud/spring-cloud-kubernetes-configuration-watcher)
- [Watcher and Kubernetes service issue](https://github.com/spring-cloud/spring-cloud-kubernetes/issues/951)
- [Minikube doc](https://minikube.sigs.k8s.io/docs/start/)

