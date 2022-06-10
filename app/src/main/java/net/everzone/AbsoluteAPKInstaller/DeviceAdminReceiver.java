package net.everzone.AbsoluteAPKInstaller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.e("Device Admin: ", "Enabled");
    }
    @Override
    public String onDisableRequested(Context context, Intent intent) {
        return "Admin disable requested";
    }
    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.e("Device Admin: ", "Disabled");
    }
    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        super.onPasswordChanged(context, intent);
        Log.e("Device Admin: ", "Password changed");
    }
}
