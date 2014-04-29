package com.chi.widget.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


public class SettingsWidget extends AppWidgetProvider {
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d("my_widget", "onDisabled");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("my_widget", "onEnabled ");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d("my_widget", "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("my_widget", "onUpdate " + Arrays.toString(appWidgetIds));
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    static void updateWidget(Context ctx, AppWidgetManager appWidgetManager, int widgetID) {

        Intent countIntent = new Intent(ctx, SettingsWidget.class);
        countIntent.setAction("wifi");
        countIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getBroadcast(ctx, widgetID, countIntent, 0);

        RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(), R.layout.activity_main);
        remoteViews.setOnClickPendingIntent(R.id.toggleWifi, pIntent);

        Intent mobileIntent = new Intent(ctx, SettingsWidget.class);
        mobileIntent.setAction("mobile").putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntentMobile = PendingIntent.getBroadcast(ctx, widgetID, mobileIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.toggleMobileInternet, pIntentMobile);


        appWidgetManager.updateAppWidget(widgetID, remoteViews);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equalsIgnoreCase("wifi")) {

            // извлекаем ID экземпляра
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                Log.e("widget", "set wifi on");
                wifiManager.setWifiEnabled(true);
            }
        } else if (intent.getAction().equalsIgnoreCase("mobile")) {
            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                try {
                    setMobileDataEnabled(context, true);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                Log.e("widget", "set mobile on");
            }
        }

    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }


}
