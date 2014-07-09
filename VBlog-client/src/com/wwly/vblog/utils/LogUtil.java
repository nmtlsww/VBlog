package com.wwly.vblog.utils;

import android.util.Log;

/**
 * Log工具类
 * 
 */
public class LogUtil
{
    private final static int VERBOSE = 0;
    private final static int DEBUG = 1;
    private final static int INFO = 2;
    private final static int WARN = 3;
    private final static int ERROR = 4;

    private static int mDefaultLogLevel = DEBUG;

    private static boolean mLoggabel = false;

    private static boolean mIsPrintAssist = false;

    public static void d(String tag, String str)
    {
        if (mLoggabel)
        {
            Log.d(tag, getLogMsg(str));
        }
    }

    public static void d(String str)
    {
        if (mLoggabel)
        {
            Log.d(getTagName(), getLogMsg(str));
        }
    }

    public static void w(String tag, String str, Throwable tr)
    {
        if (mLoggabel)
        {
            Log.w(tag, getLogMsg(str), tr);
        }
    }

    public static void w(String tag, String str)
    {
        if (mLoggabel)
        {
            Log.w(tag, getLogMsg(str));
        }
    }

    public static void w(String str)
    {
        if (mLoggabel)
        {
            Log.w(getTagName(), getLogMsg(str));
        }
    }

    public static void e(String tag, String str, Throwable tr)
    {
        if (mLoggabel)
        {
            Log.e(tag, getLogMsg(str), tr);
        }
    }

    public static void e(String tag, String str)
    {
        if (mLoggabel)
        {
            Log.e(tag, getLogMsg(str));
        }
    }

    public static void e(String str)
    {
        if (mLoggabel)
        {
            Log.e(getTagName(), getLogMsg(str));
        }
    }

    public static void i(String tag, String str)
    {
        if (mLoggabel)
        {
            Log.i(tag, getLogMsg(str));
        }
    }

    public static void i(String str)
    {
        if (mLoggabel)
        {
            Log.i(getTagName(), getLogMsg(str));
        }
    }

    public static void v(String tag, String str)
    {
        if (mLoggabel)
        {
            Log.v(tag, getLogMsg(str));
        }
    }

    public static void v(String str)
    {
        if (mLoggabel)
        {
            Log.v(getTagName(), getLogMsg(str));
        }
    }

    public static void log(String str)
    {
        String tag = getTagName();
        String msg = getLogMsg(str);

        switch (mDefaultLogLevel)
        {
        case VERBOSE:
            Log.v(tag, msg);
            break;
        case DEBUG:
            Log.d(tag, msg);
            break;
        case INFO:
            Log.i(tag, msg);
            break;
        case WARN:
            Log.w(tag, msg);
            break;
        case ERROR:
            Log.e(tag, msg);
            break;
        default:
            Log.d(tag, msg);
            break;
        }
    }

    public static void log(String str, Throwable tr)
    {
        String tag = getTagName();
        String msg = getLogMsg(str);

        switch (mDefaultLogLevel)
        {
        case VERBOSE:
            Log.v(tag, msg, tr);
            break;
        case DEBUG:
            Log.d(tag, msg, tr);
            break;
        case INFO:
            Log.i(tag, msg, tr);
            break;
        case WARN:
            Log.w(tag, msg, tr);
            break;
        case ERROR:
            Log.e(tag, msg, tr);
            break;
        default:
            Log.d(tag, msg, tr);
            break;
        }
    }

    public static void setDefaultLogLevel(int level)
    {
        mDefaultLogLevel = level;
    }

    public static void setIsPrintAssist(boolean isPrintAssist)
    {
        mIsPrintAssist = isPrintAssist;
    }

    public static void setLoggable(boolean isDebug)
    {
        mLoggabel = isDebug;
    }

    private static String getTagName()
    {
        StackTraceElement stackTraceElement = new Throwable().fillInStackTrace().getStackTrace()[2];

        String className = stackTraceElement.getClassName();
        if (className.contains("$"))
        {
            className = className.split("\\$")[1];
        }
        else if (className.contains("."))
        {
            className = className.split("\\.")[className.split("\\.").length - 1];
        }

        return className;
    }

    private static String getLogMsg(String msg)
    {
        if (!mIsPrintAssist) return msg;

        StackTraceElement stackTraceElement = new Throwable().fillInStackTrace().getStackTrace()[2];
        return new StringBuilder().append("{")
            .append("Thread:" + Thread.currentThread().getName() + ",")
            .append(stackTraceElement.getClassName() + ":" + stackTraceElement.getMethodName()
                    + ":" + stackTraceElement.getLineNumber())
            .append("} - ")
            .append(msg).toString();
    }
}
