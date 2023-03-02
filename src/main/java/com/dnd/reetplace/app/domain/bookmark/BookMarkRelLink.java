package com.dnd.reetplace.app.domain.bookmark;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Embeddable
public class BookMarkRelLink {

    private String relLink1;
    private String relLink2;
    private String relLink3;
}
