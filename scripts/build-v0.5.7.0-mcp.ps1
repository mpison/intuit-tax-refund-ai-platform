$ErrorActionPreference = "Stop"

$services = @(
    "customer-mcp-server",
    "refund-mcp-server",
    "prediction-mcp-server"
)

foreach ($service in $services) {
    $javaPath = ".\services\ai\mcp\java\$service"

    mvn -f "$javaPath\pom.xml" clean package

    $javaImage = $service.Replace("-server", "-java")

    docker build `
      --no-cache `
      -t "${javaImage}:0.5.7.0" `
      $javaPath
}

$pythonRoot = ".\services\ai\mcp\python"

foreach ($service in $services) {
    $pythonImage = $service.Replace("-server", "-python")

    docker build `
      --no-cache `
      -f "$pythonRoot\$service\Dockerfile" `
      -t "${pythonImage}:0.5.7.0" `
      $pythonRoot
}
