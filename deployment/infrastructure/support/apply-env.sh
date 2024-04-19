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

terraform workspace new "${env_id}" 2> /dev/null
terraform workspace select "${env_id}"

terraform apply
