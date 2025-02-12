# Package description

This is a Helm Chart that installs the application in Kubernetes with am Istio Gateway and adds proxies to all 
containers to begin controlling the traffic at that level. It replaces the default ingress (nginx based) with the
one from Istio. 
This assumes that istio mesh is set with meshConfig.outboundTrafficPolicy.mode=ALLOW_ANY


