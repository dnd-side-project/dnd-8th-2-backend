#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> 현재 실행중인 WAS가 존재하지 않습니다."
  exit 1
fi

echo "> 스위칭 될 $TARGET_PORT 포트에 대해 WAS health check을 시작합니다."
echo "> 'http://reet-place.shop:${TARGET_PORT}' ..."

for RETRY_COUNT in 1 2 3 4 5 6 7 8 9 10
do
    echo "> #${RETRY_COUNT} trying..."
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}"  http://reet-place.shop:${TARGET_PORT}/actuator/health)

    if [ ${RESPONSE_CODE} -eq 200 ]; then
        echo "> WAS가 정상적으로 동작합니다."
        exit 0
    elif [ ${RETRY_COUNT} -eq 10 ]; then
        echo "> Health Check에 실패했습니다."
        exit 1
    fi
    sleep 10
done