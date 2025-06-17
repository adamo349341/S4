#!/bin/bash

echo "==== Build de l'image Docker ===="

docker build -t password-manager-server .

if [ $? -eq 0 ]; then
  echo "Build terminé avec succès"
else
  echo "Build échoué"
  exit 1
fi
