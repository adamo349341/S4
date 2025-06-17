function Connect-ToVCenter {
    param (
        [string]$vcenter,
        [string]$username,
        [string]$password
    )

    try {
        Write-Host " Connexion à vCenter: $vcenter..."
        Connect-VIServer -Server $vcenter -User $username -Password $password -ErrorAction Stop
        Write-Host " Connexion réussie à $vcenter"
    } catch {
        Write-Error " Échec de connexion à vCenter: $_"
        exit 1
    }
}

function Deploy-VMFromTemplate {
    param (
        [PSCustomObject]$Config
    )

    try {
        Write-Host " Recherche du template: $($Config.template)..."
        $template = Get-Template -Name $Config.template -ErrorAction Stop
    } catch {
        Write-Error "Template introuvable: $($Config.template)"
        exit 1
    }

    try {
        Write-Host " Recherche du cluster: $($Config.cluster)..."
        $cluster = Get-Cluster -Name $Config.cluster -ErrorAction Stop
        $vmhost = Get-VMHost -Location $cluster | Get-Random
        Write-Host " Hôte sélectionné: $($vmhost.Name)"
    } catch {
        Write-Error " Cluster ou hôte introuvable"
        exit 1
    }

    try {
        Write-Host " Déploiement de la VM: $($Config.vm_name)..."
        New-VM -Name $Config.vm_name `
               -Template $template `
               -Datastore $Config.datastore `
               -VMHost $vmhost `
               -NetworkName $Config.network `
               -Confirm:$false `
               -ErrorAction Stop

        # Configuration des ressources
        Set-VM -VM $Config.vm_name -MemoryMB $Config.memory -NumCPU $Config.cpu -Confirm:$false

        # Démarrage de la VM
        Start-VM -VM $Config.vm_name

        Write-Host " VM $($Config.vm_name) déployée avec succès"
    } catch {
        Write-Error " Erreur lors du déploiement de la VM: $_"
        exit 1
    }
}