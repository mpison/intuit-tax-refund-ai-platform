$ErrorActionPreference = "Stop"

kubectl apply `
  -f .\infrastructure\kubernetes\base\mcp-switchable-v0570.yaml

$deployments = @(
    "customer-mcp-java",
    "refund-mcp-java",
    "prediction-mcp-java",
    "customer-mcp-python",
    "refund-mcp-python",
    "prediction-mcp-python"
)

foreach ($deployment in $deployments) {
    kubectl rollout status "deployment/$deployment" `
      -n refund-platform `
      --timeout=240s
}
