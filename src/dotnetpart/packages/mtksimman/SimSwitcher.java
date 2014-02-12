package dotnetpart.packages.mtksimman;

import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.provider.Settings;

public class SimSwitcher {
	public static void switchSim(ContextWrapper wrapper, ContentResolver resolver, int simNumber)
    {
    	Settings.System.putInt(resolver, "dual_sim_mode_setting", simNumber);
        Intent localIntent = new Intent("android.intent.action.DUAL_SIM_MODE");
        localIntent.putExtra("mode", simNumber);
        wrapper.sendBroadcast(localIntent);
    }
}
