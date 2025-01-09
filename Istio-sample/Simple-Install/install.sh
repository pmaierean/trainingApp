# Refer to instructions at https://istio.io/latest/docs/setup/install/helm/
# helm repo add istio https://istio-release.storage.googleapis.com/charts

# see https://github.com/istio/istio/tree/master/manifests/charts/base
# to list the the configuration values for istio/base do:
# helm show values istio/base
helm install istio-base istio/base -n istio-system --set defaultRevision=default --create-namespace

# https://github.com/istio/istio/tree/master/manifests/charts/istio-control/istio-discovery
# to list the the configuration values for istio/istiod do:
# helm show values istio/istiod
helm install istiod istio/istiod -n istio-system --wait

# helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
# See https://github.com/prometheus-community/helm-charts/tree/main/charts
helm install prometheus prometheus-community/prometheus

# helm repo add grafana https://grafana.github.io/helm-charts
# See https://github.com/grafana/helm-charts/tree/main/charts/grafana
helm install graphana grafana/grafana

# helm repo add jaegertracing https://jaegertracing.github.io/helm-charts
# helm install jaeger jaegertracing/jaeger

# helm repo add kiali https://kiali.org/helm-charts
# see https://github.com/kiali/helm-charts/tree/master
helm install kiali-server kiali/kiali-server --namespace istio-system

kubectl create namespace istio-ingress
helm install istio-ingress istio/gateway -n istio-ingress --wait
