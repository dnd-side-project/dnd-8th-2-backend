package com.dnd.reetplace.app.domain;

import com.dnd.reetplace.app.domain.common.BaseTimeEntity;
import com.dnd.reetplace.app.type.SurveyType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Survey extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private SurveyType surveyType;

    @Column(nullable = false)
    private String description;

    @Builder
    public Survey(Member member, SurveyType surveyType, String description) {
        this.member = member;
        this.surveyType = surveyType;
        this.description = surveyType.equals(SurveyType.OTHER) ? description : surveyType.getDescription();
    }
}
