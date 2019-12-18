package com.yahoo.bastrakov.simpleclockandradio.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferencesHelper {

    companion object {

        const val TAG = "SharedPreferencesHelper"

        const val HELLO_MSG = "com.yahoo.bastrakov.simpleclockandradio.HELLO_MSG"
        const val PLAY_LINK1 = "com.yahoo.bastrakov.simpleclockandradio.PLAY_LINK1"
        const val PLAY_LINK2 = "com.yahoo.bastrakov.simpleclockandradio.PLAY_LINK2"
        const val PLAY_LINK3 = "com.yahoo.bastrakov.simpleclockandradio.PLAY_LINK3"
        const val PLAY_LINK4 = "com.yahoo.bastrakov.simpleclockandradio.PLAY_LINK4"



        const val TEST_LINK1 = "http://netzradio-germania.de:1488"
        const val TEST_LINK2 = "http://maisnova.link:8088/Maisnova/MNCaxias/playlist.m3u8"
        const val TEST_LINK3 = "http://www.radioson.ru:8009/JazzClassicAndNew.RadioSon.ru.mp3"
        const val TEST_LINK4 = "http://www1.c1hundred.co.uk:7146/C100"

    }

    private val sPref: SharedPreferences
    private val context: Context


    constructor(context: Context) {
        this.context = context
        this.sPref = PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun saveStringPref(key: String, value: String) {
        val ed = sPref.edit()
        ed.putString(key, value)
        ed.commit()
    }

    private fun loadStringPref(key: String): String? {
        return sPref.getString(key, null)
    }

    fun savePlayLink(index: Int, link: String) {
        when(index){
            1 -> saveStringPref(PLAY_LINK1, link)
            2 -> saveStringPref(PLAY_LINK2, link)
            3 -> saveStringPref(PLAY_LINK3, link)
            4 -> saveStringPref(PLAY_LINK4, link)
        }
    }

    fun getPlayLink(index: Int): String? {
        var link: String? = "";
        when(index){
            1 -> link = loadStringPref(PLAY_LINK1)
            2 -> link = loadStringPref(PLAY_LINK2)
            3 -> link = loadStringPref(PLAY_LINK3)
            4 -> link = loadStringPref(PLAY_LINK4)
        }
        if (link.isNullOrEmpty())
            return null;
        return link;
    }

}
