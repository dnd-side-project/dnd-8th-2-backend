package com.dnd.reetplace.global.log.filter;

import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletResponse;

public class ResponseWrapper extends ContentCachingResponseWrapper {
    
    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }
}
