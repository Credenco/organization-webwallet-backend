package com.credenco.webwallet.backend.service.oidc.exchange.strategies

import com.credenco.webwallet.backend.domain.Credential
import com.credenco.webwallet.backend.service.oidc.exchange.TypeFilter
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

abstract class BaseFilterPresentationDefinitionMatchStrategy<T> : PresentationDefinitionMatchStrategy<T> {
    private val modifiersRegex = """(\$/[gmixsuXUAJn]*)""".toRegex()
    protected fun isMatching(credential: Credential, fields: List<TypeFilter>) = fields.all { typeFilter ->
        typeFilter.path.mapNotNull {
            JsonUtils.tryGetData(credential.asJson(), it)?.let {
                when (it) {
                    is JsonPrimitive -> it.jsonPrimitive.content
                    is JsonArray -> it.jsonArray.last().jsonPrimitive.content
                    else -> ""
                }
            }
        }.any {
            modifiersRegex.replace(typeFilter.pattern.removePrefix("/"), """\$""").toRegex().matches(it)
        }
    }
}
