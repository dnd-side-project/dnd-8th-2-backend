package com.dnd.reetplace.app.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SurveyType {

    RECORD_DELETE("기록 삭제 목적"),
    LOW_USED("사용 빈도가 낮아서"),
    USE_OTHER_SERVICE("다른 서비스 사용 목적"),
    INCONVENIENCE_AND_ERRORS("이용이 불편하고 장애가 많아서"),
    CONTENT_DISSATISFACTION("콘텐츠 불만"),
    OTHER("기타");

    private final String description;
}
