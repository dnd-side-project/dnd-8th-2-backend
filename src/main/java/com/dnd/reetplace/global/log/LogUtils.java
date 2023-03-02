package com.dnd.reetplace.global.log;

import org.slf4j.MDC;

import java.util.UUID;

public class LogUtils {

    public static final String LOG_TRACE_ID_MDC_KEY = "ReetPlaceLogTraceId";

    public static String getLogTraceId() {
        return MDC.get(LOG_TRACE_ID_MDC_KEY);
    }

    public static void setLogTraceId() {
        MDC.put(LOG_TRACE_ID_MDC_KEY, UUID.randomUUID().toString().substring(0, 8));
    }

    public static void removeLogTraceId() {
        MDC.remove(LOG_TRACE_ID_MDC_KEY);
    }
}
