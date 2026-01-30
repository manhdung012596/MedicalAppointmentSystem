$ErrorActionPreference = "Stop"

function Test-Endpoint {
    param (
        [string]$Url,
        [int]$ExpectedStatus = 200,
        [string]$ExpectedContent = ""
    )

    Write-Host "Testing $Url ..." -NoNewline

    try {
        $response = Invoke-WebRequest -Uri $Url -UseBasicParsing
        $actualStatus = $response.StatusCode
        
        if ($actualStatus -eq $ExpectedStatus) {
            Write-Host " [PASS] (Status: $actualStatus)" -ForegroundColor Green
            
            if ($ExpectedContent -ne "") {
                if ($response.Content -match $ExpectedContent) {
                    Write-Host "   -> Content Verified: '$ExpectedContent'" -ForegroundColor Gray
                } else {
                    Write-Host "   -> [FAIL] Content mismatch. Expected '$ExpectedContent'" -ForegroundColor Red
                }
            }
        } else {
            Write-Host " [FAIL] (Status: $actualStatus, Expected: $ExpectedStatus)" -ForegroundColor Red
        }
    } catch {
        # Check if it's a web exception (4xx, 5xx)
        if ($_.Exception.Response) {
            $actualStatus = $_.Exception.Response.StatusCode.value__
            if ($actualStatus -eq $ExpectedStatus) {
                Write-Host " [PASS] (Status: $actualStatus)" -ForegroundColor Green
            } else {
                Write-Host " [FAIL] (Status: $actualStatus)" -ForegroundColor Red
            }
        } else {
            Write-Host " [ERROR] $($_.Exception.Message)" -ForegroundColor Red
        }
    }
}

Write-Host "=== Medical Appointment System Automated Tests ===" -ForegroundColor Cyan

# 1. Home Page
Test-Endpoint -Url "http://localhost:8080/" -ExpectedStatus 200

# 2. Login Page
Test-Endpoint -Url "http://localhost:8080/login" -ExpectedStatus 200

# 3. Doctor Dashboard (Redirects to Appointments)
Test-Endpoint -Url "http://localhost:8080/doctor/dashboard" -ExpectedStatus 200

# 4. Doctor Personal Page
Test-Endpoint -Url "http://localhost:8080/doctor/personal" -ExpectedStatus 200

# 5. Doctor Prescribe Page
Test-Endpoint -Url "http://localhost:8080/doctor/prescribe" -ExpectedStatus 200

Write-Host "=== Tests Completed ===" -ForegroundColor Cyan
