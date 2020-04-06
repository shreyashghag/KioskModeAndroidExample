package com.android.kioskmode


import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    lateinit var mButton: Button

    private var mDecorView: View? = null
    private lateinit var mDpm: DevicePolicyManager
    private var mIsKioskEnabled = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButton= findViewById<Button>(R.id.button_toggle_kiosk)

        mButton.setOnClickListener {
            enableKioskMode(true)
        }

        val deviceAdmin = AdminReceiver.getComponentName(this)
        mDpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (!mDpm.isAdminActive(deviceAdmin)) {
            Toast.makeText(this, getString(R.string.not_device_admin), Toast.LENGTH_SHORT).show()
        }

        if (mDpm.isDeviceOwnerApp(packageName)) {
            mDpm.setLockTaskPackages(deviceAdmin, arrayOf(packageName))
        } else {
            Toast.makeText(this, getString(R.string.not_device_owner), Toast.LENGTH_SHORT).show()
        }

        mDecorView = window.decorView


    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    fun enableKioskMode(enabled: Boolean){

        try{
            if (enabled) {
                if (mDpm.isLockTaskPermitted(this.packageName)) {
                    startLockTask()
                    mIsKioskEnabled = true
                    mButton.text = getString(R.string.exit_kiosk_mode)
                } else {
                    Toast.makeText(this, getString(R.string.kiosk_not_permitted), Toast.LENGTH_SHORT).show()
                }
            } else {
                stopLockTask()
                mIsKioskEnabled = false
                mButton.text = getString(R.string.enter_kiosk_mode)
            }
        }catch (e:Exception){

        }

    }

    fun hideSystemUI()
    {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

}
