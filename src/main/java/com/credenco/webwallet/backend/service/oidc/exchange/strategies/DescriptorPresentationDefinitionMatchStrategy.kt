package com.credenco.webwallet.backend.service.oidc.exchange.strategies

import com.credenco.webwallet.backend.domain.Credential
import id.walt.oid4vc.data.dif.InputDescriptor
import id.walt.oid4vc.data.dif.PresentationDefinition
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

class DescriptorPresentationDefinitionMatchStrategy : PresentationDefinitionMatchStrategy<List<Credential>> {
    override fun match(
        credentials: List<Credential>, presentationDefinition: PresentationDefinition
    ): List<Credential> = match(credentials, presentationDefinition.inputDescriptors)

    private fun match(
        credentialList: List<Credential>, inputDescriptors: List<InputDescriptor>
    ) = credentialList.filter { cred ->
        inputDescriptors.any { desc ->
            desc.name == JsonUtils.tryGetData(cred.asJson(), "type")?.jsonArray?.last()?.jsonPrimitive?.content
        }
    }
}
