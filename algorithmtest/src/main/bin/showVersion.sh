#!/bin/sh

MODULENAME=$(echo "algorithmtest" | tr '[a-z]' '[A-Z]')
version=$(ls ../lib/*_${MODULENAME}_* | awk -F/ '{ print $NF }' | sed -n '1p' | sed -e "s/.jar//g")

echo "        Module: ${MODULENAME}"
echo "System Version: ${version}"