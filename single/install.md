### setup vagrant
- download box
https://cloud.centos.org/centos/7/vagrant/x86_64/images/
- add box
    ```
    vagrant box add ~/Downloads/CentOS-7-x86_64-Vagrant-1805_01.VirtualBox.box --name centos7
    vagrant up
    vagrant ssh single
    ```
- close selinux(need reboot)
    ```
    sudo vi /etc/selinux/config
    ```

- close iptables
    ```
    systemctl disable firewalld
    systemctl stop firewalld
    systemctl status firewalld
    ```

- test
    ```
    reboot
    vagrant ssh single
    python -m SimpleHTTPServer 8080
    ```
    
- install docker
    ```
    curl -fsSL "https://get.docker.com/" | sh
    systemctl enable docker && systemctl start docker
    ```

### setup k8s

- download
    ```
    vagrant ssh single
    yum install wget
    wget https://github.com/kubernetes/kubernetes/releases/download/v1.11.0-beta.2/kubernetes.tar.gz
    tar -zxf kubernetes.tar.gz
    ```
    
- get client/server, can find in client/server directory
kubernetes-server-linux-amd64.tar.gz
kubernetes-client-linux-amd64.tar.gz
    ```
    ./kubernetes/cluster/get-kube-binaries.sh 
    cd kubernetes/server/ && tar -zxf kubernetes-server-linux-amd64.tar.gz
    ```
    
### setup etcd 
- download
    ```
    cd && wget https://github.com/coreos/etcd/releases/download/v3.3.8/etcd-v3.3.8-linux-amd64.tar.gz
    
    tar -zxf etcd-v3.3.8-linux-amd64.tar.gz
    mv etcd-v3.3.8-linux-amd64/etcd* /usr/local/bin/
    sudo groupadd etcd && sudo useradd -c "Etcd user" -g etcd -s /sbin/nologin -r etcd
    ```
    
- edit config
    ```
    mkdir -p /opt/kubernetes/cfg/
    mkdir -p /var/lib/etcd
    vi ./kubernetes/cluster/centos/master/scripts/etcd.sh
    ```
- start etcd    
    ```
    cd && ./kubernetes/cluster/centos/master/scripts/etcd.sh
    systemctl status etcd
    ```
### other
- kube-apiserver
    ```
    cp ./kubernetes/server/kubernetes/server/bin/kube-apiserver /usr/local/bin/
    注释证书
    改变ExecStart
    ./kubernetes/cluster/centos/master/scripts/apiserver.sh
    curl 192.168.45.53:8080
    ```
- kube-controller-manager
    ```
    cp ./kubernetes/server/kubernetes/server/bin/kube-controller-manager /usr/local/bin/
    注释证书
    改变ExecStart
    ./kubernetes/cluster/centos/master/scripts/controller-manager.sh
    ```
- kube-scheduler 
    ```
    cp ./kubernetes/server/kubernetes/server/bin/kube-scheduler /usr/local/bin/
    ./kubernetes/cluster/centos/master/scripts/scheduler.sh
    ```    

- kubectl
    ```
    cd kubernetes/client/
    tar -zxf kubernetes-client-linux-amd64.tar.gz
    cd && cp kubernetes/client/kubernetes/client/bin/kubectl /usr/local/bin/
    kubectl get  nodes
    ```
    
- kubelet
    ```
    cp ./kubernetes/server/kubernetes/server/bin/kubelet /usr/local/bin/
    ./kubernetes/cluster/centos/node/scripts/kubelet.sh
    ```
    > if active failed: failed to run Kubelet: Running with swap on is not supported, please disable swap! or set --fail-swap-on flag to fals, run follow command
    > ```
    > swapoff -a
    > ```

- kube-proxy
    ```
    cp ./kubernetes/server/kubernetes/server/bin/kube-proxy /usr/local/bin/
    ./kubernetes/cluster/centos/node/scripts/proxy.sh
    ```