#!/bin/sh

if [ -n "`helm repo list | grep "metallb"`" ]; then
  echo 'metallb has already been added to the list of Helm repositories'
else
  helm repo add metallb https://metallb.github.io/metallb
  helm repo update
fi

if [ -n "`helm list | grep "metallb"`" ]; then
  echo 'metallb has already been installed'
else
  helm install metallb metallb/metallb -n metallb-system --create-namespace
  # Contains the IP range for Load balancers
  kubectl create -f metallb.yaml
fi
