 # 准备
 #### vagrant
    ```
    vagrant up
    ```
#### login master
    ```
    vagrant ssh master
    ```
#### 关闭防火墙
  - close selinux(need reboot)
    ```
    setenforce 0
    ```
    https://linuxize.com/post/how-to-disable-selinux-on-centos-7/

  - close iptables
    ```
    systemctl disable firewalld
    systemctl stop firewalld
    systemctl status firewalld
    ```

# 安装k3s

#### 安装文档
```
https://rancher.com/docs/k3s/latest/en/installation/ha/
https://docs.rancher.cn/docs/k3s/_index
```

#### 下载k3s
```
https://github.com/k3s-io/k3s/releases/download/v1.22.7+k3s1/k3s

sudo cp k3s /usr/local/bin/
```

#### 安装必要的包
> 可以先执行安装k3s, 会提示安装对用的包
```
sudo yum install -y container-selinux
sudo yum install -y https://rpm.rancher.io/k3s/stable/common/centos/7/noarch/k3s-selinux-0.4-1.el7.noarch.rpm
```

#### 安装k3s
```
curl -sfL https://get.k3s.io | INSTALL_K3S_SKIP_DOWNLOAD=true sh -s - server --token=SECRET --disable traefik
```

#### 别名
alias k=kubectl
alias docker=crictl

#### 测试
>当执行kubectl get nodes是报如下错误可参考
>https://github.com/k3s-io/k3s/issues/389

mkdir ~/.kube
sudo cat /etc/rancher/k3s/k3s.yaml > ~/.kube/config

export KUBECONFIG=~/.kube/config to your ~/.bashrc

sudo systemctl status k3s.service
kubectl get nodes

#### 卸载k3s
```
sudo systemctl stop k3s.service
/usr/local/bin/k3s-uninstall.sh
```

#### delete traefik
```
 --disable traefik

并执行：https://github.com/k3s-io/k3s/issues/1160
kubectl -n kube-system delete helmcharts.helm.cattle.io traefik
```

# install istio

#### 参考
```
https://konghq.com/blog/istio-gateway
```

#### Install helm
  ```
  https://helm.sh/docs/intro/install/
  ```

#### 安装istio
```
istioctl install --set profile=minimal -y
```

#### 安装kong
```
kubectl create namespace kong
kubectl label namespace kong istio-injection=enabled
helm repo add kong https://charts.konghq.com && helm repo update
helm install -n kong kong-istio kong/kong -f kong.yaml
```

#### 部署bookinfo
```
kubectl create namespace bookinfo
kubectl label namespace bookinfo istio-injection=enabled

kubectl apply -f samples/bookinfo/platform/kube/bookinfo.yaml -n bookinfo

kubectl apply -f bookinfo-ingress.yaml
```

#### 测试
```
https://xxx.xx.xx.xxx/productpage
```

# 配置自签名证书

```
https://cert-manager.io/docs/installation/helm/#option-2-install-crds-as-part-of-the-helm-release

k create secret tls kong-proxy-ali-tls --cert=/root/cert/aliyun_ssl/8124086_yiban.pcqmm.com.pem --key=/root/cert/aliyun_ssl/8124086_yiban.pcqmm.com.key -n kong

helm upgrade -n kong kong-istio kong/kong -f kong.yaml
```

# 删除
```
sh samples/bookinfo/platform/kube/cleanup.sh
k get all -n bookinfo
k delete namespace bookinfo

k delete all --all -n {namespace}
k delete namespace {namespace}
```