package ir.alimahdiyar.ictclogin;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class ictc_widget extends AppWidgetProvider {
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget1_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ictc_widget);
        views.setTextViewText(R.id.appwidget1_text, widgetText);
        Intent intent = new Intent(context, ictc_widget.class);
        intent.setAction("login");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.ictc_widget, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals("login")) {
            SharedPreferences sharedPref = context.getSharedPreferences("ir.alimahdiyar.ictclogin", Context.MODE_PRIVATE);
            String res = Ictclogin_functions.doLogin(sharedPref.getString("saved_username", ""), sharedPref.getString("saved_password", ""),sharedPref.getString("saved_username2", ""), sharedPref.getString("saved_password2", ""));
            Toast.makeText(context, res, Toast.LENGTH_LONG).show();
        }
    }
}

