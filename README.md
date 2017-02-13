# new-architecture-mock-up

This repository contains example projects for the different proposed components of the new architecture


## authorization-services

* Service that implements oAuth2 tools to genereta, refresh and validate access Tokens.

## authentication-services

* Service that is an oAuth2 client and  Rest API that expose authentication methods. 
* This Service also has a relationship with customer-services, works as a gateway in some times.

## customer-services

* Service that is an oAuth2 client and  Rest API that expose customer methods. 

## client-side

* This Folder contains examples about how the clients may consume Rest APIS

## common-deps

* This Folder contains a pom file with the common dependencies that are used by the example projects.

## config-repository

* This Folder contains the properties files that are used by the config-server.

## config-server

* Service that is responsible for the services configuration management

## redis-3.2.6

* This folder contains the modified shells that are necessary to create and configure a basic redis cluster.


## Docker
<img src="https://www.docker.com/sites/default/files/moby.svg" width="300"/>

> "Docker containers wrap a piece of software in a complete filesystem that contains everything needed to run: code, runtime, system tools, system libraries – anything that can be installed on a server. This guarantees that the software will always run the same, regardless of its environment."<br/>
https://www.docker.com/what-docker

The most of the projects that integrate this repository will be containerized in docker images. 
<br/>The images will be generated through Spotify docker maven plugin https://github.com/spotify/docker-maven-plugin (current version: 0.4.13)

The approach chosen was **"Specify build info in the POM"** instead of an external Dockerfile because currently, the plugin doesn't work appropriately with replacing feature which can give us more flexibility in the Dockerfile to manipulate its content dynamically.
For example:
```docker
FROM ${image.base}
ADD ${project.artifactId}/${project.version}.jar
```
In the future, an update can be considered in order to externalize the configuration.

The current XML configuration describes how the image has to be built.
```xml
<!-- <dockerDirectory>${spotify.docker.directory}</dockerDirectory> -->
<imageName>${docker.image.name}</imageName>
<baseImage>${spotify.docker.baseImage}</baseImage>
<volumes>
	<volume>/tmp</volume>
</volumes>
<runs>
	<run>mv ${project.build.finalName}.jar app.jar</run>
	<run>sh -c 'touch /app.jar'</run>
</runs>
<env>
</env>
<entryPoint>${spotify.docker.entryPoint}</entryPoint>
<forceTags>true</forceTags>
<imageTags>
	<imageTag>${project.version}</imageTag>
	<imageTag>latest</imageTag>
</imageTags>
<resources>
	<resource>
		<directory>${project.build.directory}</directory>
		<include>${project.build.finalName}.jar</include>
	</resource>
</resources>
<!-- Registry using ~/.docker/config.json file for authentication Hint: -->
<!-- The build will fail, if the config file doesn't exist. -->
<!-- <useConfigFile>true</useConfigFile> -->
```

`dockerDirectory` is disabled because the image is built through XML configuration.
<br/>If you want to use a Dockerfile instead, you should uncomment `dockerDirectory`.<br/>

The value would should point to src/main/docker
In that case the following configuration will be ignored:
- baseImage
- volumes
- runs
- env
- entryPoint
- Resources configuration will be included, but you should set the clause ADD in the Dockerfile

In order to push an image into a registry You should uncomment `<useConfigFile>` but in that case, you have to be logged in the registry and your credentials have to be stored in `~/.docker/config.json`
    
>Other configurations like imageName can be modified through overriding in the sub projects the properties inherited from parent pom

You can build a docker image of any subproject through (docker must be configured correctly):
```ssh
mvn clean package docker:build
```
The above instruction will generate a Dockerfile at the path target/docker/Dockerfile
```
FROM frolvlad/alpine-oraclejdk8:slim
RUN mv configserver-mock-up-0.1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
VOLUME /tmp
```
And a docker image with two tags will be created
```
REPOSITORY                                                TAG                 IMAGE ID            CREATED             SIZE
localhost:5000/mi-banco/configserver-mock-up              0.1.0-SNAPSHOT      a9b926686d10        2 days ago          234.9 MB
localhost:5000/mi-banco/configserver-mock-up              latest              a9b926686d10        2 days ago          234.9 MB
```
>Docker Registry<br/>
What it is<br/>
The Registry is a stateless, highly scalable server side application that stores and lets you distribute Docker images. The Registry is open-source, under the permissive Apache license.<br/>
https://docs.docker.com/registry/

It is important to notice that the name of the image has relevance. <br/>
The naming convention is **REGISTRY[:PORT]/USER/REPO[:TAG]** <br/>
The complete names of the image with its two tags are
```
localhost:5000/mi-banco/configserver-mock-up:0.1.0-SNAPSHOT
localhost:5000/mi-banco/configserver-mock-up:latest
```
The registry indicates where the image will be storage or download. <br/><br/>
In that way, if we execute the command **docker push**, docker will try to push the image into a docker registry located at `localhost:5000` 

Subsequently, docker will ask for the user `mi-banco` and finally will push the image `configserver-mock-up` in the registry.

If we want to pull the image, docker will try to download the image according to with the registry configured in the image's name.

>NOTE <br/>
If there isn't a registry in the image's name the registry by default is `docker.io`

#### Kubernetes Considerations
>The default container image pull policy is `IfNotPresent`, which causes the Kubelet to not pull an image if it already exists. If you would like to always force a pull, you must specify a pull image policy of `Always` in your .yaml file (`imagePullPolicy: Always`) or specify a `:latest` tag on your image.<br/><br/>
That is, if you’re specifying an image with other than the `:latest` tag, e.g. `myimage:v1`, and there is an image update to that same tag, the Kubelet won’t pull the updated image. You can address this by ensuring that any updates to an image bump the image tag as well (e.g. `myimage:v2`), and ensuring that your configs point to the correct version. <br/><br/>
**Note**: you should avoid using :latest tag when deploying containers in production, because this makes it hard to track which version of the image is running and hard to roll back. <br/><br>https://kubernetes.io/docs/user-guide/config-best-practices/



## kubernetes <img src="https://kubernetes.io/images/nav_logo.svg" width="300"/>

> Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications.
<br>It groups containers that make up an application into logical units for easy management and discovery. Kubernetes builds upon 15 years of experience of running production workloads at Google, combined with best-of-breed ideas and practices from the community.<br>
https://kubernetes.io

In order to manage micro services at scale, the subprojects containerized as docker images will be orchestrated by kubernetes.

There are several ways to deploy a microservice in kubernetes the first is a series of command line.<br>
The second is through the YAML descriptors that configure each necessary resource.<br>
To deploy a microservice there are five basic resources to configure.
- Pods
- Deployment
- ReplicaSet
- Services
- Labels

### What is a Pod?<br>

A pod (as in a pod of whales or pea pod) is a group of one or more containers (such as Docker containers), the shared storage for those containers, and options about how to run the containers. Pods are always co-located and co-scheduled, and run in a shared context. A pod models an application-specific “logical host” - it contains one or more application containers which are relatively tightly coupled — in a pre-container world, they would have executed on the same physical or virtual machine.<br>
While Kubernetes supports more container runtimes than just Docker, Docker is the most commonly known runtime, and it helps to describe pods in Docker terms.<br>
https://kubernetes.io/docs/user-guide/pods/#what-is-a-pod <br><br>
“Kubernetes orchestrates, schedules, and manages pods. When we refer to an application running inside of Kubernetes, it’s running within a Docker container inside of a pod. A pod is given its own IP address, and all containers within the pod share this IP (which is different from plain Docker, where each container gets an IP address). When volumes are mounted to the pod, they are also shared between the individual Docker containers running in the pod.”<br>
Excerpt From: Christian Posta. “Microservices for Java Developers.” iBooks. 

### What is a Deployment? <br>

A Deployment provides declarative updates for Pods and Replica Sets (the next-generation Replication Controller). You only need to describe the desired state in a Deployment object, and the Deployment controller will change the actual state to the desired state at a controlled rate for you. You can define Deployments to create new resources, or replace existing ones by new ones.<br>
https://kubernetes.io/docs/user-guide/deployments/#what-is-a-deployment

### What is a ReplicaSet? <br>
“Kubernetes has a concept called ReplicationController that manages the number of replicas for a given set of microservices”

Excerpt From: Christian Posta. “Microservices for Java Developers.” iBooks. 

ReplicaSet is the next-generation Replication Controller. The only difference between a ReplicaSet and a Replication Controller right now is the selector support. ReplicaSet supports the new set-based selector requirements as described in the labels user guide whereas a Replication Controller only supports equality-based selector requirements.<br>

A ReplicaSet ensures that a specified number of pod “replicas” are running at any given time. <br>
https://kubernetes.io/docs/user-guide/replicasets/#what-is-a-replicaset

### What is a Service? <br>
Kubernetes Pods are mortal. They are born and when they die, they are not resurrected. ReplicationControllers in particular create and destroy Pods dynamically (e.g. when scaling up or down or when doing rolling updates). While each Pod gets its own IP address, even those IP addresses cannot be relied upon to be stable over time. This leads to a problem: if some set of Pods (let’s call them backends) provides functionality to other Pods (let’s call them frontends) inside the Kubernetes cluster, how do those frontends find out and keep track of which backends are in that set?<br><br>
**Enter Services.**<br><br>
A Kubernetes Service is an abstraction which defines a logical set of Pods and a policy by which to access them - sometimes called a micro-service. The set of Pods targeted by a Service is (usually) determined by a Label Selector <br>
https://kubernetes.io/docs/user-guide/services/

### What is a Label? <br>
Labels are key/value pairs that are attached to objects, such as pods. Labels are intended to be used to specify identifying attributes of objects that are meaningful and relevant to users, but which do not directly imply semantics to the core system. Labels can be used to organize and to select subsets of objects. Labels can be attached to objects at creation time and subsequently added and modified at any time. Each object can have a set of key/value labels defined. Each Key must be unique for a given object.<br>
https://kubernetes.io/docs/user-guide/labels/

```javascript
"labels": {
  "key1" : "value1",
  "key2" : "value2"
}
```

### Creating resources
With the aim of generating the resources that kubernetes requires in a fast way and with dynamic values according to with each project. We use the fabric8 plugin https://maven.fabric8.io current version: 3.2.18.
<img src="https://fabric8.io/images/fabric8_logo.svg" width="300"/>

Prerequisites:
- Connection to a docker daemon
- Connection to a cluster kubernetes/openshift

Although  this plugin has the feature of build the docker image (through `mvn fabric8:build`) this is not its purpose at this moment.

We are building the docker image through `docker-maven-plugin` provided by spotify.

The image configuration in the fabric8 plugin has the intention of build correctly kubernetes.yaml/openshift.yaml descriptors and not to build the docker image.

We are using the Resource Fragments approach to configuring the plugin.

At this moment the XML configuration doesn't work (https://github.com/fabric8io/fabric8-maven-plugin/issues/813). 

An update can be done in the future to change the approach.

The main characteristics of Resource Fragments approach are:
- You have to have a folder named fabric8 under src/main Where the k8s/o7t descriptors will be taken automatically
- The descriptors aren't entire descriptors, actually are fragments.
- These fragments need to have the naming convention: 
	* name-project-service.yml 
	* name-project-deployment.yml

Where the "name-project" will be the name of the resource and service and deployment will be the kind of resources

In this example we will have two resources: a service called name-project and a deployment called name-project

#### About the deployment fragment
At this time we have the next configuration:

```YAML 
spec:
  replicas: 1
  template:
    spec:
      containers:
      - env:
        livenessProbe:
          httpGet:
              path: /health
              port: 8080
              scheme: HTTP
          initialDelaySeconds: 180
        readinessProbe:
          httpGet:
              path: /health
              port: 8080
              scheme: HTTP
          initialDelaySeconds: 30
  ```
  
With this configuration we will have a single pod, also, we configured a readinessProbe to know when a pod is ready to start accepting traffic and a livenessProbe is configured too. 

The ports must match with the spring boot configuration (application.properties property server.port)

The rest of the descriptor will be automatically added.

#### About the service fragment
At this time we have the next configuration:<br>
```
spec:
	type: LoadBalancer
	ports:
		- port: 80
		targetPort: 8080
```
This configuration is important. 

The port indicates the port of the service. 

In that way you will access the service through that port
The targetPort is the port where the tomcat is running. 

In that way it must match with the spring boot configuration (application.properties property server.port)

The type is the type of the service 
Not always LoadBalancer is necessary. 

Visit https://kubernetes.io/docs/user-guide/services


The property imagePullPolicy is configured as `IfNotPresent` to avoid download the image from the registry each time a pod is created.

In the same way, the image deployed in kubernetes contains the version as a tag in order to avoid the same situation.

If you want to change this behavior, You can override the property: `<fabric8.maven.conf.imagePullPolicy/>` in the child pom

Visit: https://kubernetes.io/docs/user-guide/config-best-practices/




The main commands of this plugin are:
- mvn fabric8:build which build the docker images (it is not its purpose at this moment. You can build the image through mvn docker:build)
- mvn fabric8:resource which will generate the k8s/o7t descriptors in the path ${project}/target/classes/META-INF/fabric8
- mvn fabric8:deploy this will deploy the resources in ks8/o7t

After executed the deploy command you will have in kubernetes: pods running, a replicationSet and a service exposed.
  
In order to execute an entire cycle you should execute:
```
mvn clean package docker:build fabric8:resource fabric8:deploy
```
  
Or you can use the plugin `exec-maven-plugin` which has incorporated all these tasks
```
mvn -Pk8s exec:exec
```
  
Another interesting commands are (visit https://maven.fabric8.io to see the entire list of commands):
```
fabric8:undeploy
fabric8:debug
```
  
In order to follow the best practices in kubernetes this project has configured some labels which will be added automatically.

Is recommended that each child project override this labels according their specifications.  

`<label.all.tier/>`

`<label.all.phase/>`

`<label.all.product/>`

>Additionally more labels can be configured in the fragments files yml. Consider that labels and selector must be the same