package com.yahoo.bastrakov.simpleclockandradio

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.yahoo.bastrakov.simpleclockandradio.data.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var animation: Animation
    private var prevIndexSelected = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        animation = AnimationUtils.loadAnimation(applicationContext, R.anim.waiter_rotate)
        initAllViews()
    }

    override fun onResume() {
        super.onResume()

        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.i(TAG, "onPause")
        doStopPlayAll()
    }

    private fun initAllViews() {

        main_setting.setOnClickListener{
            val intent = Intent(applicationContext, SettingActivity::class.java)
            startActivity(intent)
        }

        main_exit.setOnClickListener{
            finish()
        }

        main_play1.setOnClickListener{
            doPlay(1)
        }
        main_play2.setOnClickListener{
            doPlay(2)
        }
        main_play3.setOnClickListener{
            doPlay(3)
        }
        main_play4.setOnClickListener{
            doPlay(4)
        }

        showTimeTicks()

        main_volume_up.setOnClickListener{
            val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI)
        }

        main_volume_down.setOnClickListener{
            val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI)
        }

    }

    private fun showTimeTicks() {
        val dt = Calendar.getInstance().time
        val datMsg = SimpleDateFormat("dd.MM.yyyy").format(dt)
        main_day.text = datMsg
        val weekDayMsg = SimpleDateFormat("EEEE").format(dt.time)
        main_week_day.text = weekDayMsg
        val clockMsg = SimpleDateFormat("HH:mm").format(dt)
        main_clock.text = clockMsg

        Thread(Runnable {
            Thread.sleep(5000)
            this@MainActivity.runOnUiThread { showTimeTicks() }
        }).start()
    }

    private fun doStopPlayAll() {
        Log.d(TAG, "doStopPlayAll")

        for (btn in arrayOf(main_play1, main_play2, main_play3, main_play4)) {
            btn.setBackgroundResource(R.drawable.main_toggle_btn_no)
            btn.clearAnimation()
            animation.cancel()
        }

        try {
            mediaPlayer?.let{
                it.stop()
                it.release()
            }
            mediaPlayer = null
        } catch (ex: Exception) {
            Log.d(TAG, "doStopPlayAll EX: ${ex.message}")
        }
    }

    private fun doPlay(index: Int) {

        doStopPlayAll()

        if (prevIndexSelected == index) {
            prevIndexSelected = 0
            return
        }

        Log.d(TAG, "doPlay $index")

        val btn= when (index) {
            1 -> main_play1
            2 -> main_play2
            3 -> main_play3
            4 -> main_play4
            else -> main_play1
        }
        btn.setBackgroundResource(R.drawable.no_star_gold)
        btn.startAnimation(animation)

        val link = when (index) {
            1 -> sharedPreferencesHelper.getPlayLink(1)
            2 -> sharedPreferencesHelper.getPlayLink(2)
            3 -> sharedPreferencesHelper.getPlayLink(3)
            4 -> sharedPreferencesHelper.getPlayLink(4)
            else -> sharedPreferencesHelper.getPlayLink(1)
        }

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(link)
                setAudioStreamType(AudioManager.STREAM_MUSIC)

                setOnPreparedListener {
                    Log.d(TAG, "doPlay $index START")
                    start()
                    this@MainActivity.runOnUiThread {
                        btn.clearAnimation()
                        btn.setBackgroundResource(R.drawable.main_toggle_btn_yes)
                        animation.cancel()
                    }
                }
                setOnErrorListener(errorListener)

                Log.d(TAG, "doPlay $index prepareAsync")
                prepareAsync()
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.radio_error), Toast.LENGTH_LONG).show()
            Log.d(TAG, "doPlay Exception: ${e.message}")
        }

        prevIndexSelected = index
    }

    private val errorListener = MediaPlayer.OnErrorListener { mp, what, extra ->

        Log.d(TAG, "errorListener")

        val msg = when (what) {
            MediaPlayer.MEDIA_ERROR_IO ->
                "${getString(R.string.radio_error)} MEDIA_ERROR_IO"
            MediaPlayer.MEDIA_ERROR_TIMED_OUT ->
                "${getString(R.string.radio_error)} MEDIA_ERROR_TIMED_OUT"
            MediaPlayer.MEDIA_ERROR_UNSUPPORTED ->
                "${getString(R.string.radio_error)} MEDIA_ERROR_UNSUPPORTED"
            MediaPlayer.MEDIA_ERROR_MALFORMED ->
                "${getString(R.string.radio_error)} MEDIA_ERROR_MALFORMED"
            else ->
                "${getString(R.string.radio_error)}"
        }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        false
    }

}
