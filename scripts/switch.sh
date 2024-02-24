#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> 현재 Nginx가 프록싱하는 포트 : ${CURRENT_PORT}"

if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "> 현재 실행중인 WAS가 존재하지 않습니다."
    exit 1
fi

echo "set \$service_url http://3.38.66.143:${TARGET_PORT};" | tee /home/ubuntu/service_url.inc

echo "> 스위칭 후 Nginx가 프록싱하는 포트 : ${TARGET_PORT}"

# Reload nginx
sudo service nginx reload

echo "> Nginx reload"