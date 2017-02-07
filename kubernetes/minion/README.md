# Kubernetes-Minion With Flannel

## Usage

1.- Build Dockerfile, may use `build.sh` or docker shell.

2.- To run minion and then connect with master, we have to define three environment variables.

* `FLANNEL_ETCD_ENDPOINTS` - String that specify the host(s) that have a etcd service that we'll use for the minions resources management.

* `KUBE_MASTER` -  Define the master host and service port.

* `KUBELET_API_SERVER` -  Define the master host and service port.

3.- Run Docker image as container. The image starts with systemd then it will can initialize services.

docker run  \ <br />
-e FLANNEL_ETCD_ENDPOINTS=http://172.17.0.4:2379 \ <br />
-e KUBE_MASTER=http://172.17.0.4:8080 \ <br />
-e KUBELET_API_SERVER=http://172.17.0.4:8080 \ <br />
-it \ <br />
--privileged \ <br />
-d \ <br />
--restart always  \ <br />
--name kubernetes_centos_minion \ <br />
kubernetes_centos_minion <br />

4.- Mount `CGROUP` Volumes.

docker exec -ti kubernetes_centos_minion /bin/bash /usr/bin/cgroupfs-mount.sh

5.- Set Environment and start services.

docker exec -ti kubernetes_centos_minion /bin/bash /usr/bin/setEnv.sh

## Flannel

Flannel is a very simple overlay network that satisfies the Kubernetes requirements. Many people have reported success with Flannel and Kubernetes.

https://github.com/coreos/flannel#flannel

![alt tag](https://github.com/coreos/flannel/blob/master/packet-01.png)

## Etcd

Etcd is a highly-available key value store which Kubernetes uses for persistent storage of all of its REST API objects.

https://coreos.com/etcd/docs/latest/


