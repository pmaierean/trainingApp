#!/bin/sh

istioctl uninstall -y --purge
kubectl delete namespace istio-system
