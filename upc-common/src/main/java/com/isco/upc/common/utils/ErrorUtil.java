package com.isco.upc.common.utils;

public class ErrorUtil {

    /** The separation. */
    private static String SEPARATION = "\n\t-----------------------------------";

    /** The exception separation. */
    private static String EXCEPTION_SEPARATION = "\n\tException: ";

    /** The message separation. */
    private static String MESSAGE_SEPARATION = "\n\tMessage: ";

    /**
     * Extract exception message.
     * 
     * @param e
     *            the e
     * @return the string
     */
    public static String extractExceptionMessage(Throwable e) {

        if (e != null) {

            StringBuilder msg = new StringBuilder();
            msg.append("\n[System Error]: " + e.getClass().getName() + "\n");

            msg.append(SEPARATION);
            msg.append(EXCEPTION_SEPARATION + e.getClass().getName());
            msg.append(MESSAGE_SEPARATION + e.getMessage());
            extractExceptionDetails(e, msg);
            msg.append(SEPARATION);

            Throwable cause = e.getCause();
            while (cause != null) {
                msg.append(SEPARATION);
                msg.append(EXCEPTION_SEPARATION + cause.getClass().getName());
                msg.append(MESSAGE_SEPARATION + cause.getMessage());
                extractExceptionDetails(cause, msg);
                msg.append(SEPARATION);
                cause = cause.getCause();
            }

            return msg.toString();
        }

        return null;
    }

    /**
     * Extract exception details.
     * 
     * @param e
     *            the e
     * @param msg
     *            the message
     */
    private static void extractExceptionDetails(Throwable e, StringBuilder msg) {
        StackTraceElement[] stack = e.getStackTrace();
        if (stack != null && stack.length > 0) {
            msg.append("\n\tClass: " + stack[0].getClassName());
            msg.append("\n\tMethod: " + stack[0].getMethodName());
            msg.append("\n\tLine: " + stack[0].getLineNumber());
        }
    }

}
