#!/bin/bash

source ./setEnv.sh

printLog "start to stop algorithmtest."
if [ $(ps -ef | grep "cn.utstarcom.algorithmtest.AlgorithmtestApplication" | grep -v grep | wc -l) -gt "0" ] || [ $(ps -ef | grep "algorithmtest_monitor.py" | grep -v grep | wc -l) -gt "0" ]; then
    #dump algorithmtest threads
    cTime=$(date +%Y%m%d%H%M%S)
    jccPid=$(ps -ef | grep "cn.utstarcom.algorithmtest.AlgorithmtestApplication" | grep -v grep | awk '{ print $2 }')
    printLog "dump algorithmtest threads. algorithmtest pid: "$jccPid""
    if [ ! -z "$jccPid" ]; then
        $JAVA_HOME/bin/jstack -l "$jccPid" >./jstack_"$jccPid"_"$cTime".txt 2>&1
        pstack "$jccPid" >./pstack_"$jccPid"_"$cTime".txt 2>&1
    fi
    #delete expired file
    expiredDays=30
    printLog "delete expired algorithmtest dumped threads file. expired days: "$expiredDays""
    find . -type f -name "jstack_*" -mtime +"$expiredDays" | xargs rm -f
    find . -type f -name "pstack_*" -mtime +"$expiredDays" | xargs rm -f

    #stop algorithmtest
    ccPid=$(ps -ef | grep "cn.utstarcom.algorithmtest.AlgorithmtestApplication" | grep -v grep | awk '{ print $2,$3 }')
    ccPid=$ccPid" "$(ps -ef | grep "algorithmtest_monitor.py" | grep -v grep | awk '{ print $2 }' 2>/dev/null)
    if [ ! -z "$ccPid" ]; then
        printLog "the algorithmtest pid is "$ccPid""
        printLog "run kill -9 "$ccPid""
        kill -9 $ccPid
        printLog "sleep 3 secconds...."
        sleep 3
        while [ $(ps -ef | grep "cn.utstarcom.algorithmtest.AlgorithmtestApplication" | grep -v grep | wc -l) -gt "0" ] || [ $(ps -ef | grep "algorithmtest_monitor.py" | grep -v grep | wc -l) -gt "0" ]; do
            printLog "kill algorithmtest again!!!"
            printLog "kill -9 "$ccPid""
            kill -9 $ccPid
            printLog "sleep 1 secconds...."
            sleep 1
            if [ $(ps -ef | grep "cn.utstarcom.algorithmtest.AlgorithmtestApplication" | grep -v grep | wc -l) -eq "0" ] && [ $(ps -ef | grep "algorithmtest_monitor.py" | grep -v grep | wc -l) -eq "0" ]; then
                break
            fi
        done
        printLog "the algorithmtest stop completly."
    fi
else
    printLog "the algorithmtest not run!"
fi
