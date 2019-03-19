#!/bin/bash

source ./setEnv.sh

printLog "start to run algorithmtest."

if [ $(ps -ef | grep "cn.utstarcom.algorithmtest.AlgorithmtestApplication" | grep -v grep | wc -l) -eq "1" ]; then
    printLog "the algorithmtest has been running. exit!"
    exit 0
fi

cd ..
ALGORITHMTEST_HOME=$(pwd)
export ALGORITHMTEST_HOME

if [ $(ps -ef | grep "algorithmtest_monitor.py" | grep -v grep | wc -l) -eq "0" ]; then
    printLog "run algorithmtest_monitor.py script."
    cd ./bin
    nohup ./algorithmtest_monitor.py &
    cd ..
fi

CLASSPATH="$ALGORITHMTEST_HOME"/lib/*

[ -d ${ALGORITHMTEST_HOME}/logs/gc ] || mkdir -p ${ALGORITHMTEST_HOME}/logs/gc
if [ $? -ne "0" ]; then
    printLog "create gc log dir : ${ALGORITHMTEST_HOME}/logs/gc failed! exit!"
    exit 2
fi

# uapollo opts
UAPOLLO_OPTS="-Dis.access.uapollo=${IS_ACCESS_UAPOLLO}"
if [ "${IS_ACCESS_UAPOLLO}" == "true" ]; then
    UAPOLLO_OPTS="${UAPOLLO_OPTS} -Duapollo.server-port=${UAPOLLO_SEVER_PORT}"
    if [ "${UAPOLLO_CLIENT_IP}" != "" ]; then
        UAPOLLO_OPTS="${UAPOLLO_OPTS} -Duapollo.client.ip=${UAPOLLO_CLIENT_IP}"
    fi
    printLog "IS_ACCESS_UAPOLLO: ${IS_ACCESS_UAPOLLO} UAPOLLO_SEVER_PORT: ${UAPOLLO_SEVER_PORT} UAPOLLO_CLIENT_IP: ${UAPOLLO_CLIENT_IP}"
else
    printLog "IS_ACCESS_UAPOLLO: ${IS_ACCESS_UAPOLLO}"
fi

printLog "run algorithmtest main program."
printLog "algorithmtest used memory size: ${JAVAMEM}"
CURRENT_DATE=$(date '+%Y%m%d-%H.%M.%S')
GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:MaxDirectMemorySize=128M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${ALGORITHMTEST_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"
#performance stable, startup slowly for -XX:+AlwaysPreTouch
#GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:MaxDirectMemorySize=1024M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${ALGORITHMTEST_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"

$JAVA -server $GC_OPTS -classpath "$CLASSPATH" -Djava.net.preferIPv4Stack=true -Dspring.property.path="${ALGORITHMTEST_HOME}" -Dlogging.config=file:"${ALGORITHMTEST_HOME}"/config/logback.xml ${UAPOLLO_OPTS} cn.utstarcom.algorithmtest.AlgorithmtestApplication
