package com.refundplatform.policy.util;

public final class ExceptionMessageUtil {

    private static final int MAX_LENGTH =
            2000;

    private ExceptionMessageUtil() {
    }

    public static String safeMessage(
            Exception exception) {

        String message =
                exception.getMessage();

        if (message == null || message.isBlank()) {
            return exception
                    .getClass()
                    .getSimpleName();
        }

        return message.length() > MAX_LENGTH
                ? message.substring(
                        0,
                        MAX_LENGTH
                )
                : message;
    }
}
