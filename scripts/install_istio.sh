#!/bin/sh

if [ -n "`helm repo list | grep "istio"`" ]; then
  echo 'istio has already been added to the list of Helm repositories'
else
  helm repo add istio https://istio-release.storage.googleapis.com/charts
  helm repo update
fi

if [ -n "`helm list -n istio-system | grep "istio-base"`" ]; then
  echo 'istio-base has already been installed'
else
  helm install istio-base istio/base -n istio-system --set defaultRevision=default --create-namespace
fi

if [ -n "`helm list -n istio-system | grep "istiod"`" ]; then
  echo 'istiod has already been installed'
else
  helm install istiod istio/istiod -n istio-system --wait
fi

if [ -n "`helm list -n istio-ingress | grep "istio-ingressgateway"`" ]; then
  echo 'istio-ingressgateway has already been installed'
else
  helm install istio-ingressgateway istio/gateway -n istio-system --create-namespace
fi
