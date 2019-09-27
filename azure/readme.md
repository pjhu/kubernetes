# app code
https://github.com/pjhu/react-tutorial

# setup ingress-nginx
https://github.com/AliyunContainerService/k8s-for-docker-desktop

## install ingress
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud-generic.yaml
```

## image build
https://hub.docker.com/_/nginx  

## test ingress
```
kubectl get pods --all-namespaces -l app.kubernetes.io/name=ingress-nginx
```

## Deploy app
```
kubectl apply -f ec.yml
```

## Ingress
```
kubectl apply -f ingress.yaml
```

# Azure AKS

## install Azure CLI
```
brew update && brew install azure-cli
```

## Azure Login

### login
```
az login
```
<!--
在 Azure China 中使用 Azure CLI 2.0 之前，请首先运行 az cloud set -n AzureChinaCloud 更改云环境。
如果要切换回 Global Azure，请再次运行 az cloud set -n AzureCloud。
-->

### get credential
```
az aks get-credentials --resource-group ec --name ec-aks-prod
```

### test
```
kubectl get nodes
```

## docker registry

### login
```
az acr login --name econtaineregistry
```

### Grant AKS access to ACR  
https://docs.microsoft.com/en-us/azure/container-registry/container-registry-auth-aks
```
./acr-role.sh
```

### tag
```
docker tag backend:1.0 econtaineregistry.azurecr.io/backend:1.0
```

### push
```
docker push econtaineregistry.azurecr.io/backend:1.0
```

### list
```
az acr repository list --name econtaineregistry --output table
```

## dashboard
```
az aks browse --resource-group ec --name ec-aks-prod

```

## setup traefik 101
https://medium.com/@geraldcroes/kubernetes-traefik-101-when-simplicity-matters-957eeede2cf8

## traefik setup for backend service
https://docs.traefik.io/user-guide/kubernetes/

```
k apply -f traefik-accout.yml
k apply -f traefik-service.yaml
k apply -f traefik-backend-service.yml
k apply -f traefik-backend-ingress.yml
```

## test
```
curl -H Host:ec.localhost localhost:30001/admin/posts
curl -H Host:ec.localhost localhost:30001/actuator/health
```

## traefik dashboard
https://medium.com/@geraldcroes/kubernetes-traefik-101-when-simplicity-matters-957eeede2cf8
```
k apply -f traefik-web-ui.yml
http://dashboard.localhost:30002/dashboard/
```

# expose service
use nodePort expose traefik, application gateway in developing

## application gateway for k8s release version: 0.9
https://github.com/Azure/application-gateway-kubernetes-ingress
https://azure.github.io/application-gateway-kubernetes-ingress/

## key vault for aks release version: 0.0.13
https://github.com/Azure/kubernetes-keyvault-flexvol

# health check(livenessProbe & readinessProbe)
initialDelaySeconds替换开始时间

# log
https://medium.com/@nanduni/container-monitoring-using-splunk-3a0971209a16
https://docs.docker.com/config/containers/logging/splunk/
```
    docker run --log-driver=splunk \
           --log-opt splunk-token=176FCEBF-4CF5-4EDF-91BC-703796522D20 \
           --log-opt splunk-url=https://splunkhost:8088 \
           --log-opt splunk-capath=/path/to/cert/cacert.pem \
           --log-opt splunk-caname=SplunkServerDefaultCert \
           --log-opt tag="{{.Name}}/{{.FullID}}" \
           --log-opt labels=location \
           --log-opt env=TEST \
           --env "TEST=false" \
           --label location=west \
       your/applications
```


# traefik create by helm
```
helm install stable/traefik
```

```
LAST DEPLOYED: Wed Sep 11 17:03:08 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/ConfigMap
NAME                        DATA  AGE
alert-catfish-traefik       1     1s
alert-catfish-traefik-test  1     1s

==> v1/Deployment
NAME                   READY  UP-TO-DATE  AVAILABLE  AGE
alert-catfish-traefik  0/1    1           0          1s

==> v1/Pod(related)
NAME                                    READY  STATUS             RESTARTS  AGE
alert-catfish-traefik-644db58678-66trg  0/1    ContainerCreating  0         1s

==> v1/Service
NAME                   TYPE          CLUSTER-IP   EXTERNAL-IP  PORT(S)                     AGE
alert-catfish-traefik  LoadBalancer  10.0.150.57  <pending>    80:30547/TCP,443:32318/TCP  1s

```
