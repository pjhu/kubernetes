# enable K8S Dashboard
## install
https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/
```
➜  k8s-for-desktop git:(master) ✗ k apply -f recommended.yaml
namespace/kubernetes-dashboard created
serviceaccount/kubernetes-dashboard created
service/kubernetes-dashboard created
secret/kubernetes-dashboard-certs created
secret/kubernetes-dashboard-csrf created
secret/kubernetes-dashboard-key-holder created
configmap/kubernetes-dashboard-settings created
role.rbac.authorization.k8s.io/kubernetes-dashboard created
clusterrole.rbac.authorization.k8s.io/kubernetes-dashboard created
rolebinding.rbac.authorization.k8s.io/kubernetes-dashboard created
clusterrolebinding.rbac.authorization.k8s.io/kubernetes-dashboard created
deployment.apps/kubernetes-dashboard created
service/dashboard-metrics-scraper created
deployment.apps/dashboard-metrics-scraper created
```

## run
```
k proxy
```

## login
```
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
```

## get token
https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md
```
kubectl -n kubernetes-dashboard describe secret $(kubectl -n kubernetes-dashboard get secret | grep admin-user | awk '{print $1}')  
```

# default setup
## default install images
```
➜  k8s-for-desktop git:(master) ✗ docker images
REPOSITORY                           TAG                 IMAGE ID            CREATED             SIZE
k8s.gcr.io/kube-proxy                v1.14.6             ed8adf767eeb        4 weeks ago         82.1MB
k8s.gcr.io/kube-apiserver            v1.14.6             0e422c9884cf        4 weeks ago         209MB
k8s.gcr.io/kube-scheduler            v1.14.6             d27987bc993e        4 weeks ago         81.6MB
k8s.gcr.io/kube-controller-manager   v1.14.6             4bb274b1f2c3        4 weeks ago         157MB
docker/kube-compose-controller       v0.4.23             a8c3d87a58e7        3 months ago        35.3MB
docker/kube-compose-api-server       v0.4.23             f3591b2cb223        3 months ago        49.9MB
k8s.gcr.io/coredns                   1.3.1               eb516548c180        8 months ago        40.3MB
k8s.gcr.io/etcd                      3.3.10              2c4adeb21b4f        9 months ago        258MB
k8s.gcr.io/pause                     3.1                 da86e6ba6ca1        21 months ago       742kB
```

## default server
```
➜  k8s-for-desktop git:(master) ✗ k get svc --all-namespaces
NAMESPACE     NAME          TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)                  AGE
default       kubernetes    ClusterIP   10.96.0.1      <none>        443/TCP                  4m45s
docker        compose-api   ClusterIP   10.108.35.96   <none>        443/TCP                  3m27s
kube-system   kube-dns      ClusterIP   10.96.0.10     <none>        53/UDP,53/TCP,9153/TCP   4m44s
```

## default deployment
```
➜  k8s-for-desktop git:(master) ✗ k get deployments --all-namespaces
NAMESPACE     NAME          READY   UP-TO-DATE   AVAILABLE   AGE
docker        compose       1/1     1            1           4m2s
docker        compose-api   1/1     1            1           4m2s
kube-system   coredns       2/2     2            2           5m19s
```

## default pods
```
➜  k8s-for-desktop git:(master) ✗ k get pods --all-namespaces
NAMESPACE     NAME                                     READY   STATUS    RESTARTS   AGE
docker        compose-6c67d745f6-lg69n                 1/1     Running   1          2m57s
docker        compose-api-57ff65b8c7-cvlr6             1/1     Running   1          2m57s
kube-system   coredns-584795fc57-mdt6n                 1/1     Running   1          4m8s
kube-system   coredns-584795fc57-zl5wf                 1/1     Running   1          4m8s
kube-system   etcd-docker-desktop                      1/1     Running   1          3m12s
kube-system   kube-apiserver-docker-desktop            1/1     Running   1          3m18s
kube-system   kube-controller-manager-docker-desktop   1/1     Running   1          2m57s
kube-system   kube-proxy-zg67t                         1/1     Running   1          4m8s
kube-system   kube-scheduler-docker-desktop            1/1     Running   1          2m58s
```
