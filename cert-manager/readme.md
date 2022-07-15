# 参考
https://learnku.com/articles/42528

https://imroc.cc/k8s/trick/sign-free-certs-with-cert-manager/

# 安装
```
helm install ......
k apply -f issuer.yaml
k apply -f ca.yaml
```