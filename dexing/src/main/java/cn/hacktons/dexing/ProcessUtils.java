/*
 *  Copyright 2019 Chaobin Wu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
import java.nio.charset.StandardCharsets;

class ProcessUtils {
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
                    StandardCharsets.ISO_8859_1));
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
