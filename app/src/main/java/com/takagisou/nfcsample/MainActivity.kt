package com.takagisou.nfcsample

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcF
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.takagisou.nfcsample.R

class MainActivity : AppCompatActivity() {

    companion object {
        val NFC_TYPES = arrayOf(NfcF::class.java.name)
    }

    private val nfcAdapter: NfcAdapter by lazy { NfcAdapter.getDefaultAdapter(applicationContext) }

    private val felicaPendingIntent by lazy {
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        PendingIntent.getActivity(this, 0, intent, 0)
    }

    private val intentFilters by lazy {
        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { addDataType("*/*") }
        arrayOf(filter)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()


        //val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        //val felicaPendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        //val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply { addDataType("*/*") }

        //nfcAdapter.enableForegroundDispatch(this, felicaPendingIntent, arrayOf(filter), arrayOf(NFC_TYPES))
        nfcAdapter.enableForegroundDispatch(this, felicaPendingIntent, intentFilters, arrayOf(NFC_TYPES))

        Log.i(localClassName, "resume")
    }


    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(this)
        Log.i(localClassName, "pause")
    }

    override fun onNewIntent(intent: Intent?) {
        Log.i(localClassName, "onNewIntent")

        val tag = intent?.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return

        // ここで取得したTagを使ってデータの読み込みを行う
        Log.i(localClassName,"felica success!!: $tag")

    }


    private fun goNFCSettings() = if (Build.VERSION.SDK_INT < 16) startActivity(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)) else startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
}