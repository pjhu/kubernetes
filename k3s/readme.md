 # 准备
 #### vagrant
    ```
    vagrant up
    ```
#### login master
    ```
    vagrant ssh master
    vagrant ssh node1
    vagrant ssh node2
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
#### 安装docker
> k3s安装docker不是必须的，k3s默认使用container
```
https://docs.docker.com/engine/install/centos/
```

# 在三台机器上安装etcd

#### 安装文档
```
https://etcd.io/docs/v3.5/op-guide/clustering/
sudo yum install -y etcd
```

#### 在三台机器上执行如下操作
```
sudo vi /etc/etcd/etcd.conf     // 三机器复制配置
sudo rm -rf  /var/lib/etcd/default.etcd/member    // 三机器删除目录
sudo systemctl daemon-reload    // 三机器重新加载配置
sudo systemctl start etcd.service  // 三台机器顺序启动，最好在30s内，如果找不到其它机器会超时失败
```

- 第一台[192.168.56.1], 复制配置到etcd.conf
```
#[Member]
ETCD_DATA_DIR="/var/lib/etcd/default.etcd"
ETCD_LISTEN_PEER_URLS="http://192.168.56.1:2380"
ETCD_LISTEN_CLIENT_URLS="http://192.168.56.1:2379,http://127.0.0.1:2379"
ETCD_NAME="etcd1"

#[Clustering]
ETCD_INITIAL_ADVERTISE_PEER_URLS="http://192.168.56.1:2380"
ETCD_ADVERTISE_CLIENT_URLS="http://192.168.56.1:2379"
ETCD_INITIAL_CLUSTER="etcd1=http://192.168.56.1:2380,etcd2=http://192.168.56.2:2380,etcd3=http://192.168.56.3:2380"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster"
ETCD_INITIAL_CLUSTER_STATE="new"
```

- 第二台[192.168.56.2], 复制配置到etcd.conf
```
#[Member]
ETCD_DATA_DIR="/var/lib/etcd/default.etcd"
ETCD_LISTEN_PEER_URLS="http://192.168.56.2:2380"
ETCD_LISTEN_CLIENT_URLS="http://192.168.56.2:2379,http://127.0.0.1:2379"
ETCD_NAME="etcd2"

#[Clustering]
ETCD_INITIAL_ADVERTISE_PEER_URLS="http://192.168.56.2:2380"
ETCD_ADVERTISE_CLIENT_URLS="http://192.168.56.2:2379"
ETCD_INITIAL_CLUSTER="etcd1=http://192.168.56.1:2380,etcd2=http://192.168.56.2:2380,etcd3=http://192.168.56.3:2380"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster"
ETCD_INITIAL_CLUSTER_STATE="new"
```

- 第三台[192.168.56.3], 复制配置到etcd.conf
```
#[Member]
ETCD_DATA_DIR="/var/lib/etcd/default.etcd"
ETCD_LISTEN_PEER_URLS="http://192.168.56.3:2380"
ETCD_LISTEN_CLIENT_URLS="http://192.168.56.3:2379,http://127.0.0.1:2379"
ETCD_NAME="etcd3"

#[Clustering]
ETCD_INITIAL_ADVERTISE_PEER_URLS="http://192.168.56.3:2380"
ETCD_ADVERTISE_CLIENT_URLS="http://192.168.56.3:2379"
ETCD_INITIAL_CLUSTER="etcd1=http://192.168.56.1:2380,etcd2=http://192.168.56.2:2380,etcd3=http://192.168.56.3:2380"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster"
ETCD_INITIAL_CLUSTER_STATE="new"
```

#### 测试
```
[root@master ~]# etcdctl member list
35562bff4f0d6e55: name=etcd2 peerURLs=http://192.168.56.2:2380 clientURLs=http://192.168.56.2:2379 isLeader=false
98055c737238f08a: name=etcd1 peerURLs=http://192.168.56.1:2380 clientURLs=http://192.168.56.1:2379 isLeader=true
f6e21250ef798b6d: name=etcd3 peerURLs=http://192.168.56.3:2380 clientURLs=http://192.168.56.3:2379 isLeader=false


[vagrant@master ~]$ etcdctl set /testdir/testkey "hello world"
hello world
[vagrant@node1 ~]$ etcdctl get /testdir/testkey
hello world
[vagrant@node1 ~]$
```

# 在三台机器上安装etcd(TLS版)
#### 参考文档
```
https://etcd.io/docs/v3.5/op-guide/clustering/
https://blog.horus-k.com/2020/09/22/Etcd-%E9%97%AE%E9%A2%98-%E8%B0%83%E4%BC%98-%E7%9B%91%E6%8E%A7/
```

#### 生成证书
```
https://github.com/coreos/docs/blob/master/os/generate-self-signed-certificates.md
https://cloud.tencent.com/developer/article/1557551
```

#### 将证书copy到三台机器上,并更新证书
```
sudo mkdir -pv /etc/ssl/etcd/
sudo cp ~/cfssl/* /etc/ssl/etcd/
sudo chown -R etcd:etcd /etc/ssl/etcd
sudo chmod 600 /etc/ssl/etcd/*-key.pem
sudo cp ~/cfssl/ca.pem /etc/ssl/certs/

sudo yum install ca-certificates -y
sudo update-ca-trust
```

#### 配置etcd
```
sudo vi /etc/etcd/etcd.conf
```

- 第一台[192.168.56.1], 复制配置到etcd.conf
```
#[Member]
ETCD_DATA_DIR="/var/lib/etcd/default.etcd"
ETCD_LISTEN_PEER_URLS="https://192.168.56.1:2380"
ETCD_LISTEN_CLIENT_URLS="https://192.168.56.1:2379,https://127.0.0.1:2379"
ETCD_NAME="etcd1"

#[Clustering]
ETCD_INITIAL_ADVERTISE_PEER_URLS="https://192.168.56.1:2380"
ETCD_ADVERTISE_CLIENT_URLS="https://192.168.56.1:2379,https://127.0.0.1:2379"
ETCD_INITIAL_CLUSTER="etcd1=https://192.168.56.1:2380,etcd2=https://192.168.56.2:2380,etcd3=https://192.168.56.3:2380"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster"
ETCD_INITIAL_CLUSTER_STATE="new"

#[security]
ETCD_CERT_FILE="/etc/ssl/etcd/server.pem"
ETCD_KEY_FILE="/etc/ssl/etcd/server-key.pem"
ETCD_TRUSTED_CA_FILE="/etc/ssl/etcd/ca.pem"
ETCD_CLIENT_CERT_AUTH="true"
ETCD_PEER_CERT_FILE="/etc/ssl/etcd/member.pem"
ETCD_PEER_KEY_FILE="/etc/ssl/etcd/member-key.pem"
ETCD_PEER_TRUSTED_CA_FILE="/etc/ssl/etcd/ca.pem"
ETCD_PEER_CLIENT_CERT_AUTH="true"
#[logging]
ETCD_DEBUG="true"
ETCD_LOG_PACKAGE_LEVELS="etcdserver=WARNING,security=DEBUG"
```

- 第二台[192.168.56.2], 复制配置到etcd.conf
```
#[Member]
ETCD_DATA_DIR="/var/lib/etcd/default.etcd"
ETCD_LISTEN_PEER_URLS="https://192.168.56.2:2380"
ETCD_LISTEN_CLIENT_URLS="https://192.168.56.2:2379,https://127.0.0.1:2379"
ETCD_NAME="etcd2"

#[Clustering]
ETCD_INITIAL_ADVERTISE_PEER_URLS="https://192.168.56.2:2380"
ETCD_ADVERTISE_CLIENT_URLS="https://192.168.56.2:2379"
ETCD_INITIAL_CLUSTER="etcd1=https://192.168.56.1:2380,etcd2=https://192.168.56.2:2380,etcd3=https://192.168.56.3:2380"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster"
ETCD_INITIAL_CLUSTER_STATE="new"

#[security]
ETCD_CERT_FILE="/etc/ssl/etcd/server.pem"
ETCD_KEY_FILE="/etc/ssl/etcd/server-key.pem"
ETCD_TRUSTED_CA_FILE="/etc/ssl/etcd/ca.pem"
ETCD_CLIENT_CERT_AUTH="true"
ETCD_PEER_CERT_FILE="/etc/ssl/etcd/member.pem"
ETCD_PEER_KEY_FILE="/etc/ssl/etcd/member-key.pem"
ETCD_PEER_TRUSTED_CA_FILE="/etc/ssl/etcd/ca.pem"
ETCD_PEER_CLIENT_CERT_AUTH="true"
#[logging]
ETCD_DEBUG="true"
ETCD_LOG_PACKAGE_LEVELS="etcdserver=WARNING,security=DEBUG"
```

- 第三台[192.168.56.3], 复制配置到etcd.conf
```
#[Member]
ETCD_DATA_DIR="/var/lib/etcd/default.etcd"
ETCD_LISTEN_PEER_URLS="https://192.168.56.3:2380"
ETCD_LISTEN_CLIENT_URLS="https://192.168.56.3:2379,https://127.0.0.1:2379"
ETCD_NAME="etcd3"

#[Clustering]
ETCD_INITIAL_ADVERTISE_PEER_URLS="https://192.168.56.3:2380"
ETCD_ADVERTISE_CLIENT_URLS="https://192.168.56.3:2379,https://127.0.0.1:2379"
ETCD_INITIAL_CLUSTER="etcd1=https://192.168.56.1:2380,etcd2=https://192.168.56.2:2380,etcd3=https://192.168.56.3:2380"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster"
ETCD_INITIAL_CLUSTER_STATE="new"

#[security]
ETCD_CERT_FILE="/etc/ssl/etcd/server.pem"
ETCD_KEY_FILE="/etc/ssl/etcd/server-key.pem"
ETCD_TRUSTED_CA_FILE="/etc/ssl/etcd/ca.pem"
ETCD_CLIENT_CERT_AUTH="true"
ETCD_PEER_CERT_FILE="/etc/ssl/etcd/member.pem"
ETCD_PEER_KEY_FILE="/etc/ssl/etcd/member-key.pem"
ETCD_PEER_TRUSTED_CA_FILE="/etc/ssl/etcd/ca.pem"
ETCD_PEER_CLIENT_CERT_AUTH="true"
#[logging]
ETCD_DEBUG="true"
ETCD_LOG_PACKAGE_LEVELS="etcdserver=WARNING,security=DEBUG"
```

#### 配置启动服务文件
```
sudo systemctl stop etcd.service
sudo rm -rf /var/lib/etcd/default.etcd/member
sudo systemctl status etcd.service
sudo systemctl daemon-reload
sudo systemctl start etcd.service
```

#### 测试
```
[vagrant@master cfssl]$ sudo etcdctl --endpoints https://192.168.56.1:2379,https://192.168.56.2:2379,https://192.168.56.3:2379 --ca-file /etc/ssl/etcd/ca.pem --cert-file /etc/ssl/etcd/client.pem --key-file /etc/ssl/etcd/client-key.pem member list

3ec8d20e7e3a97b3: name=etcd2 peerURLs=https://192.168.56.2:2380 clientURLs=https://127.0.0.1:2379,https://192.168.56.2:2379 isLeader=false
5df9e4d0fd9f3fab: name=etcd3 peerURLs=https://192.168.56.3:2380 clientURLs=https://127.0.0.1:2379,https://192.168.56.3:2379 isLeader=false
f760d17d3dc53737: name=etcd1 peerURLs=https://192.168.56.1:2380 clientURLs=https://127.0.0.1:2379,https://192.168.56.1:2379 isLeader=true
```

# 在三台机器上安装k3s

#### 安装文档
```
https://rancher.com/docs/k3s/latest/en/installation/ha/
https://docs.rancher.cn/docs/k3s/_index
```

#### 下载k3s
```
https://github.com/k3s-io/k3s/releases/download/v1.22.7+k3s1/k3s

sudo cp k3s /usr/local/bin/
scp k3s root@192.168.56.3:/usr/local/bin/
scp k3s root@192.168.56.2:/usr/local/bin/
```

#### 在三台机器上安装必要的包
> 可以先执行安装k3s, 会提示安装对用的包
```
sudo yum install -y container-selinux
sudo yum install -y https://rpm.rancher.io/k3s/stable/common/centos/7/noarch/k3s-selinux-0.4-1.el7.noarch.rpm
```

#### 在三台机器上安装k3s
```
curl -sfL https://get.k3s.io | INSTALL_K3S_SKIP_DOWNLOAD=true sh -s - server --token=SECRET --datastore-endpoint="http://192.168.56.1:2379,http://192.168.56.2:2379,http://192.168.56.3:2379"
```

#### 在三台机器上安装k3s(使用tls etcd)
```
curl -sfL https://get.k3s.io | INSTALL_K3S_SKIP_DOWNLOAD=true K3S_DATASTORE_CAFILE=/etc/ssl/etcd/ca.pem K3S_DATASTORE_CERTFILE=/etc/ssl/etcd/client.pem K3S_DATASTORE_KEYFILE=/etc/ssl/etcd/client-key.pem sh -s - server --token=SECRET --datastore-endpoint="https://192.168.56.1:2379,https://192.168.56.2:2379,https://192.168.56.3:2379"
```

#### 测试
>当执行kubectl get nodes是报如下错误可参考
>https://github.com/k3s-io/k3s/issues/389

mkdir ~/.kube
sudo cat /etc/rancher/k3s/k3s.yaml > ~/.kube/config

export KUBECONFIG=~/.kube/config to your ~/.bashrc

sudo systemctl status k3s.service
kubectl get nodes

#### 测试etcd keys
```
ETCDCTL_API=3 etcdctl --endpoints=<etcd_ip>:2379 get / --prefix --keys-only
ETCDCTL_API=3 etcdctl --endpoints <etcd_ip>:2379 --cacert <ca_cert_path> --cert <cert_path> --key <cert_key_path> get / --prefix --keys-only
```

#### 卸载k3s
```
sudo systemctl stop k3s.service
/usr/local/bin/k3s-uninstall.sh
```