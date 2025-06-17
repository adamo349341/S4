
. "$PSScriptRoot\functions.ps1"


$configPath = "C:\Users\cyber\Desktop\vm deployement\config\vm-config1.json"
if (-not (Test-Path $configPath)) {
    Write-Error " Fichier config introuvable à l'emplacement : $configPath"
    exit 1
}
$config = Get-Content -Path $configPath | ConvertFrom-Json


Connect-ToVCenter -vcenter  $config.vcenter -username  $config.username -password  $config.password

if (-not (Get-Template -Name  $config.template)) {
    Write-Error " Template $($config.template) introuvable"
    Disconnect-VIServer -Confirm:$false
    exit 1
}


Deploy-VMFromTemplate -Config $config


Disconnect-VIServer -Confirm:$false