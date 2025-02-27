#!/bin/sh

if [ -n "`helm repo list | grep "cnpg"`" ]; then
  echo 'cnpg has already been added to the list of Helm repositories'
else
  helm repo add cnpg https://cloudnative-pg.github.io/charts
  helm repo update
fi

if [ -n "`helm list -n cnpg-system | grep "cnpg"`" ]; then
  echo 'cnpg has already been installed'
else
  # This is a single namespace installation
  helm upgrade --install cnpg --namespace cnpg-system --create-namespace --set config.clusterWide=false cnpg/cloudnative-pg
fi
