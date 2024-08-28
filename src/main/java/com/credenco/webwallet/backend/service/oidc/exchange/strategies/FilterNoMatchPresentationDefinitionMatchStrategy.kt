package com.credenco.webwallet.backend.service.oidc.exchange.strategies

import com.credenco.webwallet.backend.domain.Credential
import com.credenco.webwallet.backend.service.oidc.exchange.FilterData
import com.credenco.webwallet.backend.service.oidc.exchange.PresentationDefinitionFilterParser
import id.walt.oid4vc.data.dif.PresentationDefinition

class FilterNoMatchPresentationDefinitionMatchStrategy(
    private val filterParser: PresentationDefinitionFilterParser,
) : BaseFilterPresentationDefinitionMatchStrategy<List<FilterData>>() {

    override fun match(
        credentials: List<Credential>, presentationDefinition: PresentationDefinition
    ): List<FilterData> = match(credentials, filterParser.parse(presentationDefinition))

    private fun match(
        credentialList: List<Credential>, filters: List<FilterData>
    ) = filters.filter { fields ->
        credentialList.none { credential ->
            isMatching(credential, fields.filters)
        }
    }.distinct()
}
