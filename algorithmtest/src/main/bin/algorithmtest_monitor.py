#!/usr/bin/python

import urllib2
import os
import sys
import time
import subprocess
import commands
import logging
import json


def parseStatus(content):
    jsonMap = json.loads(content)
    serviceStatus = jsonMap.get('serviceStatus')
    return serviceStatus if serviceStatus != None else -1


def getIpPort(confFile):
    ip = '127.0.0.1'
    port = 9999
    count = 0
    file = open(confFile)
    line = file.readline()
    while line:
        arrs = line.replace("\r\n", "").replace("\n", "").split(":")
        if (arrs[0] == "  address"):
            ip = arrs[1].strip()
            if ip == '0.0.0.0':
                ip = '127.0.0.1'
            count += 1
        if (arrs[0] == "  port"):
            port = arrs[1].strip()
            count += 1
        if count == 2:
            break
        line = file.readline()
    file.close()
    return ip, port


def postHeartBeat(url, headers, data):
    postReq = urllib2.Request(url=url, headers=headers, data=data)
    try:
        response = urllib2.urlopen(postReq, timeout=10)
        status = response.getcode()
        if (status != 200):
            logging.error("postHeartBeat the status: %d" % (status))
            return -1
        else:
            return parseStatus(response.read())
    except Exception, e:
        logging.error("postHeartBeat generate exception: %s" % (e))
        return -1


def restartAlgorithmtest(algorithmtestHome):
    os.chdir(algorithmtestHome + "/bin")
    logging.info("start to run restartAlgorithmtest. curr dir: %s" %
                 (os.getcwd()))
    subprocess.Popen('nohup ./restart_algorithmtest.sh &', shell='true')
    logging.info("end to run restartAlgorithmtest. cur dir: %s" % (os.getcwd()))
    sys.exit()


def runTest(postUrl, postHeaders, postCotent):
    exceptCount = 0
    while True:
        if (postHeartBeat(postUrl, postHeaders, postCotent) == 0):
            exceptCount = 0
        else:
            exceptCount += 1
            logging.info("postHeartBeat exception, exceptCount: %s" %
                         (exceptCount))

        algorithmtestCount = commands.getoutput(
            "ps -ef | grep 'cn.utstarcom.algorithmtest.AlgorithmtestApplication' | grep -v grep | wc -l")
        if (exceptCount > 6 or algorithmtestCount != "1"):
            logging.info(
                "monitor algorithmtest run excepted, so restartAlgorithmtest. exceptCount: %s algorithmtestCount: %s" % (
                    exceptCount, algorithmtestCount))
            restartAlgorithmtest(algorithmtestHome)

        time.sleep(10)


# set log format
logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)-8s %(levelname)-5s %(message)s')

if __name__ == "__main__":

    logging.info("start to run algorithmtest_monitor.")
    logging.info("first, sleep 300 seconds...")
    time.sleep(300)
    logging.info("sleep compeleted, continue...")

    algorithmtestHome = os.getenv("ALGORITHMTEST_HOME")
    if (not os.path.exists(algorithmtestHome)):
        logging.info(
            "algorithmtest run dir: don't exists! algorithmtest_monitor exit!" % (algorithmtestHome))
        sys.exit()
    logging.info("algorithmtest home: %s" % (algorithmtestHome))

    confFile = algorithmtestHome + "/config/algorithmtest.yml"
    (ip, port) = getIpPort(confFile)
    postUrl = "http://" + ip + ":" + port + "/api/v1/ne-heartbeat"
    logging.info("algorithmtest heartbeat post url: %s" % (postUrl))
    postHeaders = {'Content-Type': 'application/json'}
    postCotent = '{"message": "NeHeartBeat"}'
    runTest(postUrl, postHeaders, postCotent)
