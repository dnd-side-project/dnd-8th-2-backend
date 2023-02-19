package com.dnd.reetplace.app.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI reetPlaceApi(@Value("reet-place.app.version") String appVersion) {
        // TODO: 로그인 기능 구현 후 Swagger에서 access-token을 header에 첨부할 수 있도록 security scheme component 추가 필요.
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Reet-Place API Docs")
                                .description("DND 8기 2조 앱 프로젝트 Reet-Place의 API 명세서")
                                .version(appVersion)
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Team Reet-Place GitHub Repository")
                                .url("https://github.com/dnd-side-project/dnd-8th-2-backend")
                );
    }
}
