param(
    [Parameter(Mandatory = $true)]
    [ValidateSet("java", "python")]
    [string] $Runtime,

    [ValidateSet("all", "customer", "refund", "prediction")]
    [string] $Domain = "all"
)

$ErrorActionPreference = "Stop"

$domains = if ($Domain -eq "all") {
    @("customer", "refund", "prediction")
}
else {
    @($Domain)
}

foreach ($currentDomain in $domains) {
    $patch = @{
        spec = @{
            selector = @{
                app = "$currentDomain-mcp"
                runtime = $Runtime
            }
        }
    } | ConvertTo-Json -Compress

    kubectl patch service "$currentDomain-mcp" `
      -n refund-platform `
      --type merge `
      -p $patch

    Write-Host "$currentDomain-mcp now routes to $Runtime"
}

kubectl get endpoints `
  -n refund-platform `
  customer-mcp `
  refund-mcp `
  prediction-mcp
