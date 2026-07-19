$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$pomPath = Join-Path $projectRoot "services\core\refund-status-service\pom.xml"
$pom = Get-Content $pomPath -Raw

if ($pom -notmatch "spring-boot-starter-jdbc") {
    $dependency = @"

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
"@
    $pom = $pom.Replace(
        "</dependencies>",
        "$dependency`r`n    </dependencies>"
    )
}

if ($pom -notmatch "<artifactId>flyway-core</artifactId>") {
    $dependency = @"

        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-database-postgresql</artifactId>
        </dependency>
"@
    $pom = $pom.Replace(
        "</dependencies>",
        "$dependency`r`n    </dependencies>"
    )
}

Set-Content $pomPath $pom -Encoding UTF8

$resourceDir = Join-Path $projectRoot "services\core\refund-status-service\src\main\resources"

Get-ChildItem $resourceDir -Filter "application*.yml" |
Where-Object { $_.Name -ne "application-flyway.yml" } |
ForEach-Object {
    $content = Get-Content $_.FullName -Raw

    if ($content -notmatch "application-flyway.yml") {
        if ($content -match "(?m)^spring:\s*$") {
            $content = $content -replace `
                "(?m)^spring:\s*$", `
                "spring:`r`n  config:`r`n    import: optional:classpath:application-flyway.yml"
        }
        else {
            $content =
                "spring:`r`n  config:`r`n    import: optional:classpath:application-flyway.yml`r`n`r`n" `
                + $content
        }

        Set-Content $_.FullName $content -Encoding UTF8
    }
}

$refundManifest = Join-Path $projectRoot "infrastructure\kubernetes\base\refund-status-service.yaml"
(Get-Content $refundManifest -Raw) `
    -replace "refund-status-service:\d+\.\d+\.\d+", "refund-status-service:0.5.2" |
Set-Content $refundManifest -Encoding UTF8

$uiManifest = Join-Path $projectRoot "infrastructure\kubernetes\base\customer-ui.yaml"
(Get-Content $uiManifest -Raw) `
    -replace "customer-ui:\d+\.\d+\.\d+", "customer-ui:0.5.2" |
Set-Content $uiManifest -Encoding UTF8

Write-Host "v0.5.2 project updates applied."
