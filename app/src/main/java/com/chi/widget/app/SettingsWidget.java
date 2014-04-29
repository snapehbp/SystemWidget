package com.chi.widget.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

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
        }
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
