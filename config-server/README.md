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


## Cryptography 

> **Prerequisites:** to use the encryption and decryption features a Java certificate has to be created and installed in kubernetes. Additionally, the kubernetes SECRETS for location, password, alias and secret have to be created too.

The main advantage of this set up is that the property values don’t have to be in plain text when they are "at rest" (e.g. in a git repository). The values that start with `{cipher}` will be decrypted before sending to clients over HTTP. If a value cannot be decrypted it is removed from the property source and an additional property is added with the same key, but prefixed with "invalid." and a value that means "not applicable" (usually "<n/a>"). This is largely to prevent cipher text being used as a password and accidentally leaking.


If you are setting up a remote config repository for config client applications it might contain an application.yml like this, for instance:


*application.yml*
```YAML
spring:
  datasource:
    username: dbuser
    password: '{cipher}FKSAJDFGYOS8F7GLHAKERGFHLSAJ'
```

Encrypted values in a .properties file must not be wrapped in quotes, otherwise the value will not be decrypted:


*application.properties*
```properties
spring.datasource.username: dbuser
spring.datasource.password: {cipher}FKSAJDFGYOS8F7GLHAKERGFHLSAJ
```
You can safely push this plain text to a shared git repository and the secret password is protected.

The server also exposes `/encrypt` and `/decrypt` endpoints (on the assumption that these will be secured and only accessed by authorized agents). If you are editing a remote config file you can use the Config Server to encrypt values by POSTing to the `/encrypt` endpoint, e.g.

```shell
curl http://myuser:mypassword@172.17.0.3:8888/encrypt -d valueToEncrypt
```

> **NOTE:** If the value you are encrypting has characters in it that need to be URL encoded you should use the `--data-urlencode` option to curl to make sure they are encoded properly.

The inverse operation is also available via `/decrypt` (provided the server is configured with a symmetric key or a full key pair):

```shell
curl http://myuser:mypassword@172.17.0.3:8888/decrypt -d AQAS1z38vdbwETz6YQVEkbYriSLI6Nu+duBqSwjoSHMU/qAVctEqvU9xmn8TbaBUed1Q+0o4JqONUi5Q+cKLvDzA/ItIUbyE6Sg3274ByKGjIvdr6Rb5VHDcGGyFMZbHlCVC/wi1YweI8lqT/IDYXoex/D7H0D++hGNFE7edVYppEVvn1N0c1Smxh0IXN6CjGFd5yMGSYg1d41AVBD9ucN8k3jlPzYjIV6xGzI70W54XuMmFwBkr/kR8SHmu4HnvHEqDFOnVVQLHr8K6YIl/i4uVsQGWNTMMagWVtVANoujnW2gxtK5aTFIT2pbDeGBh5atu+j0TaFi9gN7mo8sp5lfBVNbCkLvL0ZMB7KwZipOIe6WmdvBe474WkVjE2WXG++E=
```

> **TIP:** If you are testing like this with curl, then use --data-urlencode (instead of -d) or set an explicit Content-Type: text/plain to make sure curl encodes the data correctly when there are special characters ('+' is particularly tricky).

Take the encrypted value and add the `{cipher}` prefix before you put it in the YAML or properties file, and before you commit and push it to a remote repository.
<br><br>


### Key Management

The Config Server use an asymmetric RSA key pair.

To configure an asymmetric key you can create a keystore (e.g. as created by the keytool utility that comes with the JDK). The keystore properties are configured in the `application.yml` under encrypt.keyStore.* with the values:

* `location` (a Resource location),
* `password` (to unlock the keystore) and
* `alias` (to identify which key in the store is to be used).
* `secret` (key password)

### Creating a Key Store for Testing

To create a keystore for testing you can do something like this:

```shell
keytool -genkeypair -alias configServAlias -keyalg RSA \
  -dname "CN=Configuration Server,OU=MiBanco,O=EVERTEC,L=San Juan,S=Puerto Rico,C=PR" \
  -keypass DevConfigServSecret -keystore configServer.jks -storepass letmeinPassword  \
  -validity 3650
```
> **NOTICE:** Each certificate is valid only for a limited amount of time. `validity` describe how long the certificate is valid in terms of days

The `configServer.jks` needs to be exposed as a SECRET in kubernetes through the command:
```shell
kubectl create secret generic config-server-keystore --from-file=/Users/et41451/Documents/keystore/configServer.jks
```

The `application.yml` in the configserver
```YAML
encrypt:
  key-store: 
    location: ${KEY_STORE_LOCATION}
    password: ${KEY_STORE_PASSWORD}
    alias: ${KEY_STORE_ALIAS}
    secret: ${KEY_STORE_SECRET}
```

As you can notice the values will be taken from environment variables in order to can run the application in any environment. Actually, these variables will be set as SECRETS inside kubernetes.


### Kubernetes configuration

On the side of kubernetes also it is necessary to create the SECRETS and map them with the `KEY_STORE_LOCATION`, `KEY_STORE_PASSWORD`, `KEY_STORE_ALIAS` and `KEY_STORE_SECRET` environment variables. Also, you have to indicate where will be mounting the keystore.

*config-server-cryptography-secrets.yml*

```YAML
apiVersion: v1
kind: Secret
metadata:
  name: config-server-cryptography-secrets
type: Opaque
data: 
  location: ZmlsZTovLy9rZXlzdG9yZS9jb25maWdTZXJ2ZXIuamtz
  alias: Y29uZmlnU2VydktleQ==
  password: bGV0bWVpbg==
  secret: RGV2Q29uZmlnU2VydlBhc3M=
```

`configserver-deployment.yml` variables - secrets mapping

```YAML
- name: KEY_STORE_LOCATION
  valueFrom:
    secretKeyRef: 
      key: location
      name: config-server-cryptography-secrets
- name: KEY_STORE_PASSWORD
  valueFrom:
    secretKeyRef: 
      key: password
      name: config-server-cryptography-secrets
- name: KEY_STORE_ALIAS
  valueFrom:
    secretKeyRef: 
      key: alias
      name: config-server-cryptography-secrets
- name: KEY_STORE_SECRET
  valueFrom:
    secretKeyRef: 
      key: secret
      name: config-server-cryptography-secrets
```
`configserver-deployment.yml` mounting the keystore
```YAML
  volumeMounts:
  - mountPath: /keystore
    name: java-keystore
    readOnly: true
volumes:
- name: java-keystore
  secret:
    secretName: config-server-keystore
```
> **NOTICE:** The `config-server-keystore` is the secret of the `configServer.jks` created before


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
    The next plugin will unzips the New Relic zip file in `target/newrelic`. This directory will be delivered to the docker image and its newrelic.yml will be replaced with a customized newrelic.yml file
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
					<goal>unpack</goal>
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
				</configuration>
			</execution>
		</executions>
	</plugin>
     ```
    The above configuration has been added to the parent pom in order that others micro services can take advantage of this configuration.
    
    
* **Dockerfile**
	
    The Dockerfile is generated by docker maven plugin. The configuration corresponding to New Relic is the is inside it.
    
    The next section will incorporate the `target/newrelic` directory inside the docker image in the path `/newrelic` 
    ```xml 
    <resource>
		<targetPath>/newrelic</targetPath>
		<directory>${project.build.directory}/newrelic</directory>
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
					<directory>${project.build.directory}/newrelic</directory>
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
  
  
* Log file
  
  New Relic's Java agent stores log files in the `logs` directory. The Java agent creates this directory in the same location used for the newrelic.jar 

With the above configuration, New Relic will be able to report statistics when the microservice be deployed in docker or kubernetes and won't be able in an IDE development environment.

If you need to configure your IDE to report in New Relic, you need to:
* Use the `target/newrelic` directory (or your own New Relic distribution)
* Replace the `newrelic.yml` with your own `newrelic.yml` (with a valid license key)
* Optionally, you can indicate where is the `newrelic.yml` through the parameter `newrelic.config.file`
* Finally, set the `-javaagent` and the `newrelic.environment` parameters in the IDE.

![NewRelic_IDEEnv](src/main/doc/images/NewRelic_IDEEnv.png)













## Configure a new repository 

In order to configure a new repository for a new microservice within the configuration server you should.

* Modify the `bootstrap.yml` and set under `spring.cloud.config.server.git.repos` the new configuration
  ```YAML
  spring: 
    cloud: 
      config: 
        server: 
          git: 
            repos: 
              authorization-server:
                clone-on-start: true
                uri: ${GIT_AUTHSERVER_REPO_URI}
                username: ${GIT_AUTHSERVER_REPO_USERNAME}
                password: ${GIT_AUTHSERVER_REPO_PASSWORD}
                search-paths:
                - /*
  ```
  > **NOTICE** that the repository name (`authorization-server` in this case) must match with the `spring.application.name` on the client side

* As you can notice the values will be set through environment variables, You must relate these variables  with the kubernetes deployment descriptor (`src/main/fabric8/configserver-deployment.yml`)
  ```YAML
  - name: GIT_AUTHSERVER_REPO_URI
    valueFrom:
      secretKeyRef:
        key: authorization-server-uri
        name: git-configserver-repo-secrets
  - name: GIT_AUTHSERVER_REPO_USERNAME
    valueFrom:
      secretKeyRef:
        key: authorization-server-username
        name: git-configserver-repo-secrets
  - name: GIT_AUTHSERVER_REPO_PASSWORD
    valueFrom:
      secretKeyRef:
        key: authorization-server-password
        name: git-configserver-repo-secrets
  ```

  >  The secret `git-configserver-repo-secrets` and its keys must exist in kubernetes

# ConfigServer Client

> **Prerequisites:** The configserver is configured in such a way that each microservice has its own configuration repository, so before you can configure a client its repository must have been configured first in the configserver

A Spring Boot application can take immediate advantage of the Spring Config Server (or other external property sources provided by the application developer), and it will also pick up some additional useful features related to Environment change events.

### POM dependency

Set the following dependency to your pom.

```XML
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

>Also may be necessary set the spring cloud dependencyManagement but if you inherited from new-architecture parent pom it is not necessary

### Configure Bootstrap

All client apps that want to consume the Config Server need a `bootstrap.yml` in `src/main/resources` directory (or an environment variable) with the server address in spring.cloud.config.uri (defaults to "http://localhost:8888").

*src/main/resources/bootstrap.yml*
```YAML
spring: 
  cloud:
    config:
      uri: ${CONFIG_SERVER_URI}
```

`${CONFIG_SERVER_URI}` indicate that this value will be taken from the environment variable `CONFIG_SERVER_URI` in your IDE you can provide this by adding a new environment variable 

![AuthoEnvars](src/main/doc/images/AuthoEnvars.png)

For kubernetes you have to configure the environment variable  in `src/main/fabric8/authorization-server-deployment.yml`

```YAML
- name: CONFIG_SERVER_URI
  value: http://configserver
```

Where the `http://configserver` is the service name of the configserver. (you must ask for the correct name)


### Config Client Fail Fast
In some cases, it may be desirable to fail startup of a service if it cannot connect to the Config Server. If this is the desired behavior, set the bootstrap configuration property `spring.cloud.config.failFast=true` and the client will halt with an Exception.

This is the case for most microservices.

*src/main/resources/bootstrap.yml*
```YAML
spring: 
  cloud:
    config:
      uri: ${CONFIG_SERVER_URI}
      fail-fast: true
```


### Config Client Retry

If you expect that the config server may occasionally be unavailable when your app starts, you can ask it to keep trying after a failure. First you need to set spring.cloud.config.failFast=true, and then you need to add spring-retry and spring-boot-starter-aop to your classpath. The default behaviour is to retry 6 times with an initial backoff interval of 1000ms and an exponential multiplier of 1.1 for subsequent backoffs. You can configure these properties (and others) using spring.cloud.config.retry.* configuration properties.

>TIP: 
To take full control of the retry add a @Bean of type RetryOperationsInterceptor with id "configServerRetryInterceptor". Spring Retry has a RetryInterceptorBuilder that makes it easy to create one.



### Locating Remote Configuration Resources

The Config Service serves property sources from `/{name}/{profile}/{label}`, where the default bindings in the client app are

* "name" = ${spring.application.name}
  * e.g. If your  `spring.application.name=authorization-server`, then your configuration file must be  named `authorization-server.yml` or `authorization-server.properties`. 

* "profile" = ${spring.profiles.active} (actually Environment.getActiveProfiles())
  * If your configuration files are based on the environment, you should include the profile in the file name e.g. `authorization-server-development.yml`, `authorization-server-QA.yml`, `authorization-server-production.yml`, etc. If you do not provide a profile, then the same file will be served for all profiles

* "label" = ${spring.cloud.config.label} ("master" by default if this property is not present) 

All of them (name, profile and label) can be overridden by setting spring.cloud.config.* (where * is "name", "profile" or "label"). The "label" is useful for rolling back to previous versions of configuration; with the default Config Server implementation it can be a git label, branch name or commit id. Label can also be provided as a comma-separated list, in which case the items in the list are tried on-by-one until one succeeds. This can be useful when working on a feature branch, for instance, when you might want to align the config label with your branch, but make it optional (e.g. spring.cloud.config.label=myfeature,develop).

```YAML
spring: 
  profiles:
    active: ${PROFILE_ACTIVE}
  application:
    name: authorization-server
  cloud:
    config:
      uri: ${CONFIG_SERVER_URI}
      profile: ${PROFILE_ACTIVE}
      label: ${CONFIG_SERVER_LABEL:master}
      fail-fast: true
```

For kubernetes you must provide the environment varibles in `src/main/fabric8/authorization-server-deployment.yml`

```YAML
- name: PROFILE_ACTIVE
  valueFrom:
    configMapKeyRef: 
      key: profile
      name: profile-active-configmap
```

As you can notice the variable will be taken from the configmap `profile-active-configmap`, thus that the configmap must be configured before


### Security
The configuration server uses HTTP Basic security then clients just need to add the environment variables for password and username You can do that via the config server URI, or via separate username and password properties, e.g.

bootstrap.yml
```YAML
spring:
  cloud:
    config:
     uri: http://${CONFIG_SERVER_USER}:${CONFIG_SERVER_PASSWORD}@${CONFIG_SERVER_URI}
```

OR

```YAML
spring: 
  cloud:
    config:
      uri: ${CONFIG_SERVER_URI}
      username: ${CONFIG_SERVER_USER}
      password: ${CONFIG_SERVER_PASSWORD}
```

For kubernetes you must provide the environment varibles in `src/main/fabric8/authorization-server-deployment.yml`

```YAML
- name: CONFIG_SERVER_USER
  valueFrom:
    secretKeyRef: 
      key: username
      name: config-server-secrets
- name: CONFIG_SERVER_PASSWORD
  valueFrom:
    secretKeyRef: 
      key: password
      name: config-server-secrets
```

As you can notice the variables will be taken from the secret `config-server-secrets`, thus that the secret must be configured before

The `spring.cloud.config.password` and `spring.cloud.config.username` values override anything that is provided in the URI.

### Health Indicator
The Config Client supplies a Spring Boot Health Indicator that attempts to load configuration from Config Server. The health indicator can be disabled by setting health.config.enabled=false. The response is also cached for performance reasons. The default cache time to live is 5 minutes. To change that value set the health.config.time-to-live property (in milliseconds).

### Spring Cloud Bus for propagating configuration changes
<br>

![CloudBusDiagram](src/main/doc/images/CloudBusDiagram.png)


The Spring Cloud Bus provides a mechanism to refresh configurations across multiple instances without knowing how many instances there are, or their locations. 

This is particularly handy when there are many service instances of a microservice running or when there are many microservices of different types running. This is done by connecting all service instances through a single message broker. 

Each instance subscribes for change events, and refreshes its local configuration when required. This refresh is triggered by making a call to any one instance by hitting the `/bus/refresh` endpoint, which then propagates the changes through the cloud bus and the common message broker. 

* You have to add a new Cloud Bus dependency: 

  ```XML
  <dependency>
  	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bus-amqp</artifactId>
  </dependency>
  ```

* The microservice also needs connectivity to RabbitMQ, you must configure this in its configuration file (located in the repository) as environment variables because kubernetes will provide these values after.

  ```YAML
  spring:
     rabbitmq:
      host: ${CONFIG_RABBITMQ_HOST}
      port: ${CONFIG_RABBITMQ_PORT}
      username: ${CONFIG_RABBITMQ_USER}
      password: ${CONFIG_RABBITMQ_PASSWORD}
  ```

  You must relate these variables  with the kubernetes deployment descriptor (`src/main/fabric8/authorization-server-deployment.yml` in this case)

  ```YAML
  - name: CONFIG_RABBITMQ_HOST
    valueFrom:
      secretKeyRef: 
        key: hotst
        name: rabbitmq-secrets
  - name: CONFIG_RABBITMQ_PORT
    valueFrom:
      secretKeyRef: 
        key: port
        name: rabbitmq-secrets
  - name: CONFIG_RABBITMQ_USER
    valueFrom:
      secretKeyRef: 
        key: user
        name: rabbitmq-secrets
  - name: CONFIG_RABBITMQ_PASSWORD
    valueFrom:
      secretKeyRef: 
        key: password
        name: rabbitmq-secrets
  ```

  The secret `rabbitmq-secrets` must be exist in kubernetes

After that, when you change a value in the microservice configuration file (located in the repository) You can run the following command to `/bus/refresh` 

Note that we are running a new bus endpoint against one of the instances, in this case: 

```shell
curl –d {} localhost:9090/bus/refresh
```


 Immediately, we will see the following message in each instance: 
 
 ```shell
 Received remote refresh request. Keys refreshed [property.prop]
 ```
 
 The bus endpoint sends a message to the message broker internally, which is eventually consumed by all instances, reloading their property files. Changes can also be applied to a specific application by specifying the application name like so:

```shell
/bus/refresh?destination=search-service:**
```

We can also refresh specific properties by setting the property name as a parameter.

## Auto refresh with WebHooks 

Many source code repository providers (like Github, Gitlab, Gogs or Bitbucket for instance) will notify you of changes in a repository through a webhook. 

You can configure the webhook via the provider’s user interface as a URL and when you push in the repository the webhook is activated and it can make a POST request to `/bus/refresh` endpoint in order to refresh the configuration as explained on Spring Cloud Bus section

![WebhookConf](src/main/doc/images/WebhookConf.png)

> During the development spring-cloud-config-monitor was examined as an alternative to provide the same solution but it did not pass the stress test.
 




