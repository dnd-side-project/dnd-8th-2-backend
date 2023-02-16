package com.dnd.reetplace.global.log;

import org.slf4j.MDC;

import java.util.UUID;

public class LogUtils {

    public static final String LOG_TRACE_ID = "ReetPlaceLogTraceId";

    public static String getLogTraceId() {
        return MDC.get(LOG_TRACE_ID);
    }

    public static void setLogTraceId() {
        MDC.put(LOG_TRACE_ID, UUID.randomUUID().toString().substring(0, 8));
    }
}
