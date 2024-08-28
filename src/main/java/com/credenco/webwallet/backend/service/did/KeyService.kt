package com.credenco.webwallet.backend.service.did

import id.walt.crypto.keys.Key
import id.walt.crypto.keys.KeySerialization
import kotlinx.coroutines.runBlocking

object KeyService {
    fun deserializeKey(document: String): Key {
        return runBlocking {  KeySerialization.deserializeKey(document).getOrThrow() }
    }
}
