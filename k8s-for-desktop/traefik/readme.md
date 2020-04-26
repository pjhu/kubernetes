# Reference
https://docs.traefik.io/user-guides/crd-acme/

## IngressRoute
remove TLS

##### run
```
kubectl apply -f traefik-IngressRoute.yml
```

##### output
```
customresourcedefinition.apiextensions.k8s.io/ingressroutes.traefik.containo.us created
customresourcedefinition.apiextensions.k8s.io/middlewares.traefik.containo.us created
customresourcedefinition.apiextensions.k8s.io/ingressroutetcps.traefik.containo.us created
customresourcedefinition.apiextensions.k8s.io/ingressrouteudps.traefik.containo.us created
customresourcedefinition.apiextensions.k8s.io/traefikservices.traefik.containo.us created
clusterrole.rbac.authorization.k8s.io/traefik-ingress-controller created
clusterrolebinding.rbac.authorization.k8s.io/traefik-ingress-controller created
```

## Services

##### run
```
kubectl apply -f traefik-Services.yml
```

##### output
```
service/traefik created
service/whoami created
```

## Deployment

##### run
```
kubectl apply -f traefik-Deployment.yml
```

##### output
```
serviceaccount/traefik-ingress-controller created
deployment.apps/traefik created
deployment.apps/whoami created
```

## routes

##### run
```
kubectl apply -f traefik-Routers.yml
```

##### output
```
ingressroute.traefik.containo.us/simpleingressroute created
```

## get all services

##### run
```
 k get svc -A
```

##### output
```
```

## traefik UI
```
http://localhost:30808
```
