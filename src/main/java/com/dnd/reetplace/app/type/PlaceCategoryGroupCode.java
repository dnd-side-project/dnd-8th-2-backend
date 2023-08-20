package com.dnd.reetplace.app.type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = """
        <p>카테고리 그룹 코드. 목록은 다음과 같음</p>
        <ul>
            <li><code>MT1</code> - 대형마트</li>
            <li><code>CS2</code> - 편의점</li>
            <li><code>PS3</code> - 어린이집, 유치원</li>
            <li><code>SC4</code> - 학교</li>
            <li><code>AC5</code> - 학원</li>
            <li><code>PK6</code> - 주차장</li>
            <li><code>OL7</code> - 주유소, 충전소</li>
            <li><code>SW8</code> - 지하철역</li>
            <li><code>BK9</code> - 은행</li>
            <li><code>CT1</code> - 문화시설</li>
            <li><code>AG2</code> - 중개업소</li>
            <li><code>PO3</code> - 공공기관</li>
            <li><code>AT4</code> - 관광명소</li>
            <li><code>AD5</code> - 숙박</li>
            <li><code>FD6</code> - 음식점</li>
            <li><code>CE7</code> - 카페</li>
            <li><code>HP8</code> - 병원</li>
            <li><code>PM9</code> - 약국</li>
        </ul>
        """,
        example = "CE7")
@AllArgsConstructor
@Getter
public enum PlaceCategoryGroupCode {

    MT1("대형마트"),
    CS2("편의점"),
    PS3("어린이집, 유치원"),
    SC4("학교"),
    AC5("학원"),
    PK6("주차장"),
    OL7("주유소, 충전소"),
    SW8("지하철역"),
    BK9("은행"),
    CT1("문화시설"),
    AG2("중개업소"),
    PO3("공공기관"),
    AT4("관광명소"),
    AD5("숙박"),
    FD6("음식점"),
    CE7("카페"),
    HP8("병원"),
    PM9("약국");

    private final String description;
}
