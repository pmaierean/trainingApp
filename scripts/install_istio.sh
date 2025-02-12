#!/bin/sh

if [ -n "`helm repo list | grep "istio"`" ]; then
  echo 'istio has already been added to the list of Helm repositories'
else
  helm repo add istio https://istio-release.storage.googleapis.com/charts
  helm repo update
fi

istioctl install --set profile=demo --set meshConfig.outboundTrafficPolicy.mode=REGISTRY_ONLY --set meshConfig.accessLogFile=/dev/stdout -y
istioctl verify-install
