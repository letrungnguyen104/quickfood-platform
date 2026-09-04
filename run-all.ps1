$root = $PSScriptRoot

$services = @(
    "api-gateway",
    "auth-service",
    "media-service",
    "menu-service",
    "order-service",
    "restaurant-service",
    "user-service"
)

foreach ($service in $services) {
    Write-Host "Starting $service..."

    $servicePath = Join-Path $root $service

    Start-Process powershell -ArgumentList `
        "-NoExit", `
        "-Command", `
        "Set-Location '$servicePath'; .\mvnw.cmd clean spring-boot:run"
}