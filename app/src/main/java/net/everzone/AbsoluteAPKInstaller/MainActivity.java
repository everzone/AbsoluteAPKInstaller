package net.everzone.AbsoluteAPKInstaller;

import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by admin@everzone.net on 6/11/2022.
 */

public class MainActivity extends AppCompatActivity {
    static MainActivity instance = null;
    static int PICK_FILE_REQUEST = 0x0126;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        {
            DevicePolicyManager tDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName tDevicePolicyAdmin = new ComponentName(this,
                    DeviceAdminReceiver.class);

            if(!tDevicePolicyManager.isAdminActive(tDevicePolicyAdmin)) { // Device admin also true, but to be Device Owner.
                Toast.makeText(getApplicationContext(), "I'm not the Device Owner!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FileChooser.class);
                i.putExtra(Constants.INITIAL_DIRECTORY, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
                i.putExtra(Constants.ALLOWED_FILE_EXTENSIONS, "apk");
                i.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
                startActivityForResult(i, PICK_FILE_REQUEST);
            }
        });
        button.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && data != null) {
            if (resultCode == RESULT_OK) {
                Uri file = data.getData();
                try {
                    installPackage(file.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean installPackage(String apkPath)
            throws IOException {
        String package_name = null;
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if(packageInfo != null) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            package_name= appInfo.packageName;
        }

        if(package_name == null) {
            Toast.makeText(this, "Install Failed : Can't get the package name", Toast.LENGTH_LONG).show();
            return false;
        }

        Toast.makeText(getApplicationContext(), "Start Installation", Toast.LENGTH_SHORT).show();

        PackageInstaller packageInstaller = getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        params.setAppPackageName(package_name);

        // set params
        int sessionId = packageInstaller.createSession(params);
        PackageInstaller.Session session = packageInstaller.openSession(sessionId);

        OutputStream out = session.openWrite(package_name, 0, -1);

        long sizeBytes = 0;
        File file = new File(apkPath);
        if (file.isFile())
            sizeBytes = file.length();

        int total = 0;
        FileInputStream fis = new FileInputStream(apkPath);
        byte[] buffer = new byte[65536];
        int c;
        while ((c = fis.read(buffer)) != -1) {
            total += c;
            out.write(buffer, 0, c);
        }
        session.fsync(out);
        fis.close();
        out.close();

        session.commit(createIntentSender(getApplicationContext(), sessionId));

        return true;
    }

    private static IntentSender createIntentSender(Context context, int sessionId) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                sessionId,
                new Intent(context, InstallReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent.getIntentSender();
    }

    public void onInstallComplete() {
        Toast.makeText(this, "Install Succeed!", Toast.LENGTH_LONG).show();
    }

    public void onInstallFailed() {
        Toast.makeText(this, "Install Failed!", Toast.LENGTH_LONG).show();
    }
}
