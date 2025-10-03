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
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.R
import com.taufik.themovieshow.utils.helper.SecureEncryptedKey.generateOrGetSecretKey
import com.taufik.themovieshow.utils.language.LanguageCache
import com.taufik.themovieshow.utils.objects.CommonConstants.KET_DB_INTEGRITY_PREFS
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_SECURE_PREFS
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_SETTINGS_PREFS
import com.taufik.themovieshow.utils.objects.PreferencesKey.IV_KEY
import com.taufik.themovieshow.utils.objects.PreferencesKey.PASSPHRASE_KEY
import com.taufik.themovieshow.utils.objects.PreferencesKey.PASS_HASH_KEY
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Locale
import javax.crypto.Cipher

val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = KEY_SETTINGS_PREFS)
val Context.secureDataStore by preferencesDataStore(name = KEY_SECURE_PREFS)
val Context.integrityDataStore: DataStore<Preferences> by preferencesDataStore(name = KET_DB_INTEGRITY_PREFS)

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
        prefs[IV_KEY] = Base64.encodeToString(iv, Base64.DEFAULT)
        prefs[PASSPHRASE_KEY] = Base64.encodeToString(encrypted, Base64.DEFAULT)
    }
}

suspend fun Context.getDecryptedPassphrase(): ByteArray {
    val prefs = secureDataStore.data.first()
    val base64Pass = prefs[PASSPHRASE_KEY]

    return if (base64Pass != null) {
        Base64.decode(base64Pass, Base64.DEFAULT)
    } else {
        generateAndStoreNewPassphrase()
    }
}

fun Context.generateAndStoreNewPassphrase(): ByteArray {
    val newPassphrase = ByteArray(32).also { SecureRandom().nextBytes(it) }
    val base64Pass = Base64.encodeToString(newPassphrase, Base64.DEFAULT)

    runBlocking {
        secureDataStore.edit { it[PASSPHRASE_KEY] = base64Pass }
    }

    if (BuildConfig.DEBUG) Log.w("DB", "New passphrase generated & stored")
    return newPassphrase
}

suspend fun Context.ensurePassphraseIntegrity(currentPassphrase: ByteArray): Boolean {
    val currentHash = Base64.encodeToString(
        MessageDigest.getInstance("SHA-256").digest(currentPassphrase),
        Base64.NO_WRAP
    )

    val storedHash = integrityDataStore.data
        .map { prefs -> prefs[PASS_HASH_KEY] }
        .first()

    return if (storedHash == null || storedHash != currentHash) {
        integrityDataStore.edit { prefs ->
            prefs[PASS_HASH_KEY] = currentHash
        }
        // new passphrase the database must be changed, return false
        false
    } else {
        // same passphrase then database is safe
        true
    }
}