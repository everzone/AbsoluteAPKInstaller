package net.everzone.AbsoluteAPKInstaller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.util.Log;

public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent == null)
            return;

        int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -1);

        switch(status) {
            case PackageInstaller.STATUS_PENDING_USER_ACTION:
                Intent activityIntent = intent.getParcelableExtra(Intent.EXTRA_INTENT);
                context.startActivity(activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case PackageInstaller.STATUS_SUCCESS:
                if(MainActivity.instance != null)
                    MainActivity.instance.onInstallComplete();
                break;
            default:
                MainActivity.instance.onInstallFailed();
                String msg = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
                Log.e("!", String.format("received %d and %s", status, msg));
                break;
        }
    }
}
