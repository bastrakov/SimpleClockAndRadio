package com.yahoo.bastrakov.simpleclockandradio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.yahoo.bastrakov.simpleclockandradio.data.ImpExpModel
import com.yahoo.bastrakov.simpleclockandradio.data.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_setting.*
import java.lang.Exception
import java.io.File
import java.io.FileOutputStream


class SettingActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private val savePath =
        "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/simpleclockandradio.json"


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

        setting_hello_msg.setOnFocusChangeListener { view, b ->
            if (!b) {
                val msg = setting_hello_msg.text.toString()
                if (!msg.isNullOrEmpty()) {
                    sharedPreferencesHelper.saveHelloMsg(msg)
                }
            }
        }

        setting_play_link_1.setOnFocusChangeListener { view, b ->
            saveLink(b, view, 1)
        }
        setting_play_link_2.setOnFocusChangeListener { view, b ->
            saveLink(b, view, 2)
        }
        setting_play_link_3.setOnFocusChangeListener { view, b ->
            saveLink(b, view, 3)
        }

        populateFieldsWithSavedData()
    }

    private fun populateFieldsWithSavedData() {

        val helloMsg = sharedPreferencesHelper.getHelloMsg();
        setting_hello_msg.setText(if (helloMsg.isNullOrEmpty()) {getString(R.string.hello_msg)} else helloMsg)

        val playLink1 = sharedPreferencesHelper.getPlayLink(1);
        val playLink2 = sharedPreferencesHelper.getPlayLink(2);
        val playLink3 = sharedPreferencesHelper.getPlayLink(3);

        setting_play_link_1.setText(if (playLink1.isNullOrEmpty()) {getString(R.string.play_link_1_url)} else playLink1)
        setting_play_link_2.setText(if (playLink2.isNullOrEmpty()) {getString(R.string.play_link_2_url)} else playLink2)
        setting_play_link_3.setText(if (playLink3.isNullOrEmpty()) {getString(R.string.play_link_3_url)} else playLink3)
    }

    private fun saveLink(b: Boolean, view: View?, index: Int) {
        if (!b) {
            val link = (view as EditText).text.toString()
            if (!link.isNullOrEmpty()) {
                sharedPreferencesHelper.savePlayLink(index, link)
            }
        }
    }

    private fun settingExport() {

        val helloMsg = sharedPreferencesHelper.getHelloMsg();
        val playLink1 = sharedPreferencesHelper.getPlayLink(1);
        val playLink2 = sharedPreferencesHelper.getPlayLink(2);
        val playLink3 = sharedPreferencesHelper.getPlayLink(3);

        val model = ImpExpModel(
            if (helloMsg.isNullOrEmpty()) {getString(R.string.hello_msg)} else helloMsg,
            if (playLink1.isNullOrEmpty()) {getString(R.string.play_link_1_url)} else playLink1,
            if (playLink2.isNullOrEmpty()) {getString(R.string.play_link_2_url)} else playLink2,
            if (playLink3.isNullOrEmpty()) {getString(R.string.play_link_3_url)} else playLink3
        )

        val jsonStr = Gson().toJson(model).toString()

        try {
            val fos = FileOutputStream(File(savePath))
            fos.write(jsonStr.toByteArray())
            fos.close()

            Toast.makeText(this, getString(R.string.data_saved), Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            ex.printStackTrace();
        }
    }

    private fun settingImport() {
        if (File(savePath).exists()) {
            val jsonStr = File(savePath).readText()
            val model = Gson().fromJson(jsonStr, ImpExpModel::class.java)

            if (!model.helloMsg.isNullOrEmpty())
                sharedPreferencesHelper.saveHelloMsg(model.helloMsg)

            if (!model.link1.isNullOrEmpty())
                sharedPreferencesHelper.savePlayLink(1, model.link1)
            if (!model.link2.isNullOrEmpty())
                sharedPreferencesHelper.savePlayLink(2, model.link2)
            if (!model.link3.isNullOrEmpty())
                sharedPreferencesHelper.savePlayLink(3, model.link3)

            populateFieldsWithSavedData()

            Toast.makeText(this, getString(R.string.data_loaded), Toast.LENGTH_LONG).show()
        }
    }

}
