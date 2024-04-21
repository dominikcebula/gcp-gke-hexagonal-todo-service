#!/bin/bash

env_id=$1

if [ -z "${env_id}" ]; then
  echo "ENV ID is unset or set to the empty string"
  exit 1
fi

if [[ "${env_id}" != @(ci|snd|dev|cert|prod) ]]; then
  echo "ENV ID needs to be set to one of the following values: ci, snd, dev, cert, prod"
  exit 1
fi

if helm status todo-service > /dev/null 2>&1
then
  operation=upgrade
else
  operation=install
fi

helm ${operation} todo-service -f "values.yaml" -f "environments/values.env-${env_id}.yaml" .
