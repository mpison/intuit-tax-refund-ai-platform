$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot

$items = @(
    @{
        Target = "README.md"
        Section = "docs\patch-sections\README_v0.5.4.md"
        Marker = "## IRS Simulator Console"
    },
    @{
        Target = "CHANGELOG.md"
        Section = "docs\patch-sections\CHANGELOG_v0.5.4.md"
        Marker = "## v0.5.4"
    },
    @{
        Target = "ROADMAP.md"
        Section = "docs\patch-sections\ROADMAP_v0.5.4.md"
        Marker = "## v0.5.4 IRS Simulator Console"
    }
)

foreach ($item in $items) {

    $targetPath = Join-Path $projectRoot $item.Target
    $sectionPath = Join-Path $projectRoot $item.Section

    if (!(Test-Path $sectionPath)) {
        Write-Host "Missing section file: $sectionPath"
        continue
    }

    if (!(Test-Path $targetPath)) {
        New-Item -ItemType File -Path $targetPath -Force | Out-Null
    }

    $current = Get-Content $targetPath -Raw

    if ($current -like "*$($item.Marker)*") {

        Write-Host "$($item.Target) already updated."
        continue
    }

    Add-Content $targetPath "`r`n"
    Get-Content $sectionPath | Add-Content $targetPath

    Write-Host "Updated $($item.Target)"
}

Write-Host ""
Write-Host "=========================================="
Write-Host "v0.5.4 documentation patch completed."
Write-Host "=========================================="