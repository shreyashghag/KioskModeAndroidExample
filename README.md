# KioskModeAndroidExample
This is an basic example how can you run an Android App in Kiosk Mode.i.e only your app would run on the device and also you cannot exit the app till to disable kiosk mode.

The one annoying prerequisite to setting a device owner is that the device needs to be ‘unprovisioned’. For me, this meant removing my associated Google account, provisioning the device, then re-adding my account. This may seem strange, but for external kiosks or enterprise users - this is a no-brainer, as those wouldn’t typically be hooked up to a Google account anyways.

For the DPM route to work, your app with its corresponding app ID needs to be installed on the device, and the app needs to have a ‘DeviceAdminReceiver’ setup.

Setting up the DeviceAdminReceiver is pretty simple. First, add a receiver to your AndroidManifest as below:
        
        
        <receiver
            android:name="com.android.kioskmode.AdminReceiver"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_DEVICE_ADMIN">
            <meta-data
                android:name="android.app.device_admin"
                android:resource="@xml/device_admin" />

            <intent-filter>
                <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
            </intent-filter>
        </receiver>

Next, create the class that implements the DeviceAdminReceiver which will return you the Component Name which used later while setting adb Shell commands:

        class AdminReceiver :DeviceAdminReceiver(){

            companion object{
                fun getComponentName(context:Context):ComponentName{
                    Log.d("Component Name",ComponentName(context.applicationContext,
                        AdminReceiver::class.java).toString())
                    return ComponentName(context.applicationContext,
                        AdminReceiver::class.java)
                }
            }

        }
        
Once you have this app on your device, go to the command line and run:
  
        adb shell dpm set-device-owner //here you would paste the component Name which we get in Logcat from AdminReceiver.kt

        adb shell dpm set-device-owner com.android.kioskmode/.AdminReceiver
    

Which should give you something like:

      Success: Device owner set to package com.android.kioskmode
      Active admin set to component {com.android.kioskmode/com.android.kioskmode.AdminReceiver}        
        
Deleting the app 

once you’ve enabled it as a Device Administrator. This is a little roundabout, because the idea behind the device administrator is that it’s not for Google Play store apps, but rather for enterprises where an IT administrator can set these up - and they’re intended for enterprise use afterwards.

Notice that once the Device Owner application is set, it cannot be unset with the dpm command. 

You’ll need to programmatically use the DevicePolicyManager.clearDeviceOwnerApp() method or factory reset your device.
        
Try to delete the app, and if you still run into trouble, go to Settings -> Security -> Device Administrators and uncheck the Kiosk app as a device administrator, then re-try uninstalling.

If you still run into trouble,go to command line and run:

       abd shell dpm remove-active-admin //here you would paste the component Name which we get in Logcat from AdminReceiver.kt

       adb shell dpm remove-active-admin com.android.kioskmode/.AdminReceiver
