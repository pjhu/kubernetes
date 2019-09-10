# reference
https://github.com/AliyunContainerService/k8s-for-docker-desktop

# install ingress
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/mandatory.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/cloud-generic.yaml
```

# image build
https://hub.docker.com/_/nginx  

# test ingress
```
kubectl get pods --all-namespaces -l app.kubernetes.io/name=ingress-nginx
```

# Deploy app
```
kubectl apply -f ec.yml
```

# Ingress
```
kubectl apply -f ingress.yaml
```

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
