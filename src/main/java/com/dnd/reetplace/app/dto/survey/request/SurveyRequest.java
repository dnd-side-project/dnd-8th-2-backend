package com.dnd.reetplace.app.dto.survey.request;

import com.dnd.reetplace.app.dto.survey.SurveyDto;
import com.dnd.reetplace.app.type.SurveyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class SurveyRequest {

    @Schema(description = "설문 선택항목", example = "RECORD_DELETE", required = true)
    @NotEmpty
    private SurveyType surveyType;

    @Schema(description = "설문 선택항목으로 '기타'를 선택한 경우의 탈퇴 이유", example = "~~한 이유로 인한 탈퇴")
    private String description;

    public SurveyDto toDto() {
        return SurveyDto.of(surveyType, description);
    }
}
