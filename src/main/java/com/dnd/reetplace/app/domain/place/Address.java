package com.dnd.reetplace.app.domain.place;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Address {

    @Column(nullable = false)
    private String sido;

    @Column(nullable = false)
    private String sgg;

    @Column(nullable = false)
    private String lotNumberAddress;

    @Column(nullable = false)
    private String roadAddress;

    public Address(String lotNumberAddress, String roadAddress) {
        int sidoIdx = roadAddress.indexOf(" ");
        int sggIdx = roadAddress.indexOf(" ", sidoIdx + 1);

        this.sido = roadAddress.substring(0, sidoIdx);
        this.sgg = roadAddress.substring(sidoIdx + 1, sggIdx);
        this.lotNumberAddress = lotNumberAddress.substring(sggIdx + 1);
        this.roadAddress = roadAddress.substring(sggIdx + 1);
    }
}
