#!/bin/sh

kubectl delete -f kiali-gateway.yaml
kubectl delete -f prometheus-for-kiali.yaml
helm uninstall kiali-server --namespace istio-system
helm uninstall prometheus --namespace istio-system