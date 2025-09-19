package com.taufik.themovieshow.utils.helper

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.taufik.themovieshow.utils.extensions.secureDataStore
import com.taufik.themovieshow.utils.objects.CommonConstants.ENCRYPTED_DB_PASSPHRASE
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_IV
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_PASSPHRASE
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_SECURE_PREFS
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object SecureEncryptedKey {

    private const val KEY_ALIAS = ENCRYPTED_DB_PASSPHRASE
    private val IV = stringPreferencesKey(KEY_IV)
    val PASSPHRASE = stringPreferencesKey(KEY_PASSPHRASE)

    fun generateOrGetSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

        if (keyStore.containsAlias(KEY_ALIAS)) {
            val entry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
            return entry.secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
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
}