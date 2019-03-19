#!/bin/bash

source ./setEnv.sh

printLog "stop algorithmtest ..."
./stop_algorithmtest.sh
printLog "stop algorithmtest success! start the algorithmtest ..."
nohup ./start_algorithmtest.sh &
printLog "start algorithmtest completed, please use 'tail -f nohup.out' to check start result."
