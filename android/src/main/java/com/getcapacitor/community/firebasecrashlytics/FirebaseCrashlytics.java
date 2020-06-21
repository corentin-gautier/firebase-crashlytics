package com.getcapacitor.community.firebasecrashlytics;

import android.Manifest;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin(
    permissions = {
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.WAKE_LOCK
    }
)
public class FirebaseCrashlytics extends Plugin {
  
  public final static String TAG = "FirebaseCrashlytics";

    @Override
    public void load() {
        super.load();
    }

    @PluginMethod()
    public void crash(PluginCall call) {
        if (call.hasOption("message")) {
            call.success();
            throw new RuntimeException(call.getString("message"));
        } else {
            call.reject("message is missing.");
        }
    }

    @PluginMethod()
    public void setContext(PluginCall call) {
        try {
          if (call.hasOption("key") && call.hasOption("value")) {
            String key = call.getString("key");
            String type = call.getString("type", "string");

            switch(type) {
              case "string":
                com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().setCustomKey(key, call.getString("value"));
                break;

              case "long":
                com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().setCustomKey(key, Long.valueOf(call.getInt("value")));
                break;

              case "int":
                com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().setCustomKey(key, call.getInt("value"));
                break;

              case "boolean":
                com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().setCustomKey(key, call.getBoolean("value"));
                break;

              case "float":
                com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().setCustomKey(key, call.getFloat("value"));
                break;

              case "double":
                com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().setCustomKey(key, call.getDouble("value"));
                break;
            }
          } else {
            call.reject("key or value is missing.");
          }
        } catch (Exception ex) {
          Log.e(TAG, "Caught exception while setting a crashlytics context: " + ex.getLocalizedMessage());
        }
    }

    @PluginMethod()
    public void setUserId(PluginCall call) {
        if (call.hasOption("userId")) {
            String userId = call.getString("userId");

            com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().setUserId(userId);
            call.success();
        } else {
            call.reject("userId is missing.");
        }
    }

    @PluginMethod()
    public void addLogMessage(PluginCall call) {
        if (call.hasOption("message")) {
            String message = call.getString("message");

            com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().log(message);
            call.success();
        }
    }

    @PluginMethod()
    public void setEnabled(PluginCall call) {
        if (call.hasOption("enabled")) {
            Boolean enabled = call.getBoolean("enabled");

            com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(enabled);
            call.success();
        }
    }
}
