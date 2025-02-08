#!/bin/sh

if [ -n "`helm repo list | grep "prometheus-community"`" ]; then
  echo 'prometheus-community repository has already been added to the list of repositories'
else
    helm repo add add prometheus-community https://prometheus-community.github.io/helm-charts
    helm repo update
fi

if [ -n "`helm list -n prometheus | grep "prometheus"`" ]; then
  echo 'prometheus has been installed'
else
  helm install prometheus prometheus-community/prometheus --namespace istio-system
fi

if [ -n "`helm repo list | grep "kiali"`" ]; then
  echo 'kiali has already been added to the list of Helm repositories'
else
  helm repo add add kiali https://kiali.org/helm-charts
  helm repo update
fi

if [ -n "`helm list -n istio-system | grep "kiali-server"`" ]; then
  echo 'kiali-server has already been installed'
else
  kubectl apply -f prometheus-for-kiali.yaml
  helm install --namespace istio-system kiali-server kiali/kiali-server
  kubectl apply -f kiali-gateway.yaml
  # Generate an access token
  # kubectl -n istio-system create token kiali
fi
