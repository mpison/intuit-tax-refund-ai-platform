$ErrorActionPreference = "Stop"

$root =
    Split-Path `
        -Parent `
        $PSScriptRoot

& (
    Join-Path `
        $PSScriptRoot `
        "apply-v0.5.4-backend.ps1"
)

$candidates =
    @(
        "services\integrations\irs-simulator",
        "services\simulator\irs-simulator",
        "services\core\irs-simulator",
        "services\irs-simulator"
    )

$servicePath =
    $null

foreach (
    $candidate
    in $candidates
) {

    $candidatePath =
        Join-Path `
            $root `
            $candidate

    if (
        Test-Path (
            Join-Path `
                $candidatePath `
                "pom.xml"
        )
    ) {

        $servicePath =
            $candidatePath

        break
    }
}

if (
    $null -eq $servicePath
) {

    throw "Could not locate IRS simulator."
}

mvn `
  -f (
      Join-Path `
          $servicePath `
          "pom.xml"
  ) `
  clean package

docker build `
  --no-cache `
  -t irs-simulator:0.5.4 `
  $servicePath

docker build `
  --no-cache `
  -t irs-admin-ui:0.5.4 `
  (
      Join-Path `
          $root `
          "applications\irs-admin-ui"
  )
