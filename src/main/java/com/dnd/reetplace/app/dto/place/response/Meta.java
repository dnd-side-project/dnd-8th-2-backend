package com.dnd.reetplace.app.dto.place.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class Meta {
    private int totalCount;
    private int pageableCount;
    private Boolean isEnd;
    private SameName sameName;
}
