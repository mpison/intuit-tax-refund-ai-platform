$ErrorActionPreference = "Stop"

$images = @(
    "customer-mcp-java:0.5.7.0",
    "refund-mcp-java:0.5.7.0",
    "prediction-mcp-java:0.5.7.0",
    "customer-mcp-python:0.5.7.0",
    "refund-mcp-python:0.5.7.0",
    "prediction-mcp-python:0.5.7.0"
)

foreach ($image in $images) {
    kind load docker-image $image --name refund-demo
}
