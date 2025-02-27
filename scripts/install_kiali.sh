#!/bin/sh
if [ -n "`helm repo list | grep "prometheus-community"`" ]; then
  echo 'prometheus-community repository has already been added to the list of repositories'
else
    helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
    helm repo update
fi

if [ -n "`helm list -n prometheus | grep "prometheus"`" ]; then
  echo 'prometheus has been installed'
else
  helm install prometheus prometheus-community/kube-prometheus-stack --namespace monitoring --create-namespace
  # creates services on the istio-system namespace for prometheus and grafana
  kubectl apply -f prometheus-for-kiali.yaml
fi

if [ -n "`helm repo list | grep "kiali"`" ]; then
  echo 'kiali has already been added to the list of Helm repositories'
else
  helm repo add kiali https://kiali.org/helm-charts
  helm repo update
fi

if [ -n "`helm list -n istio-system | grep "kiali-server"`" ]; then
  echo 'kiali-server has already been installed'
else
  helm install --namespace monitoring kiali-server kiali/kiali-server
  kubectl apply -f monitoring-gateway.yaml
  # Generate an access token
  # kubectl -n monitoring create token kiali > kiali.txt
fi
