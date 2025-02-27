#!/bin/sh

helm delete cnpg -n cnpg-system
kubectl delete namespace cnpg-system
