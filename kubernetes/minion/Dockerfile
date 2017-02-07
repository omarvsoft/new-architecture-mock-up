FROM centos:7
MAINTAINER Erick Vargas "erick.vargas@evertecinc.com"

# Flanneld environment file
ADD dist/flanneld flanneld 
# Kubernetes config file
ADD dist/config config 
# Kubelet config file
ADD dist/kubelet kubelet
# Shell that configure SO services that can be monitored
ADD dist/setEnv.sh /usr/bin/setEnv.sh
# Shell that mount cgroup volumes for Flannel and Kubernetes to store pods.
ADD dist/cgroupfs-mount.sh /usr/bin/cgroupfs-mount.sh

# Environment variables
ENV container=docker

# Install necessary packages
RUN yum -y update && yum -y install kernel-headers \
    etc \
    ntp \
    golang \
    gcc \
    flannel \ 
    kubernetes \
    apt-transport-https \
        ca-certificates \
        curl \
        lxc \
        vim \
        iptables \
        docker-io && yum clean all

# Override environment and configuration files

RUN mv flanneld /etc/sysconfig/flanneld

RUN mv config /etc/kubernetes/config

RUN mv kubelet /etc/kubernetes/kubelet

#Assigning execute permissions for shells

RUN chmod +x /usr/bin/setEnv.sh

RUN chmod +x /usr/bin/cgroupfs-mount.sh

# Define volumes
# Indispensable to mount cgroup volumes
VOLUME ["/sys/fs/cgroup","/sys/fs/cgroup"]
# Indispensable to mount cgroup volumes
VOLUME ["/var/run/docker.sock","/var/run/docker.sock"]

# This image shall start whit systemd
# IMPORTANT: Do not overwrite the entrypoint when run this image
CMD ["/usr/sbin/init"]