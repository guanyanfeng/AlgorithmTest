#!/bin/bash

function printLog() {
    echo ""$(date +%Y%m%d-%H%M%S)" - [$(basename $0)]: $2"
}

srcfile="$(echo $1 | sed 's/"//g')"
dstfile="$(echo $2 | sed 's/"//g')"
printLog "start copy source file: ${srcfile} to releasenotes: ${dstfile}"
cp -p "${srcfile}" "${dstfile}"
if [ "$?" -eq "0" ]; then
    printLog "copy releasenotes success!"
else
    printLog "copy releasenotes failed!"
fi
