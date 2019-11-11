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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.multidex.MultiDex;

import java.io.File;
import java.io.IOException;

import static cn.hacktons.dexing.ProcessUtils.hasDexOpt;
import static cn.hacktons.dexing.ProcessUtils.obtainLock;


class NormalDexProcessor {

    void install(Context context) {
        boolean hasOptimized = hasDexOpt(context);
        if (hasOptimized) {
            DexLog.i("no need for optdex, install directly ");
            MultiDex.install(context);
            return;
        }
        DexLog.i("hang up process, waiting for dexopt");
        File lock = obtainLock(context);
        while (!lock.exists()) {
            try {
                lock.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DexLog.i("show dex install Activity");
        Intent intent = new Intent(context, DexInstallActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        while (lock.exists()) {
            DexLog.i("loop...");
            SystemClock.sleep(100);
        }
        MultiDex.install(context);
    }
}
