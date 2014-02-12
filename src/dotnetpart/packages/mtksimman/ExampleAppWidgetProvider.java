package dotnetpart.packages.mtksimman;

import com.example.mtksimman.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.RemoteViews;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    private static final String SIM1_CLICKED    = "firstSimSwitchClick";
    private static final String SIM2_CLICKED    = "secondSimSwitchClick";
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews;
        ComponentName widget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        widget = new ComponentName(context, ExampleAppWidgetProvider.class);

        int currentState = Settings.System.getInt(context.getContentResolver(), "dual_sim_mode_setting", -1);
        if (currentState == 1 || currentState == 3)
        	remoteViews.setTextViewText(R.id.first_button, "on");
        else
        	remoteViews.setTextViewText(R.id.first_button, "off");
        
        if (currentState == 2 || currentState == 3)
        	remoteViews.setTextViewText(R.id.second_button, "on");
        else
        	remoteViews.setTextViewText(R.id.second_button, "off");
        
        remoteViews.setOnClickPendingIntent(R.id.first_button, getPendingSelfIntent(context, SIM1_CLICKED, appWidgetIds[0]));
        remoteViews.setOnClickPendingIntent(R.id.second_button, getPendingSelfIntent(context, SIM2_CLICKED, appWidgetIds[0]));
        appWidgetManager.updateAppWidget(widget, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if ("android.appwidget.action.APPWIDGET_UPDATE".equals(intent.getAction()))
    	{
        	super.onReceive(context, intent);
        	return;
    	}
        
        if (SIM1_CLICKED.equals(intent.getAction())) {
        	switchSim(1, context);
        	return;
        }
        
        else if (SIM2_CLICKED.equals(intent.getAction())){
        	switchSim(2, context);
        }
        
        if ("android.intent.action.DUAL_SIM_MODE".equals(intent.getAction())){
        	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        	
        	RemoteViews remoteViews;
            ComponentName watchWidget;
            
        	remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, ExampleAppWidgetProvider.class);

        	int mode = intent.getIntExtra("mode", -1);
        	switch (mode)
	        	{
		        	case 0:
		        		remoteViews.setTextViewText(R.id.first_button, "off");
		        		remoteViews.setTextViewText(R.id.second_button, "off");
		        		break;
		        	case 1:
		        		remoteViews.setTextViewText(R.id.first_button, "on");
		        		remoteViews.setTextViewText(R.id.second_button, "off");
		        		break;
		        	case 2:
		        		remoteViews.setTextViewText(R.id.first_button, "off");
		        		remoteViews.setTextViewText(R.id.second_button, "on");
		        		break;
		        	case 3:
		        		remoteViews.setTextViewText(R.id.first_button, "on");
		        		remoteViews.setTextViewText(R.id.second_button, "on");
		        		break;
	        	}
        	
        	appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        	return;
       }
    }

    private void switchSim(int simNumber, Context context)
    {
    	
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        RemoteViews remoteViews;
        ComponentName widget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        widget = new ComponentName(context, ExampleAppWidgetProvider.class);

        int currentState = Settings.System.getInt(context.getContentResolver(), "dual_sim_mode_setting", -1);
        int newState = 0;
        
        //Extract to state map
        if (currentState != simNumber && (currentState + simNumber) <= 3)
        	newState = currentState + simNumber;
        else
        	newState = currentState - simNumber;
        
        //remoteViews.setTextViewText(R.id.first_button, "TESTING");

    	Settings.System.putInt(context.getContentResolver(), "dual_sim_mode_setting", newState);
        Intent localIntent = new Intent("android.intent.action.DUAL_SIM_MODE");
        localIntent.putExtra("mode", newState);
        context.sendBroadcast(localIntent);
        
        //remoteViews.setOnClickPendingIntent(R.id.first_button, getPendingSelfIntent(context, SIM1_CLICKED));
        //remoteViews.setOnClickPendingIntent(R.id.second_button, getPendingSelfIntent(context, SIM2_CLICKED));
        
        appWidgetManager.updateAppWidget(widget, remoteViews);
    }
    
    protected PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetIds) {
		Intent launchIntent = new Intent();
		launchIntent.setClass(context, ExampleAppWidgetProvider.class);
		launchIntent.setAction(action);

		PendingIntent pi = PendingIntent.getBroadcast(context, appWidgetIds,
				launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		return pi;
    }
}