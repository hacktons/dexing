package cn.hacktons.dexing;

import android.util.Log;

class DexLog {
    private static String TAG = "Dexing";
    static boolean sDisable = true;

    static void i(String text) {
        if (sDisable) {
            return;
        }
        Log.i(TAG, text);
    }
}
