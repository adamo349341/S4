#!/bin/bash

echo "==== Test de l'image ===="

# Stopper les anciens conteneurs
docker rm -f password-manager-server-test 2>/dev/null

# Lancer le serveur en mode test
docker run --name password-manager-server-test -p 8443:8443 -d password-manager-server

sleep 5

# Test simple sur /debug
curl -k https://localhost:8443/debug

docker logs password-manager-server-test

# Nettoyage
docker stop password-manager-server-test
docker rm password-manager-server-test
