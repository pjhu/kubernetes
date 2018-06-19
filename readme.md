## single
setup single kubernetes

## cluster
https://github.com/pjhu/kubespray

### setup vagrant
    https://github.com/pjhu/kubernetes/blob/master/single/readme.md
    
### resolve problem

-  ansible
    https://github.com/kubernetes-incubator/kubespray/issues/2791
    ```
    brew remove ansible
    # ansible-playbook may have been overridden by homebrew, so reinstall through pip
    pip uninstall ansible
    pip install ansible
    ```

-  sshpass
    https://github.com/ansible-tw/AMA/issues/21
    ```
    brew create https://sourceforge.net/projects/sshpass/files/sshpass/1.06/sshpass-1.06.tar.gz --force
    brew install sshpass
    ```
 ### deploy
 - vagrant
    ```
    vagrant up
    ```
- login master
    ```
    vagrant ssh k8s-01
    kubectl get nodes
    ```