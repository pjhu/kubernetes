# use "native" spring config 

## set up
```
k apply -k .
k apply -f springconfigclient-config.yaml
k apply -f traefik-spring-config-server-service.yml
k apply -f traefik-spring-config-server-client.yml
```

<!--
1. k apply -k kustomization.yaml, this command need medicine-backend-prod.yml must be current directory or sub directory
2. the created configMap name is gen-springconfigserver-configmap-k62ctmbh55, need to be specify name
3. config-client need config-server accessible
 -->

## test client to use config server
```
curl -H Host:springconfigclient.localhost localhost:30001
```

## if you want test config server only
1. need to add nodePort to traefik-spring-config-server-service.yml, example is 30003
2. run the follow command
```
curl localhost:30003/medicine-backend-prod.yml
curl localhost:30003/medicine-backend/prod
curl localhost:30003/acuator/health
curl localhost:30003/acuator/info
```

## refresh
```
curl -H Host:springconfigclient.localhost -X POST localhost:30001/acuator/refresh
```

# use git repo

## set up
```
k apply -k .
k apply -f springconfigclient-config.yaml
k apply -f traefik-spring-config-server-service-git.yml
k apply -f traefik-spring-config-server-client.yml
```

<!--
1. k apply -k kustomization.yaml, this command need medicine-backend-prod.yml must be current directory or sub directory
2. the created configMap name is gen-springconfigserver-configmap-k62ctmbh55, need to be specify name
3. config-client need config-server accessible
 -->

## test client to use config server
```
curl -H Host:springconfigclient.localhost localhost:30001
```

## if you want test config server only
1. need to add nodePort to traefik-spring-config-server-service-git.yml, example is 30004
2. run the follow command
```
curl localhost:30004/medicine-backend-prod.yml
curl localhost:30003/medicine-backend/prod
curl localhost:30003/acuator/health
curl localhost:30003/acuator/info
```

## refresh
```
curl -H Host:springconfigclient.localhost -X POST localhost:30001/acuator/refresh
```
