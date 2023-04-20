package com.dnd.reetplace.app.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScrapService {

    private static final String PRINT_PAGE_BASE_URL = "https://place.map.kakao.com/placePrint.daum?confirmid=";
    private static final String DEFAULT_IMAGE_URL = "https://climate.onep.go.th/wp-content/uploads/2020/01/default-image.jpg";

    /**
     * kakaoPid에 해당하는 장소의 thumbnail image url을 반환한다.
     *
     * @param kakaoPid thumbnail image를 추출할 장소의 고유 id
     * @return thumbnail image url
     */
    public String getPlaceThumbnailUrl(String kakaoPid) {
        Document doc;
        try {
            doc = Jsoup.connect(PRINT_PAGE_BASE_URL + kakaoPid).get();
        } catch (IOException ex) {
            return DEFAULT_IMAGE_URL;
        }

        Element thumbnailImage = doc.selectFirst("div.kakaomap_popup div.popup_body div.thumb_info img.thumb_g");
        return thumbnailImage != null ? thumbnailImage.absUrl("src") : DEFAULT_IMAGE_URL;
    }
}
