package com.yahoo.bastrakov.simpleclockandradio

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.gson.Gson
import com.yahoo.bastrakov.simpleclockandradio.data.ImpExpModel
import com.yahoo.bastrakov.simpleclockandradio.data.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_setting.*
import java.lang.Exception

class SettingActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
//    private var mediaPlayer: MediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        initAllViews()
    }

    private fun initAllViews() {

        setting_exit.setOnClickListener {
            finish()
        }

        setting_exp.setOnClickListener {
            settingExport()
        }

        setting_imp.setOnClickListener {
            settingImport()
        }

        setting_play_link_1.setOnFocusChangeListener { view, b ->
            readLink(b, view, 1)
        }
        setting_play_link_2.setOnFocusChangeListener { view, b ->
            readLink(b, view, 2)
        }
        setting_play_link_3.setOnFocusChangeListener { view, b ->
            readLink(b, view, 3)
        }
        setting_play_link_4.setOnFocusChangeListener { view, b ->
            readLink(b, view, 4)
        }

    }

    private fun readLink(b: Boolean, view: View?, index: Int) {
        if (!b) {
            val link = (view as EditText).text.toString()
            if (!link.isNullOrEmpty()) {
                var test = testUri(link)
                if (test) {
                    sharedPreferencesHelper.savePlayLink(index, link)
                    colorUriField(index, true)
                } else if (!test) {
                    colorUriField(index, false)
                }
            } else {
                colorUriField(index, true)
            }
        }
    }

    private fun colorUriField (index: Int, result: Boolean) {
        var view: EditText = setting_play_link_1
        when (index) {
            1 -> view = setting_play_link_1
            2 -> view = setting_play_link_2
            3 -> view = setting_play_link_3
            4 -> view = setting_play_link_4
        }
        if (!result)
            view.setBackgroundResource(R.drawable.main_toggle_btn_no)
        else
            view.setBackgroundResource(R.drawable.form_field_border)
    }

    private fun testUri (testUri: String) : Boolean {

        MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setDataSource(testUri)

            try {
                prepareAsync ()
            } catch (ex: Exception) {
                return false
            }

//            start()
        }

        return true;
    }

    private fun settingExport() {
        val model = ImpExpModel(
            sharedPreferencesHelper.getPlayLink(1),
            sharedPreferencesHelper.getPlayLink(2),
            sharedPreferencesHelper.getPlayLink(3),
            sharedPreferencesHelper.getPlayLink(4)
        )

        val jsonStr = Gson().toJson(model).toString()

        val a = 0;

    }

    private fun settingImport() {

    }

}
