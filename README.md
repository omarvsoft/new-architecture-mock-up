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