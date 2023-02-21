package com.dnd.reetplace.app.config;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Profile("test")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    /**
     * 여러 스프링 컨텍스트가 실행되면 10000 포트가 충돌이 나 Redis 테스트가 진행되지 않는다.
     * 10000 포트에 대한 실행 여부를 확인한다.
     * 1. 실행중이 아닐 경우 10000 포트를 부여하여 Redis Server를 시작한다.
     * 2. 실행중일 경우 10001 ~ 65535 포트 중 사용하지 않는 포트를 부여하여 Redis Server를 시작한다.
     */
    @PostConstruct
    public void redisServer() throws IOException {
        int port = isRedisRunning()? findAvailablePort() : redisPort;
        redisServer = new RedisServer(port);
        redisServer.start();
    }

    /**
     * PreDestroy를 사용하여 Bean이 소멸되기 전, 동작중인 Redis Server를 종료한다.
     */
    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    /**
     * Redis가 현재 실행중인지 확인한다.
     */
    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }


    /**
     * 10001 ~ 65535 포트 중 사용가능한 포트번호를 반환한다.
     *
     * @return 사용 가능한 포트번호
     */
    public int findAvailablePort() throws IOException {
        for (int port = 10001; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    /**
     * 해당 port를 사용중인 프로세스를 확인하는 sh를 실행한다.
     */
    private Process executeGrepProcessCommand(int port) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    /**
     * 해당 Process가 현재 실행중인지 확인한다.
     */
    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }

        return !StringUtils.isEmpty(pidInfo.toString());
    }
}
