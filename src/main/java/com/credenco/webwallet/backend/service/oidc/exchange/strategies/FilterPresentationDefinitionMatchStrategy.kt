package com.credenco.webwallet.backend.service.oidc.exchange.strategies

import com.credenco.webwallet.backend.domain.Credential
import com.credenco.webwallet.backend.service.oidc.exchange.FilterData
import com.credenco.webwallet.backend.service.oidc.exchange.PresentationDefinitionFilterParser
import id.walt.oid4vc.data.dif.PresentationDefinition

class FilterPresentationDefinitionMatchStrategy(
    private val filterParser: PresentationDefinitionFilterParser,
) : BaseFilterPresentationDefinitionMatchStrategy<List<Credential>>() {

    override fun match(
        credentials: List<Credential>, presentationDefinition: PresentationDefinition
    ): List<Credential> = match(credentials, filterParser.parse(presentationDefinition))

    private fun match(
        credentialList: List<Credential>, filters: List<FilterData>
    ) = filters.isNotEmpty().takeIf { it }?.let {
        credentialList.filter { credential ->
            filters.any { fields ->
                isMatching(credential, fields.filters)
            }
        }
    } ?: emptyList()
}
