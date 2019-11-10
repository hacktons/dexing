package cn.hacktons.dexing;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtils {
    private static final String PREFERENCE_NAME = "dexopt";
    private static String name;

    static String getProcessName() {
        if (name != null) {
            return name;
        }
        String processName = null;
        BufferedReader cmdlineReader = null;
        try {
            cmdlineReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + android.os.Process.myPid() + "/cmdline"),
                    "iso-8859-1"));
            int c;
            StringBuilder builder = new StringBuilder();
            while ((c = cmdlineReader.read()) > 0) {
                builder.append((char) c);
            }
            builder.trimToSize();
            processName = builder.toString();
        } catch (Exception e) {
        } finally {
            if (cmdlineReader != null) {
                try {
                    cmdlineReader.close();
                } catch (IOException e) {
                }
            }
        }
        name = processName;
        return processName;
    }

    static boolean hasDexOpt(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String key;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            String name = info.versionName;
            key = name + "|" + version;
        } catch (PackageManager.NameNotFoundException e) {
            key = "_|_";
        }
        return sp.getBoolean(key, false);
    }

    static void saveDexOpt(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        String key;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            String name = info.versionName;
            key = name + "|" + version;
        } catch (PackageManager.NameNotFoundException e) {
            key = "_|_";
        }
        editor.putBoolean(key, true);
        editor.apply();
    }

    static File obtainLock(Context context) {
        return new File(context.getCacheDir(), "dexopt.lock");
    }

}
