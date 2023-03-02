package com.dnd.reetplace.global.exception.place;

import com.dnd.reetplace.global.exception.common.NotFoundException;

public class PlaceKakaoPidNotFoundException extends NotFoundException {

    public PlaceKakaoPidNotFoundException(String kakaoPid) {
        super("kakaoPid = " + kakaoPid);
    }
}
