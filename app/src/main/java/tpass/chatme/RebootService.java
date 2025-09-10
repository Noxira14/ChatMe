
package tpass.chatme;

import android.content.*;

public class RebootService extends BroadcastReceiver {

    @Override
    public void onReceive(Context myContext, Intent myIntent) {
        
        if (Intent.ACTION_BOOT_COMPLETED.equals(myIntent.getAction())) {
            Intent serviceIntent = new Intent(myContext, notiservice.class);
            myContext.startService(serviceIntent);
        }
        
    }
}

