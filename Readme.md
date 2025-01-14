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

The file at ./sources/docker-compose.yaml contains instructions to run the application in a Docker compose environment

