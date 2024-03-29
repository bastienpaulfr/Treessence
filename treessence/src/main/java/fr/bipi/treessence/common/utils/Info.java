package fr.bipi.treessence.common.utils;

import java.lang.reflect.Method;

import timber.log.Timber;

/**
 * Utility class to get some information (method name, thread, etc.)
 */
public final class Info {

    private static final Method GET_ST;
    private static int sDepth;

    static {
        Method m = null;
        try {
            m = Throwable.class.getDeclaredMethod("getStackTraceElement", int.class);
            m.setAccessible(true);
            sDepth = 2;
        } catch (Exception ignore) {
            sDepth = 3;
        } finally {
            GET_ST = m;
        }
    }

    private Info() {
    }

    /**
     * @return A String containing some thread information
     */
    public static String getThreadInfoString() {
        Thread t = Thread.currentThread();
        return t + ", id : " + t.getId();
    }

    /**
     * @return A String containing more thread information
     */
    public static String getThreadFullInfoString() {
        Thread t = Thread.currentThread();
        //noinspection StringBufferReplaceableByString
        StringBuilder sb = new StringBuilder();
        sb.append("Thread : ").append(t)
            .append(", ")
            .append("id : ").append(t.getId())
            .append(", ")
            .append("state : ").append(t.getState())
            .append(", ")
            .append("alive : ").append(t.isAlive())
            .append(", ")
            .append("interrupted : ").append(t.isInterrupted());
        return sb.toString();
    }

    /**
     * Get the name of the calling method
     *
     * @return Name of the calling method
     */
    public static String getMethodName() {
        return getMethodName(sDepth);
    }

    /**
     * Get the name of the calling method
     *
     * @param depth depth
     * @return Name of the calling method
     */
    public static String getMethodName(int depth) {
        StackTraceElement element;
        try {
            if (GET_ST != null) {
                element = (StackTraceElement) GET_ST.invoke(new Throwable(), depth);
            } else {
                element = getStackTraceElement(depth);
            }
            if (element != null) {
                return element.getMethodName();
            } else {
                return "";
            }
        } catch (Exception e) {
            Timber.e(e.toString());
        }
        return "";
    }

    public static StackTraceElement getStackTraceElement(int depth) {
        return new Throwable().getStackTrace()[depth];
    }

}
