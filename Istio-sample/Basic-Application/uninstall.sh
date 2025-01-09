helm delete istio-ingress -n istio-ingress
helm uninstall kiali-server --namespace istio-system
# helm uninstall jaeger
helm uninstall graphana
helm uninstall prometheus
kubectl delete namespace istio-ingress
helm delete istiod -n istio-system
helm delete istio-base -n istio-system
kubectl delete namespace istio-system
