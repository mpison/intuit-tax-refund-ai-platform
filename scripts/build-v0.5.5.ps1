$ErrorActionPreference = "Stop"

mvn -f .\services\admin\admin-service\pom.xml clean package

docker build --no-cache -t admin-service:0.5.5 .\services\admin\admin-service
docker build --no-cache -t admin-ui:0.5.5 .\applications\admin-ui
