package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
