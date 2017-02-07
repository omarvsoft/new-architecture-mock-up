#!/bin/bash

#export FLANNEL_ETCD_ENDPOINTS=http://localhost:8080 
#export KUBE_MASTER=http://localhost:8080
#export KUBELET_API_SERVER=http://localhost:8080

#flanneldFile="flanneld"
#kubernetesConfigFile="config"
#kubeletFile="kubelet"


kubernetesConfigFile="/etc/kubernetes/config"
kubeletFile="/etc/kubernetes/kubelet"
flanneldFile="/etc/sysconfig/flanneld"



echo "flanneld path          : "$flanneldFile
echo "kubernetes config path : "$kubernetesConfigFile
echo "kubelet config path    : "$kubeletFile

echo "FLANNEL_ETCD_ENDPOINTS="${FLANNEL_ETCD_ENDPOINTS}
echo "KUBE_MASTER="${KUBE_MASTER}

sed -i -e "s|{FLANNEL_ETCD_ENDPOINTS}|${FLANNEL_ETCD_ENDPOINTS}|g" $flanneldFile

sed -i -e "s|{KUBE_MASTER}|${KUBE_MASTER}|g" $kubernetesConfigFile

sed -i -e "s|{KUBELET_API_SERVER}|${KUBELET_API_SERVER}|g" $kubeletFile


for SERVICES in etcd ntpd lxc.service libvirtd  kube-proxy kubelet docker flanneld; do
    systemctl restart $SERVICES
    systemctl enable $SERVICES
    systemctl status $SERVICES 
done


