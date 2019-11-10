package cn.hacktons.dexing;

import android.app.Activity;
import android.os.Bundle;

public class DexInstallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Multidex.sLayoutId);
        new Installer(this).start();
    }
}
