#!/usr/bin/env bash
echo "compiling project gateway"
cd ../../
mvn clean install -DskipTests
cd gateway-ha/target
mkdir -p presto_gateway
cp *.jar presto_gateway/
cp ../../deploy/script/gateway.sh presto_gateway/
tar -czf presto_gateway.tar.gz presto_gateway