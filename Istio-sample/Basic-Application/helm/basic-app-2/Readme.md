# Package description

This is a Helm Chart that installs the application in Kubernetes with am Ingres Gateway. For testing with minikube, an ingress controller
needs to be added 

>minikube addons enable ingress

The above will load a new pod ingress-nginx which will act as the gateway.
The training-restful is connecting to an external database in order to persist the values in the table to display. Having no istio sidecar with 
the containers, that outbound connection is made without intermediaries.


