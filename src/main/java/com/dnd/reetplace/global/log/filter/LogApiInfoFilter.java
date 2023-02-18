package com.dnd.reetplace.global.log.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.dnd.reetplace.global.log.LogUtils.*;

@Slf4j
@Component
public class LogApiInfoFilter extends OncePerRequestFilter {

    private static final String[] LOG_BLACK_LIST = {
            "/swagger",
            "/v3/api-docs"
    };

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml"),
            MediaType.MULTIPART_FORM_DATA
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        setLogTraceId();

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(new RequestWrapper(request), new ResponseWrapper(response), filterChain);
        }

        removeLogTraceId();
    }

    private void doFilterWrapped(
            RequestWrapper request,
            ContentCachingResponseWrapper response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        boolean doLog = Arrays.stream(LOG_BLACK_LIST).noneMatch(request.getRequestURI()::contains);

        try {
            if (doLog) logRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            if (doLog) logResponse(response);
            response.copyBodyToResponse();
        }
    }

    private void logRequest(RequestWrapper request) throws IOException {
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
            uri += "?" + queryString;
        }

        log.info(
                "[{}] Request: [{}] uri={}, content-type={}, content-length={}",
                getLogTraceId(),
                request.getMethod(),
                uri,
                request.getContentType(),
                request.getContentLength()
        );

        logPayload("Request", request.getContentType(), request.getInputStream());
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        logPayload("Response", response.getContentType(), response.getContentInputStream());
    }

    private void logPayload(String prefix, String contentType, InputStream inputStream) throws IOException {
        if (contentType == null) {
            contentType = MediaType.APPLICATION_JSON_VALUE;
        }

        if (!isVisible(MediaType.valueOf(contentType))) {
            log.info("[{}] {} Payload: Binary Content", getLogTraceId(), prefix);
            return;
        }

        byte[] content = StreamUtils.copyToByteArray(inputStream);
        if (ObjectUtils.isEmpty(content)) {
            return;
        }

        String contentString = new String(content);
        // TODO: application/json type 이외의 경우에는 어떻게 출력해야 할 지 추후 확인 필요
        if (contentType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            contentString = contentString.replaceAll("\n *", "").replaceAll(",", ", ");
        }
        log.info("[{}] {} Payload: {}", getLogTraceId(), prefix, contentString);
    }

    private boolean isVisible(MediaType mediaType) {
        return VISIBLE_TYPES.stream()
                .anyMatch(visibleType -> visibleType.includes(mediaType));
    }
}
