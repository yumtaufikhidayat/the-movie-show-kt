package com.taufik.themovieshow.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.taufik.themovieshow.R
import com.taufik.themovieshow.utils.helper.SecureEncryptedKey.generateOrGetSecretKey
import com.taufik.themovieshow.utils.language.LanguageCache
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_IV
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_PASSPHRASE
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_SECURE_PREFS
import com.taufik.themovieshow.utils.objects.PreferencesKey.IV
import com.taufik.themovieshow.utils.objects.PreferencesKey.PASSPHRASE
import es.dmoral.toasty.Toasty
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings.preferences_pb")
val Context.secureDataStore by preferencesDataStore(name = KEY_SECURE_PREFS)

private const val TIME_60 = 60

fun Context.showTrailerVideo(key: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, "vnd.youtube://$key".toUri()))
    } catch (e: Exception) {
        this.showSuccessToastyIcon(getString(R.string.tvInstallBrowserYouTube, e.localizedMessage))
    }
}

fun Context.tryOpenBrowser(link: String) {
    runCatching {
        val intentBrowser = Intent(Intent.ACTION_VIEW, link.toUri())
        if (intentBrowser.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intentBrowser, getString(R.string.tvOpenWith)))
        } else {
            showErrorToastyIcon(getString(R.string.tvNoHandleApp))
        }
    }.onFailure { e ->
        showErrorToastyIcon(e.message.orEmpty())
    }
}

fun Context.share(shareText: String, link: String) {
    try {
        val body = StringBuilder(shareText).append(link).toString()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, body)
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.tvShareWith)
            )
        )
    } catch (e: Exception) {
        Log.e("TAG", "Error: ${e.localizedMessage}")
        showErrorToastyIcon(getString(R.string.tvOops))
    }
}

fun Context.showSuccessToastyIcon(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.showSuccessToasty(message: String) {
    Toasty.success(this, message, Toast.LENGTH_SHORT, false).show()
}

fun Context.showErrorToastyIcon(message: String) {
    Toasty.error(this, message, Toast.LENGTH_SHORT, true).show()
}

fun Context.createCustomTabView(@StringRes titleRes: Int, isSelected: Boolean): View {
    val view = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
    val tabText = view.findViewById<TextView>(R.id.tabText)

    tabText.text = getString(titleRes)
    tabText.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
    tabText.setTextColor(
        ContextCompat.getColor(
            this,
            if (isSelected) android.R.color.white else R.color.colorSemiBlack
        )
    )

    tabText.background = if (isSelected) ContextCompat.getDrawable(this, R.drawable.bg_tab) else null
    return view
}

fun Context.convertRuntime(value: Int): String {
    val hours = value / TIME_60
    val minutes = value % TIME_60
    return this.getString(R.string.tvRuntimeInTime, hours, minutes)
}

fun Context.getLocalizedString(@StringRes resId: Int): String {
    val locale = Locale(LanguageCache.get(this))
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config).getText(resId).toString()
}

suspend fun Context.encryptAndStorePassphrase(rawPassphrase: String) {
    val secretKey = generateOrGetSecretKey()
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val secureDataStore = this.secureDataStore

    cipher.init(Cipher.ENCRYPT_MODE, secretKey)

    val iv = cipher.iv
    val encrypted = cipher.doFinal(rawPassphrase.toByteArray(Charsets.UTF_8))

    secureDataStore.edit { prefs ->
        prefs[IV] = Base64.encodeToString(iv, Base64.DEFAULT)
        prefs[PASSPHRASE] = Base64.encodeToString(encrypted, Base64.DEFAULT)
    }
}

fun Context.getDecryptedPassphrase(): ByteArray {
    val prefs = this.getSharedPreferences(KEY_SECURE_PREFS, Context.MODE_PRIVATE)
    val iv = Base64.decode(prefs.getString(KEY_IV, null), Base64.DEFAULT)
    val encrypted = Base64.decode(prefs.getString(KEY_PASSPHRASE, null), Base64.DEFAULT)

    val secretKey = generateOrGetSecretKey()
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

    return cipher.doFinal(encrypted)
}