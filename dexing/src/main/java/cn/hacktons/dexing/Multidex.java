package cn.hacktons.dexing;

import android.app.Application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Multidex {
    private static final String NODEX_PROCESS_NAME = ":nodex";
    private static final boolean IS_VM_MULTIDEX_CAPABLE = isVMMultidexCapable(System.getProperty("java.vm.version"));

    static int sLayoutId;

    public static void install(Application context) {
        install(context, R.layout.activity_dex_opt);
    }

    public static void install(Application context, int layout) {
        if (IS_VM_MULTIDEX_CAPABLE) {
            return;
        }
        if (layout <= 0) {
            throw new RuntimeException("layout is not valid");
        }
        sLayoutId = layout;
        String name = ProcessUtils.getProcessName();
        DexLog.i("process name => " + name);
        boolean isDexOptProcess = name.equals(context.getApplicationInfo().processName + NODEX_PROCESS_NAME);
        if (isDexOptProcess) {
            return;
        }
        new NormalDexProcessor().install(context);
    }

    public static void enableLog() {
        DexLog.sDisable = false;
    }

    static boolean isVMMultidexCapable(String versionString) {
        boolean isMultidexCapable = false;
        if (versionString != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
            if (matcher.matches()) {
                try {
                    int major = Integer.parseInt(matcher.group(1));
                    int minor = Integer.parseInt(matcher.group(2));
                    isMultidexCapable = major > 2 || major == 2 && minor >= 1;
                } catch (NumberFormatException var5) {
                }
            }
        }

        DexLog.i("VM with version " + versionString + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
        return isMultidexCapable;
    }
}
