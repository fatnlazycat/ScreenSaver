package own.dnk.screensaver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.util.concurrent.TimeUnit
import android.support.v4.content.ContextCompat.getSystemService



class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    val et: EditText by lazy { findViewById<EditText>(R.id.et) }
    val btn: Button by lazy { findViewById<Button>(R.id.btn) }
    var ok = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate, ok=$ok")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume, ok=$ok")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause, ok=$ok")

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.d(TAG, "onWindowFocusChanged, hasFocus=$hasFocus")

        if (!hasFocus && !ok) {
            Log.d(TAG, "onWindowFocusChanged, starting new activity")

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    fun click(view: View) {
        val r = Regex("ooooo(\\d*)")
        val minutes = r.matchEntire(et.text)?.groupValues?. let {
            ok = true
            if (it.size > 1) try { it[1].toLong() }
            catch (e: Exception) { null }
            else null
        } ?: 15

        et.setText("")

        Log.d(TAG, "click, ok=$ok, minutes=$minutes, text=${et.text}")

        if (ok) {
            setAlarm(minutes)

            Log.d(TAG, "click, finishing activity")
            view.post { finish() }

        }
    }

    fun setAlarm(minutes: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.set(AlarmManager.RTC,
            System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes),
            pendingIntent)
        Log.d(TAG, "Alarm set for $minutes")
    }

    override fun onUserLeaveHint() {
        Log.d(TAG, "onUserLeaveHint, ok=$ok")

        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        forceHome(this, intent)
        super.onUserLeaveHint()
    }

    private fun forceHome(paramContext: Context, paramIntent: Intent?) {
        if (paramIntent != null) {
            (getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(
                AlarmManager.RTC,
                System.currentTimeMillis() - 1,
                PendingIntent.getActivity(paramContext, 0, paramIntent, 0)
            )

            Log.d(TAG, "Alarm set from forceHome")
        }

    }
}
