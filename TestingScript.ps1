# ==========================
# CONFIG
# ==========================
$BaseUrl  = "http://localhost:8080"
$Username = "admin_user"
$Password = "Password123!"

# ==========================
# 1) Login to get fresh token
# ==========================
$loginBody = @{
    username = $Username
    password = $Password
} | ConvertTo-Json

$loginResponse = Invoke-WebRequest -Uri "$BaseUrl/api/auth/login" `
    -Method POST `
    -Headers @{ "Content-Type" = "application/json" } `
    -Body $loginBody

$loginJson = $loginResponse.Content | ConvertFrom-Json

$token = $loginJson.token
$role  = $loginJson.role

Write-Host "Login status code:" $loginResponse.StatusCode
Write-Host "Token (first 40 chars):" ($token.Substring(0, [Math]::Min(40, $token.Length))) "..."
Write-Host "Role from server:" $role

if (-not $token) {
    throw "Login failed: token was null/empty. Check credentials or the /api/auth/login endpoint."
}

# ==========================
# 2) Call secured GET /api/tickets
# ==========================
$authHeaders = @{
    "Authorization" = "Bearer $token"
}

$ticketsResponse = Invoke-WebRequest -Uri "$BaseUrl/api/tickets" `
    -Headers $authHeaders `
    -Method GET

Write-Host "GET /api/tickets status code:" $ticketsResponse.StatusCode

$ticketsJson = $ticketsResponse.Content | ConvertFrom-Json

# Show a quick table of tickets (id, code, status, fullName)
$ticketsJson | Format-Table id, ticketCode, assetName, dateSubmitted, fullName, employeeID, status
