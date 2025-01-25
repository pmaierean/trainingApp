#!/bin/sh

kubernets delete -f metallb.yaml
helm delete metallb -n metallb-system
kubectl delete namespace metallb-system