package cn.hacktons.dexing;

import android.app.Activity;
import android.os.Process;
import android.os.SystemClock;
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
        final double start = SystemClock.currentThreadTimeMillis();
        Activity context = mRef.get();
        if (context == null) {
            return;
        }
        DexLog.i("execute install");
        MultiDex.install(context);
        DexLog.i("install success, release lock");
        obtainLock(context).delete();
        saveDexOpt(context);
        DexLog.i("remove activity");
        context.finish();
        double diff = SystemClock.currentThreadTimeMillis() - start;
        DexLog.i("time consumed => " + (diff / 1000.0) + "s");
        android.os.Process.killProcess(Process.myPid());
    }
}
