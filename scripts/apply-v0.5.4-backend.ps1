$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot

$candidates = @(
    "services\integrations\irs-simulator"
)

$servicePath = $null

foreach ($candidate in $candidates) {
    $candidatePath = Join-Path $root $candidate
    $pomCandidate = Join-Path $candidatePath "pom.xml"

    if (Test-Path $pomCandidate) {
        $servicePath = $candidatePath
        break
    }
}

if ($null -eq $servicePath) {
    throw "Could not locate the IRS simulator pom.xml."
}

$templatePath = Join-Path $root "services\integrations\irs-simulator"

if ($servicePath -ne $templatePath) {
    $sourceJavaPath = Join-Path $templatePath "src\main\java\com\refundplatform\irs\*"
    $targetJavaPath = Join-Path $servicePath "src\main\java\com\refundplatform\irs"

    if (-not (Test-Path $targetJavaPath)) {
        New-Item -ItemType Directory -Path $targetJavaPath -Force | Out-Null
    }

    Copy-Item `
        -Path $sourceJavaPath `
        -Destination $targetJavaPath `
        -Recurse `
        -Force

    $sourceResourcesPath = Join-Path $templatePath "src\main\resources\application.yml"
    $targetResourcesPath = Join-Path $servicePath "src\main\resources\application.yml"
    $targetResourcesDirectory = Split-Path -Parent $targetResourcesPath

    if (-not (Test-Path $targetResourcesDirectory)) {
        New-Item -ItemType Directory -Path $targetResourcesDirectory -Force | Out-Null
    }

    Copy-Item `
        -Path $sourceResourcesPath `
        -Destination $targetResourcesPath `
        -Force
}

$pomPath = Join-Path $servicePath "pom.xml"
$pom = Get-Content $pomPath -Raw

if ($pom -notmatch "spring-boot-starter-jdbc") {
    $dependencies = @"

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
"@

    $pom = $pom.Replace(
        "</dependencies>",
        "$dependencies`r`n    </dependencies>"
    )

    Set-Content `
        -Path $pomPath `
        -Value $pom `
        -Encoding UTF8
}

Write-Host ""
Write-Host "IRS simulator updated successfully."
Write-Host "Detected service path: $servicePath"
Write-Host "Updated pom.xml: $pomPath"
