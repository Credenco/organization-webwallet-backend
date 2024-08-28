package com.credenco.webwallet.backend.service.did

import id.walt.crypto.keys.Key
import id.walt.ebsi.accreditation.AccreditationClient
import id.walt.oid4vc.data.CredentialFormat
import id.walt.oid4vc.responses.CredentialResponse

class PregeneratedVCAccreditationClient(did: String,
                                        authenticationKey: Key,
                                        trustedIssuer: String,
                                        val onboardCredential: String?,
                                        val issuerCredential: String?): AccreditationClient("", did = did, authenticationKey, trustedIssuer, "", "", "") {
    override suspend fun getAccreditationToAttest(): CredentialResponse {
        return CredentialResponse.Companion.success(CredentialFormat.jwt_vc, this.issuerCredential!!)
    }

    override suspend fun getAuthorisationToOnboard(): CredentialResponse {
        return CredentialResponse.Companion.success(CredentialFormat.jwt_vc, this.onboardCredential!!)
    }
}
