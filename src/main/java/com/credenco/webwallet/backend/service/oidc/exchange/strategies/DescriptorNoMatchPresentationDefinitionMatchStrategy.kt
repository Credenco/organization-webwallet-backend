package com.credenco.webwallet.backend.service.oidc.exchange.strategies

import com.credenco.webwallet.backend.domain.Credential
import com.credenco.webwallet.backend.service.oidc.exchange.FilterData
import com.credenco.webwallet.backend.service.oidc.exchange.TypeFilter
import id.walt.oid4vc.data.dif.InputDescriptor
import id.walt.oid4vc.data.dif.PresentationDefinition
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

class DescriptorNoMatchPresentationDefinitionMatchStrategy : PresentationDefinitionMatchStrategy<List<FilterData>> {
    override fun match(
        credentials: List<Credential>, presentationDefinition: PresentationDefinition
    ): List<FilterData> = match(credentials, presentationDefinition.inputDescriptors)

    private fun match(
        credentialList: List<Credential>, inputDescriptors: List<InputDescriptor>
    ) = inputDescriptors.filter { desc ->
        credentialList.none { cred ->
            desc.name == JsonUtils.tryGetData(cred.asJson(), "type")?.jsonArray?.last()?.jsonPrimitive?.content
                    || desc.id == JsonUtils.tryGetData(
                cred.asJson(),
                "type"
            )?.jsonArray?.last()?.jsonPrimitive?.content
        }
    }.map {
        FilterData(
            credential = it.name ?: it.id,
            filters = listOf(TypeFilter(path = emptyList(), pattern = it.name ?: it.id))
        )
    }
}
