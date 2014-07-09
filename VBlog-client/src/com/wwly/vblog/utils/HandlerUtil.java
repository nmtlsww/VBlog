package com.wwly.vblog.utils;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil
{

    public static void runOnUiThread(Runnable action)
    {
        Looper mainLooper = Looper.getMainLooper();
        if (Thread.currentThread() == mainLooper.getThread())
        {
            // already UI thread, run it directly
            action.run();
        }
        else
        {
            // post the action on UI thread
            postOnUiThread(action);
        }
    }

    public static void postOnUiThread(Runnable action, long delayMillis)
    {
        if (delayMillis > 0)
        {
            new Handler(Looper.getMainLooper()).postDelayed(action, delayMillis);
        }
        else
        {
            postOnUiThread(action);
        }
    }

    public static void postOnUiThread(Runnable action)
    {
        new Handler(Looper.getMainLooper()).post(action);
    }

    public static void postOnUiThreadDelayed(Runnable action, long delayMillis)
    {
        new Handler(Looper.getMainLooper()).postDelayed(action, delayMillis);
    }

}
