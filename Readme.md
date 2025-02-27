# Demo application development

## Overview

This project demonstrates the development process of a Web based application from the earliest stage to one that is 
containerized with Kubernetes. The emphasis is on a training material for Istio 

Various branches are created in order to allow the audience to walk through the code at any stages at a time.

## Stage 1 - Dockerized base

### Application sources

This folder contains the source code for the application in the demo project

- ./Istio-sample/Basic-Application/sources/Router contains the configuration to build an image of the router based on nginx
- ./Istio-sample/Basic-Application/sources/Sample contains source files of a Gradle base project that builds an image of a Java and SpringBoot based Restful server
- ./Istio-sample/Basic-Application/sources/SampleUI contains source files that builds an image of an Angular application serving as the User Interface

### Running on Docker compose
The file at ./sources/docker-compose.yaml contains instructions to run the application in a Docker compose environment
To build the project and deploy containers with docker compose use:
> docker compose build
> docker compose up -d

to undeploy
> docker compose down

The file at ./design/BasicApp.eapx contains an Enterprise Architect (https://sparxsystems.com/) project

## Stage 2 - Minimal Kubernetes 

### Helm sources

I've added more files to the project.The Helm Chart at ./helm/basic-app contains all that is needed to install Pods and Services for the application to run simply in
To install with helm use:

>helm install sample basic-app --atomic

To uninstall
>helm uninstall sample

### Documentation

The file at ./documentation/Step2.docx contains my notes about the application at this stage. 

This is the component diagram at this stage

![Component diagram](https://github.com/pmaierean/trainingApp/blob/step2-minimal-kubernetes/Istio-sample/Basic-Application/documentation/Step2.png)

## Stage 3 - Application with Istio as a service mesh

### Istio install scripts
I've added the shell scripts at ./scripts. To install istio (https://istio.io/latest/) use:
> sh install_istio.sh

To uninstall form a Kubernetes cluster istio use:
> sh uninstall_istio.sh

For bare-metal Kubernetes clusters, I've also provided install and uninstall scripts for MetalLB (https://metallb.io/) 

### Documentation

The file at ./documentation/Step3.docx contains my notes about Step3

The component diagram at this stage of development is below

![Component diagram](https://github.com/pmaierean/trainingApp/blob/main/Istio-sample/Basic-Application/documentation/Step3.png)

## Stage 4 - Application with a persistence layer on Postgresql

### Database definition
I've added a Postgres service with docker-compose-yaml, along with one running Liquidbase (https://www.liquibase.com/) that
creates the schema used by the application. I've added the persistence layer to the Restful application that maps the database
tables to POJOs with a JPA library.

The database connection is made directly from the Restful container in Kubernets (see Istio-sample/Basic-Application/helm/basic-app-3/charts/training-restful/values.yaml)

### Documentation

The component diagram at this stage of development is below

![Component diagram](https://github.com/pmaierean/trainingApp/blob/main/Istio-sample/Basic-Application/documentation/Step4.png)

## Stage 5 - Installing native cloud Postgres operator

The goal is to have a persistence layer within the Kubernetes cluster for the application. The replication of the database
is such that it can recreate schema and data upon cluster migration (with liquidbase) upon recreation. 