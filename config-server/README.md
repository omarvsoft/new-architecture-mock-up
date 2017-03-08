# Spring Cloud Config

>Spring Cloud Config provides server and client-side support for externalized configuration in a distributed system. <br/>With the Config Server you have a central place to manage external properties for applications across all environments. <br/>The concepts on both client and server map identically to the Spring Environment and PropertySource abstractions, so they fit very well with Spring applications, but can be used with any application running in any language. <br/>As an application moves through the deployment pipeline from dev to test and into production you can manage the configuration between those environments and be certain that applications have everything they need to run when they migrate. <br/>The default implementation of the server storage backend uses git so it easily supports labelled versions of configuration environments, as well as being accessible to a wide range of tooling for managing the content. <br/>It is easy to add alternative implementations and plug them in with Spring configuration.<br/>
https://cloud.spring.io/spring-cloud-config/



## Start the server:

The server is a Spring Boot application so you can run it from your Console or IDE instead if you prefer (the main class is **com.evertec.cibp.configserver.Application**).

It is important to notice that the configuration server needs some environment variables (can be more or less in the future. Each repository configured needs its own environment variables). 

* Basic autentication
  * SECRET_CONFIG_SERVER_USER
  * SECRET_CONFIG_SERVER_PASSWORD
* Encrypt key-store (It permits the encryption and decryption of sensitive information in the Git repository.)
  * SECRET_KEY_STORE_LOCATION (the path where the Java Key Store is located) 
  * SECRET_KEY_STORE_PASSWORD (the password of the key store)
  * SECRET_KEY_STORE_ALIAS (the alias of the key store)
  * SECRET_KEY_STORE_SECRET (the secret of the key store)
* Server GIT
  * SECRET_CONFIG_GIT_DEFAULT_REPO (It is required to have one default repository)
  * SECRET_CONFIG_GIT_DEFAULT_USERNAME
  * SECRET_CONFIG_GIT_DEFAULT_PASSWORD

**Run in console:**
```SHELL
java -jar -DCONFIG_SERVER_USER=myuser \
-DCONFIG_SERVER_PASSWORD=mypassword \
-DKEY_STORE_LOCATION=file:///keystore/configServer.jks \
-DKEY_STORE_PASSWORD=letmein \
-DKEY_STORE_ALIAS=configServKey \
-DKEY_STORE_SECRET=DevConfigServPass \
-DGIT_DEFAULT_REPO_URI=https://github.com/cesargomezvela/config-repo-default.git \
-DGIT_DEFAULT_REPO_USERNAME=muck-up \
-DGIT_DEFAULT_REPO_PASSWORD=co-qui2017 \
-Dnewrelic.config.file=/Users/et41451/Development/code/sts-workspace_maqueta/new-architecture-mock-up/config-server/src/main/newrelic/example_newrelic.yml \
-Dnewrelic.environment=development \
-Dnewrelic.logfile=/Users/et41451/Development/code/sts-workspace_maqueta/new-architecture-mock-up/config-server/src/main/newrelic/logs/newrelic_config_agent.log \
-javaagent:/Users/et41451/Development/code/sts-workspace_maqueta/new-architecture-mock-up/config-server/src/main/newrelic/newrelic-java-3.27.0/newrelic.jar \
configserver-mock-up-0.1.0-SNAPSHOT.jar
```

**IDE configuration**

![IDEnvironmet](src/main/doc/images/IDEnvironmet.png)

> The SECRET prefix is important because that reflect that the variable will be provided by the Kubernetes SECRETS mechanism. <br> Avoid replacing these values for text plain in the application.yml instead of that, you should set your environment as shown above



Independently if you resources are properties or YAML files the HTTP service has resources in the form:
```
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```

where the "application" is injected as the spring.config.name in the SpringApplication (i.e. what is normally "application" in a regular Spring Boot app), "profile" is an active profile (or comma-separated list of properties), and "label" is an optional git label (defaults to "master".)

Spring Cloud Config Server pulls configuration for remote clients from a git repository (which must be provided):
```YAML
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
```


![NewRelic](src/main/doc/images/NewRelic.png)

New Relic has been chosen as Microservices monitoring tool. This section describe how to start up New Relic in the configserver.



* **pom.xml**
	
    This dependency will download New Relic as a zip file which contains all the necessary libraries to work   
    ```xml 
    <!-- The newrelic dependency. -->
	<dependency>
		<groupId>com.newrelic.agent.java</groupId>
		<artifactId>newrelic-java</artifactId>
		<version>${newrelic.version}</version>
		<scope>provided</scope>
		<type>zip</type>
	</dependency>
     ```
    The next plugin will unzips the New Relic zip file in `target/newrelic-unzip/newrelic`. This directory will be delivered to the docker image and its newrelic.yml will be replaced with a customized newrelic.yml file
    ```xml 
    <!-- Maven-dependency::plugin[] -->
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-dependency-plugin</artifactId>
		<version>2.6</version>
		<executions>
			<execution>
				<id>unpack-zip</id>
				<phase>prepare-package</phase>
				<goals>
					<goal>unpack-dependencies</goal>
				</goals>
				<configuration>
					<artifactItems>
						<artifactItem>
							<groupId>com.newrelic.agent.java</groupId>
							<artifactId>newrelic-java</artifactId>
							<version>${newrelic.version}</version>
							<type>zip</type>
							<overWrite>true</overWrite>
							<outputDirectory>target</outputDirectory>
							<destFileName>newrelic</destFileName>
						</artifactItem>
					</artifactItems>
					<outputDirectory>target/newrelic-unzip</outputDirectory>
				</configuration>
			</execution>
		</executions>
	</plugin>
     ```
    The above configuration has been added to the parent pom in order that others micro services can take advantage of this configuration.
    
    
* **Dockerfile**
	
    The Dockerfile is generated by docker maven plugin. The configuration corresponding to New Relic is the is inside it.
    
    The next section will incorporate the `target/newrelic-unzip/newrelic` directory inside the docker image in the path `/newrelic` 
    ```xml 
    <resource>
		<targetPath>/newrelic</targetPath>
		<directory>${project.build.directory}/newrelic-unzip/newrelic</directory>
	</resource>
    ```
    
    The next section will replace the default newrelic.yml with the custom newrelic.yml
    ```xml 
    <resource>
		<directory>src/main/newrelic</directory>
		<targetPath>/newrelic</targetPath>
		<include>newrelic.yml</include>
	</resource>
    ```
    
    The next configuration establishes how the container must be run.
    ```xml 
    <spotify.docker.entryPoint>
    [ "sh", "-c", "java $JAVA_OPTS -Dnewrelic.environment=$PROFILE_ACTIVE -Dnewrelic.config.license_key=$NEW_RELIC_LICENSE -javaagent:/newrelic/newrelic.jar -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
    </spotify.docker.entryPoint>
    ```
    
    * `-Dnewrelic.environment=$PROFILE_ACTIVE` tells to New Relic which environment is running. The `$PROFILE_ACTIVE` is an environment variable that kubernetes will set before the container runs.
    
    * `-Dnewrelic.config.license_key=$NEW_RELIC_LICENSE`  tells to New Relic what is the license key that will be used. The `$NEW_RELIC_LICENSE` is an environment variable that kubernetes will set before the container runs.
    
    * Finally `-javaagent:/newrelic/newrelic.jar` indicate where is located the newrelic.jar
    
    The entire docker maven plugin configuration is
    
   ```xml 
    <!-- Spotify::plugin[] -->
	<plugin>
		<groupId>com.spotify</groupId>
		<artifactId>docker-maven-plugin</artifactId>
		<version>${spotify.plugin.version}</version>
		<configuration>
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
				<resource>
					<targetPath>/newrelic</targetPath>
					<directory>${project.build.directory}/newrelic-unzip/newrelic</directory>
				</resource>
				<resource>
					<directory>src/main/newrelic</directory>
					<targetPath>/newrelic</targetPath>
					<include>newrelic.yml</include>
				</resource>
			</resources>
		</configuration>
	</plugin>
   ```
   
   
   
* **Kubernetes**
	
    * In order to run the docker images in any environment the New Relic key license is set as an environment variable and because this is a sensitive information it is exposed as a secret in kubernetes.

	  This resource needs to be executed only once in the cluster to set the license of the New Relic. Once the license is placed in 	kubernetes every pod can consume this as an environment variable. It is important to mention that the value require Base64 encryption. If you want to know more about the secret, you need to refer to kubernetes documentation.
    
      newrelic-license-key-secrets.yml

	  ```YAML
	  apiVersion: v1
      kind: Secret
      metadata:
        name: newrelic-license-key-secrets
      type: Opaque
      data:
        license: JzFiMWY1YTk5MGJmODdiMmU0OGI3Y2U2MGMwNDA2YzYwYWJjNGI1MTQn
	  ```

	  On the side of the configserver kubernetes configuration, it is necessary to relate the secret generated above with the environment variable `$NEW_RELIC_LICENSE` the next is a fragment of the file `src/main/fabric8/configserver-deployment.yml` that accomplish this job.
    
      ```YAML
	  - name: NEW_RELIC_LICENSE
        valueFrom:
          secretKeyRef: 
            key: license
            name: newrelic-license-key-secrets
	  ```
   * The environment where New Relic will report is set as an environment variable too, but as this information is not sensitive it is exposed as a configmap in kubernetes

     This resource needs to be executed only once in the cluster to set the environment where New Relic will report. Once it is placed in kubernetes every pod can consume it as an environment variable.
     
     profile-active-configmap.yml
     
     ```YAML
	 kind: ConfigMap
     apiVersion: v1
     metadata: 
       name: profile-active-configmap
     data:
       profile: development
	  ```
      On the side of the configserver kubernetes configuration, it is necessary to relate the configmap generated above with the environment variable `$PROFILE_ACTIVE` the next is a fragment of the file `src/main/fabric8/configserver-deployment.yml` that accomplish this job.
      
      ```YAML
	  - name: PROFILE_ACTIVE
        valueFrom:
          configMapKeyRef: 
            key: profile
            name: profile-active-configmap
	  ```
	
  As you can notice the kuebernetes configuration is related to the docker configuration when a pod is built, kubenetes sets the  variables **NEW_RELIC_LICENSE** and **PROFILE_ACTIVE** in the environment, their values are taken from the SECRETS and CONFIGMAP configured previously, then docker starts the container through the command line 
  
  ```
  java $JAVA_OPTS  \
  -Dnewrelic.environment=$PROFILE_ACTIVE  \
  -Dnewrelic.config.license_key=$NEW_RELIC_LICENSE  \
  -javaagent:/newrelic/newrelic.jar  \
  -Djava.security.egd=file:/dev/./urandom  \
  -jar /app.jar
  ```
* **src/main/newrelic/newrelic.yml** 
	
     The New Relic Java agent reads its configuration from the `newrelic.yml` file. By default, the agent looks for this file in the directory that contains newrelic.jar. New Relic has a default `newrelic.yml`, the present `newrelic.yml` is a custom file configured especially to the configserver. In the maven package phase the custom file will replace the default newrelic.yml

	The parameter `#license_key: '1b1f5a9933fsd8b7ce60c0406c73626hsy'` has been commented because this parameter will be set as an environment variable (see the docker section and kubernetes section)
    
    The file has been customized to support different environments such as development, QA, production and PILOT. 
    We can Use `-Dnewrelic.environment=<environment>`  on the Java startup command line to set the environment. This task is accomplished through docker and kubernetes configuration (see above).
    
   It has been added two app names CONFIGURATION-SERVER and CIBP-NEW-ARCH. This configuration will report in two sections in the New Relic console one specially designed for the configserver application and the other is where all the CIBP microservices will report.
    
  ![NewRelic_namespaces](src/main/doc/images/NewRelic_namespaces.png)


With the above configuration, New Relic will be able to report statistics when the microservice be deployed in docker or kubernetes and won't be able in an IDE development environment.

If you need to configure your IDE to report in New Relic, you need to:
* Use the **target/newrelic-unzip/newrelic** directory (or your own New Relic distribution)
* Replace the newrelic.yml with your own newrelic.yml (with a valid license key)
* Optionally, you indicate where is the newrelic.yml through the parameter newrelic.config.file
* Finally, set the **-javaagent** and the **newrelic.environment** parameters in the IDE.

![NewRelic_IDEEnv](src/main/doc/images/NewRelic_IDEEnv.png)