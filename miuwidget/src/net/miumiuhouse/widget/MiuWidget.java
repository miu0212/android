package net.miumiuhouse.widget;

import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MiuWidget extends AppWidgetProvider {
    private static final String TIME_ACTION = "TIME_ACTION";
    private static final String PAST_ACTION = "PAST_ACTION";
    
    private static final String TAG_ = "MiuWidget";
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }
    
    @Override
    public void onDeleted(Context context, int apWidgetIds[]) {
        Log.d(TAG_, "onDeleted");
    }
    
    @Override
    public void onDisabled(Context context) {
        Log.d(TAG_, "onDisabled");
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        super.onReceive(context, intent);
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_DELETED)) {
            Toast.makeText(context, "delete", Toast.LENGTH_LONG).show();
        }
        else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Toast.makeText(context, "update", Toast.LENGTH_LONG).show();
        }
        else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_ENABLED)) {
            Toast.makeText(context, "enabled", Toast.LENGTH_LONG).show();
        }
        else if (action.equals(AppWidgetManager.ACTION_APPWIDGET_DELETED)) {
            Toast.makeText(context, "deleted", Toast.LENGTH_LONG).show();
        }
    }
    
    public void updateWidget(Context context, AppWidgetManager manager, int id) {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.main);
        
        Intent intentTime = new Intent(context, ButtonReceiver.class);
        intentTime.setAction(TIME_ACTION);
        intentTime.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        PendingIntent pendingIntentTime = PendingIntent.getBroadcast(context, id, intentTime, 0);
        
        remoteView.setOnClickPendingIntent(R.id.timeButton, pendingIntentTime);
        
        Intent intentPast = new Intent(context, ButtonReceiver.class);
        intentTime.setAction(PAST_ACTION);
        intentTime.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        PendingIntent pendingIntentPast = PendingIntent.getBroadcast(context, id, intentPast, 0);
        
        remoteView.setOnClickPendingIntent(R.id.pastButton, pendingIntentPast);
        
        manager.updateAppWidget(id, remoteView);
        
    }
    
    public static class ButtonReceiver extends BroadcastReceiver {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.main);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, MiuWidget.class);
            
            if (TIME_ACTION.equals(intent.getAction())) {
                remoteView.setTextViewText(R.id.dispText, new Date().toLocaleString());
            }
            else if (PAST_ACTION.equals(intent.getAction())) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm.getText().equals("")) {
                    cm.setText("クリップボードに値がコピーされていません。");
                }
                remoteView.setTextViewText(R.id.dispText, cm.getText());
            }
            
            manager.updateAppWidget(thisWidget, remoteView);
            
            Bundle extras = intent.getExtras();
            if (extras != null) {
                extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
            }
            
            int id = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            manager.updateAppWidget(id, remoteView);
        }
    }
}
