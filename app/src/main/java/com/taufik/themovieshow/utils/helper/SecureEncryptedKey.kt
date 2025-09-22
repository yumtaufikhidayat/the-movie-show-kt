package com.taufik.themovieshow.utils.helper

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.taufik.themovieshow.utils.objects.CommonConstants.ENCRYPTED_DB_PASSPHRASE
import com.taufik.themovieshow.utils.objects.CommonConstants.KEY_STORE_ANDROID
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object SecureEncryptedKey {

    private const val KEY_ALIAS = ENCRYPTED_DB_PASSPHRASE
    private const val KEY_STORE = KEY_STORE_ANDROID

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
}