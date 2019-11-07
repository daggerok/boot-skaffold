# boot-skaffold [![Build Status](https://travis-ci.org/daggerok/boot-skaffold.svg?branch=master)](https://travis-ci.org/daggerok/boot-skaffold)
Using spring-boot maven project together with Google JIB and Skaffold

## table of content
* [level 0: no skaffold: local workflow](#level-0-no-skaffold-local-workflow)
* [level 1: no skaffold: docker workflow](#level-1-no-skaffold-docker-workflow)
* [level 2: no skaffold: jib docker workflow](#level-2-no-skaffold-jib-docker-workflow)
* [level 3: skaffold pod workflow](#level-3-skaffold-pod-workflow)
* [level 4: full skaffold jib workflow](#level-4-full-skaffold-jib-workflow)
* [TODO: level 5: skaffold jib on k3d](#TODO-level-5-skaffold-jib-on-k3d)
* [links](#links)

## level 4: full skaffold jib workflow

```bash
brew reinstall skaffold
skaffold init -f k8s.yaml
```

You will need at least one k8s yaml files:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: boot-skaffold
  labels:
    app: boot-skaffold
spec:
  selector:
    matchLabels:
      app: boot-skaffold
  replicas: 1
  template:
    metadata:
      name: boot-skaffold
      labels:
        app: boot-skaffold
    spec:
      containers:
        - name: boot-skaffold
          image: daggerok/boot-skaffold
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: boot-skaffold
  labels:
    app: boot-skaffold
spec:
  ports:
    - port: 8080
      name: http
  type: LoadBalancer
  selector:
    app: boot-skaffold
```

Next, all you need to do is only add to `build` section:

```yaml
build:
  artifacts:
  - image: daggerok/boot-skaffold
    context: .
    jib: {}
```

Full skaffold.yaml should looks like so:

```yaml
apiVersion: skaffold/v1beta17
kind: Config
metadata:
  name: boot-skaffold
build:
  artifacts:
  - image: daggerok/boot-skaffold
    jib: {}
deploy:
  kubectl:
    manifests:
    - k8s.yaml
```

Now we can develop!

```bash
kubectl get pods -o wide -w 
skaffold dev
```

```bash
http :8080/actuator/info
http :8080/actuator/env | jq '.propertySources[].properties.HOSTNAME.value' 
```

## level 3: skaffold pod workflow

```bash
brew reinstall skaffold
skaffold init -f k8s.yaml
```

You will need at least one k8s yaml file:

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: boot-skaffold
  labels:
    app: boot-skaffold
spec:
  containers:
    - name: boot-skaffold
      image: daggerok/boot-skaffold
      imagePullPolicy: IfNotPresent
```

Next, all you need to do is only add `jib: {}` to artifact built with maven jib project:

```yaml
apiVersion: skaffold/v1beta17
kind: Config
metadata:
  name: boot-skaffold
build:
  artifacts:
  - image: daggerok/boot-skaffold
    jib: {}
deploy:
  kubectl:
    manifests:
    - k8s-pod.yaml
```

Now we can run in dev mode:

```bash
kubectl port-forward boot-skaffold 8080
http :8080/actuator/info
```

## level 2: no skaffold: jib docker workflow

```bash
./mvnw package dockerBuild
docker run -it --rm --name app -p 8080:8080 daggerok/boot-skaffold
http :8080/actuator/info
```

## level 1: no skaffold: docker workflow

```bash
docker build -t daggerok/boot-skaffold .
docker run -it --rm --name app -p 8080:8080 daggerok/boot-skaffold
http :8080/actuator/info
```

## level 0: no skaffold: local workflow

```bash
./mvnw package dockerBuild
java -jar ./target/*.jar
http :8080/actuator/info
```

## TODO: level 5: skaffold jib on k3d

```bash
brew reinstall skaffold k3d

k3d create --name k3d --api-port 6551 --publish 8080:8080 --workers 1
export KUBECONFIG="$(k3d get-kubeconfig --name='k3d')"
kubectl cluster-info

#skaffold init -f k8s-k3d.yaml
#vi skaffold.yaml
skaffold dev
#...
```

## links
For further reference, please consider the following sections:

* [YouTube: Develop Faster on Kubernetes With Google Container Tools and Cloud Build (Cloud Next '19)](https://www.youtube.com/watch?v=TYx0BTyFtmc)
* https://stedolan.github.io/jq/manual/#Basicfilters
* https://kubernetes.io/docs/tasks/access-application-cluster/access-cluster/#without-kubectl-proxy
* https://github.com/rancher/k3d/blob/master/docs/examples.md
* TODO: https://github.com/rancher/k3d/blob/master/docs/examples.md
<!--
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/maven-plugin/)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.2.0.RELEASE/spring-framework-reference/languages.html#coroutines)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/htmlsingle/#production-ready)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/htmlsingle/#using-boot-devtools)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
-->
