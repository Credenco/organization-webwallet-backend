package com.credenco.webwallet.backend.service.oidc.exchange.strategies

import com.credenco.webwallet.backend.domain.Credential
import id.walt.oid4vc.data.dif.PresentationDefinition

interface PresentationDefinitionMatchStrategy<out T> {
    fun match(
        credentials: List<Credential>, presentationDefinition: PresentationDefinition
    ): T
}
