package com.dnd.reetplace.app.dto.place.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.*;

@JsonNaming(SnakeCaseStrategy.class)
@Getter
public class SameName {
    private String[] region;
    private String keyword;
    private String selectedRegion;
}
