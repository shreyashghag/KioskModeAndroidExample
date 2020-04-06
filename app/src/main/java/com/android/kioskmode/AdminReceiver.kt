package com.android.kioskmode

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.util.Log

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