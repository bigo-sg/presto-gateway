#!/usr/bin/env bash

cmd=$1
pid_path=$2
jar_path=$3
jvm_config=$4
config=$5
echo $@
export JAVA_HOME=/data/opt/jdk/presto/current
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH

if [ ${cmd} == "start" ]
then
    echo "nohup java -jar ${jvm_config} ${jar_path} server ${config} > /tmp/gateway.log 2>&1 &" > /data1/var/presto/prestoproxy/nohup.out
    nohup java -jar ${jvm_config} ${jar_path} server ${config} > /tmp/gateway.log 2>&1 &
    sleep 5
    pid=`ps -aux | grep gateway-ha|grep 'presto_gateway'|grep -v 'start'|awk '{print $2}'`
    rm ${pid_path}
    echo ${pid}>${pid_path}
elif [ ${cmd} == "stop" ]
then
    cat ${pid_path} | while read line
    do
        kill -9 ${line}
    done
fi
