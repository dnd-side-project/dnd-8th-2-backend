package com.dnd.reetplace.app.dto.survey;

import com.dnd.reetplace.app.domain.Member;
import com.dnd.reetplace.app.domain.Survey;
import com.dnd.reetplace.app.type.SurveyType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SurveyDto {

    private SurveyType surveyType;
    private String description;

    public static SurveyDto of(SurveyType surveyType, String description) {
        return new SurveyDto(surveyType, surveyType.equals(SurveyType.OTHER) ? description : surveyType.getDescription());
    }

    public Survey toEntity(Member member) {
        return Survey.builder()
                .member(member)
                .surveyType(surveyType)
                .description(description)
                .build();
    }
}
