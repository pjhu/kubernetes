# Reference
https://docs.traefik.io/user-guides/crd-acme/

## setup env
(1) rbac
```run
kubectl apply -f traefik/rbac.yml
```

```output
clusterrole.rbac.authorization.k8s.io/traefik-ingress-controller created
serviceaccount/traefik-ingress-controller created
clusterrolebinding.rbac.authorization.k8s.io/traefik-ingress-controller created
```

(2) resource definition
```run
kubectl apply -f traefik/resource_definition.yml
```

```output
customresourcedefinition.apiextensions.k8s.io/ingressroutes.traefik.containo.us created
customresourcedefinition.apiextensions.k8s.io/middlewares.traefik.containo.us created
customresourcedefinition.apiextensions.k8s.io/ingressroutetcps.traefik.containo.us created
customresourcedefinition.apiextensions.k8s.io/ingressrouteudps.traefik.containo.us created
customresourcedefinition.apiextensions.k8s.io/traefikservices.traefik.containo.us created
```

(3) traefik
```run
kubectl apply -f traefik/traefik.yml
```

```
deployment.apps/traefik created
service/traefik created
```

## traefik UI
```
http://127.0.0.1:8088/dashboard/
```

## 部署服务
```
kubectl create namespace dev
k apply -f traefik/whoami.yml
```

## test server
```
kubectl port-forward --address 0.0.0.0 service/traefik 80:80 8088:8088 -n kube-system
http://localhost:80/notls
```