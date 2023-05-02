package com.dnd.reetplace.integration.app.service;

import com.dnd.reetplace.app.service.ScrapService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ScrapServiceTest {

    private final ScrapService sut;

    public ScrapServiceTest(@Autowired ScrapService sut) {
        this.sut = sut;
    }

    @DisplayName("Kakao place id가 주어지고, scraping을 수행하면, 장소의 thumbnail image url을 반환한다.")
    @Test
    void givenKakaoPid_whenScraping_thenReturnPlaceThumbnailUrl() {
        // given
        String kakaoPid = "1879186093";

        // when
        String thumbnailUrl = sut.getPlaceThumbnailUrl(kakaoPid);

        // then
        assertThat(thumbnailUrl).isNotBlank();
    }
}