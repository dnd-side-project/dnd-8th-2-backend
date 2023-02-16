#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PROFILE=prod1
TARGET_PORT=0

echo "> 현재 실행중인 포트번호 : ${CURRENT_PORT}"

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PROFILE=prod2
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PROFILE=prod1
  TARGET_PORT=8081
else
  echo "> 현재 실행중인 WAS가 존재하지 않습니다."
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo ">${TARGET_PROFILE} 프로필에 해당하는 WAS를 종료합니다."
  sudo kill ${TARGET_PID}
fi

nohup java -jar -Dspring.profiles.active=${TARGET_PROFILE} /home/ubuntu/reetplace/build/libs/* > /home/ubuntu/nohup.out 2>&1 &
echo "> ${TARGET_PROFILE} 프로필에 해당하는 WAS가 새로 실행됩니다."
exit 0