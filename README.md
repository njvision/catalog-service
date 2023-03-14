Run the test Gradle task to execute the application’s tests

$ ./gradlew test

Running the application

$ ./gradlew bootRun

Package application as a container image

$ ./gradlew bootBuildImage

Get the details of the newly created image

$ docker images catalog-service:0.0.1-SNAPSHOT

Where: <project_name>:<version> 
is:    catalog-service:0.0.1-SNAPSHOT

Run the image and verify that the containerized application is working correctly

$ docker run --rm --name catalog-service -p 8080:8080 catalog-service:0.0.1-SNAPSHOT

In case appear message: “WARNING: The requested image’s platform (linux/amd64) does not match the detected host platform (linux/arm64/v8) and no specific platform was requested.”

$ docker run --rm --name catalog-service -p 8080:8080 --platform linux/amd64 catalog-service:0.0.1-SNAPSHOT

Start a local Kubernetes cluster with the following command:

$ minikube start --no-vtx-check

$ minikube kubectl -- get po -A

$ minikube dashboard

Manually import it into your local cluster:

$ minikube image load catalog-service:0.0.1-SNAPSHOT

Create deployment resource that will make Kubernetes create application instances as Pod resources:

$ kubectl create deployment catalog-service --image=catalog-service:0.0.1-SNAPSHOT

By default, applications running in Kubernetes are not accessible. Let’s fix that.
First, you can expose 
Catalog Service to the cluster through a Service resource by running the following command:

$ kubectl expose deployment catalog-service --name=catalog-service --port=8080

You can then forward the traffic from a local port on your computer (for example, 8000) to the port exposed by the Service inside the cluster (8080).

$ kubectl port-forward service/catalog-service 8000:8080

Delete the Service:

$ kubectl delete service catalog -service

Delete the deployment:

$ kubectl delete deployment catalog -service

Stop minikube:

$ minikube stop

Create Jar file and run it:

$ ./gradlew bootJar

$ java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar


