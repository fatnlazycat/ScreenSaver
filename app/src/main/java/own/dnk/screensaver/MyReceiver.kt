package own.dnk.screensaver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceiver: BroadcastReceiver() {
    val TAG = "MyReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive")
        val i = Intent(context, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context?.startActivity(i)
    }

}