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
import android.os.Process;
import android.support.multidex.MultiDex;

import java.lang.ref.WeakReference;

import static cn.hacktons.dexing.ProcessUtils.obtainLock;
import static cn.hacktons.dexing.ProcessUtils.saveDexOpt;

public class Installer extends Thread {
    private WeakReference<Activity> mRef;

    Installer(Activity context) {
        this.mRef = new WeakReference<>(context);
    }

    @Override
    public void run() {
        final double start = System.currentTimeMillis();
        Activity context = mRef.get();
        if (context == null) {
            return;
        }
        DexLog.i("execute install...");
        MultiDex.install(context);
        DexLog.i("install success, release lock");
        obtainLock(context).delete();
        saveDexOpt(context);
        DexLog.i("remove activity");
        context.finish();
        double diff = System.currentTimeMillis() - start;
        DexLog.i("time consumed => " + (diff / 1000.0) + "s");
        android.os.Process.killProcess(Process.myPid());
    }
}
