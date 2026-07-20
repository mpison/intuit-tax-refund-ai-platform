$ErrorActionPreference = "Stop"

mvn -f .\services\ai\policy-management-service\pom.xml clean package

docker build --no-cache -t policy-management-service:0.5.6 .\services\ai\policy-management-service
docker build --no-cache -t admin-ui:0.5.6 .\applications\admin-ui
