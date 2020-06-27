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
cd ..
kubectl apply -f traefik/
```

## traefik UI
```
http://localhost:8080/dashboard
```

## test server
```
http://localhost:8000
```