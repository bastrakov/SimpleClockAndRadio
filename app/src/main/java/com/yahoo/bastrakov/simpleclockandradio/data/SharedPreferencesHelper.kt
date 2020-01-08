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
        }
    }

    fun getPlayLink(index: Int): String? {

        val playLink = when(index) {
            1 -> PLAY_LINK1
            2 -> PLAY_LINK2
            3 -> PLAY_LINK3
            else -> PLAY_LINK1
        }
        return loadStringPref(playLink)
    }

    fun saveHelloMsg(msg: String) {
        saveStringPref(HELLO_MSG, msg)
    }

    fun getHelloMsg(): String? {
        return loadStringPref(HELLO_MSG)
    }

}
