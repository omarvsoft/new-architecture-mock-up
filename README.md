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
