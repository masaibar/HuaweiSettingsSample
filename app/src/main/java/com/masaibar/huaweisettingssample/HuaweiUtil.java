package com.masaibar.huaweisettingssample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * https://stackoverflow.com/questions/31638986/protected-apps-setting-on-huawei-phones-and-how-to-handle-it
 */
public class HuaweiUtil {

    private static final String PACKAGE_NAME_HUAWEI_SYSTEM_MANAGER = "com.huawei.systemmanager";
    private static final String ACTIVITY_NAME_HUAWEI_PROTECT_ACTIVITY =
            ".optimize.process.ProtectActivity";

    private Context mContext;

    public HuaweiUtil(Context context) {
        mContext = context;
    }

    public boolean hasProtectedAppSetting() {
        Intent intent = new Intent();
        intent.setClassName(
                PACKAGE_NAME_HUAWEI_SYSTEM_MANAGER,
                String.format("%s%s",
                        PACKAGE_NAME_HUAWEI_SYSTEM_MANAGER, ACTIVITY_NAME_HUAWEI_PROTECT_ACTIVITY)
        );

        List<ResolveInfo> resolveInfos = mContext.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfos.size() > 0;
    }

    public void openProtectedSettings() {
        try {
            String cmd = String.format(
                    "am start -n %s/%s",
                    PACKAGE_NAME_HUAWEI_SYSTEM_MANAGER,
                    ACTIVITY_NAME_HUAWEI_PROTECT_ACTIVITY
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd = String.format("%s --user %s", cmd, getUserSerial());
            }

            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private String getUserSerial() {
        Object userManager = mContext.getSystemService("user");
        if (userManager == null) {
            return "";
        }

        try {
            Method myUserHandleMethod =
                    android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle =
                    myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser =
                    userManager.getClass()
                            .getMethod("getSerialNumberForUser", myUserHandle.getClass());
            long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);

            return String.valueOf(userSerial);

        } catch (NoSuchMethodException | IllegalArgumentException |
                InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }
}
