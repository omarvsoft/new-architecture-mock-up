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
https://www.docker.com
>"Docker containers wrap a piece of software in a complete filesystem that contains everything needed to run: code, runtime, system tools, system libraries – anything that can be installed on a server. This guarantees that the software will always run the same, regardless of its environment."

The most of the projects that integrate this repository will be containerized in docker images. The images will be generated through spotify docker maven plugin https://github.com/spotify/docker-maven-plugin (current version: 0.4.13)

The approach choosen was "Specify build info in the POM" instead of a external Dockerfile because currently the plugin doesn't work appropriately with replace feature which can give us more flexibility in the Dockerfile to manipulate its content dinamically.
For example:
```sh
FROM ${image.base}
ADD ${project.artifactId}/${project.version}.jar
```
In the future an update can be considered in order to externalice the configuration.

The current POM configuration:
```sh
<!-- <dockerDirectory>${docker.spotify.plugin.dockerDirectory}</dockerDirectory> -->
<imageName>${docker.image.name}</imageName>
<baseImage>frolvlad/alpine-oraclejdk8:slim</baseImage>
<volumes>
	<volume>/tmp</volume>
</volumes>
<runs>
	<run>mv ${project.build.finalName}.jar app.jar</run>
	<run>sh -c 'touch /app.jar'</run>
</runs>
<env>
</env>
<entryPoint>[ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]</entryPoint>
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
<useConfigFile>true</useConfigFile>
```
