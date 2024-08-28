INSERT IGNORE INTO wallet (external_key, display_name, api_key)
VALUES ('df803dd4-32c6-4a69-afe5-d807c6701ed7', 'KvK', 'tsh7ugwBfY3a1fE90w9aWnwg90A04');
select @walletId := id
from wallet
where external_key = 'df803dd4-32c6-4a69-afe5-d807c6701ed7';

UPDATE wallet
SET display_name = 'Kamer van Koophandel'
WHERE id = @walletId;

update wallet
set logo = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIwAAAAsCAYAAAC33FDQAAAACXBIWXMAACxLAAAsSwGlPZapAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAU1SURBVHgB7Zw/TBRBFMYfxkYtNBY2Nppo5Z9oIZQcVhpNBE000QIxNiYSIDHYCVgpMQGCdkTAhAIShUswWslRAoUEpdJEGhoLpVFL5Lu45Dxud2e+NzN3xf4Swp9bwuzON+97894cdZtbSEaGIbskI8OCTDAZVmSCybBid/kP6q51CMPY/ZvS2tSQet3a9x/S1DNc/MyQO3FM5h63F79ueToiM4ufhKH3+gXpuXHR+PrC6ldpejQsDNMP70pz/ant73HvR+/1CcNcX7vkTh5LvW7527qcfdAvLD3XL0rvjQs7fh48wrQ9n6DFcuTQQRm9f2v7+47LOWGBAGzIL6wIA8ZcKpYQ4Pm29I8Iy5mjhyuKBQQVTN/UO+uJKgWrCxMQgWhzYN8eYcA4Nn7/Mb5+ZomLZBhjaPCcNYtyuvtu7OvBBIMb6J18LywDd67+J5aI27l6YRn/sGB0HcI7OwEdl3ISkqHZgozNLQoLxFLpOUcEEUyUt7DAejovNVZ87UrDaWExzX/mV78IA0I7PkJRXJRT/KJE3pI23iCC0YbIwbaW2Nc1trS8tm5kS2xiHTq6YFFu/DK32VJuN9XH5i2leBeMJkRCLMhb0ui83CgMeLhpySyEzuZdIfMX7aIcaLtqdK1XwWhDJG4iyU8jGk8cF5bC52QxFEg7ws7IZOwumFlcofNDRGcsStMo7VUwmhAJPzXdjmIls6s5bfczTkbHK/V8bmUDFmXX6LSwmC7KCG+C0YRICMXET0sxKWZVAoKOizLF1wg7wgQgJwhB1+gb+jljUdqO04tgNCHSxk9L0SSY+aXKeQzugyFU7oJFySbkGKPtogTOBaMJkZGfMt6P32UnKi4pz5OT0ZpLb5Fo0dS1yivmNjgXjCpEbvV2NIkiW4KPsyXWjlh7NAXj1dS12EUJnApGEyLhp3HFOVNMmp9xlBfn8lt2xCTsuA/f9E3y+WFcxdwUZ4LRhMikZpcNGlsq3y1pcgOfjBcWZPDtvDAkVcxNcSYYtt6CSU5qdtnSSu5OyvtFjB1BLL5rL5oiaFLF3JSqH6BC2GfDayWat+ofbKsg/29XBLEwY9JYom9cPeeaOHHX9mLC6qhBEhBL8zku+Y1siDn7gr8bqvbCAMHgLJKWmhAMlI9EzhWt57mVHp2RYc6+hD4kxYD7Q29PQ82c6R2cnU/t65ii6WB3veTKAiFqLy5ArqmxJqeCYScpwqU1sQermKQyRO3FFVprciYYhOSfr57Ix2fdwuLSmjQHq2zRnPqzBXWezddDVPskQmNNTgUDtDUVV9YUYosbEXJ3FNV5cAZIU/NhrclLDoNGoGayXFlTiF1LSGGWg34Qmwaw1uRFMLgJtrkFXFlTiES0mrUXCJU9bQgYa/K2S8LK02w1XVhTMRn1XKpvbqjudho5jSbC2VqT1201EjPNzsmFNfmsj8DyDuzV7QxdoInmttbkVTBQvqZ768KafFpGrdReEEVDWZP3wp02m9dak6aDnUSt1V6wMDXR3NSaglR6bd70XgmtNfmwpWq8BTYJiEUTzU2tKYhgtCFTa02wJW0VupwQB6Vs0UZzE2sK1kvShkyNNeHvnjni7i2r1ay9pKFJgEGaNQUTjLY2AzTWpLXFUmr53AuErKm0p1lT0G41cglNyNRYEyKMK1uqtfylHG2lPcmagh9v0JSzAWtNxQNODpqEqL3Uqh1FuIjmcda0QzB4GMzH/n17xQRtORuw1uSigx3qLbBatJX2OGuqy/5Pb4YN2X/RzLAiE0yGFZlgMqz4C8r3gdOKZlydAAAAAElFTkSuQmCC'
where id = @walletId;


INSERT IGNORE INTO wallet_key (wallet_id, kid, display_name, document, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'BsmUragmz_pRBv1R0xSIq07UNZkEJqn6J_N22DklCbk', 'Key KvK Credentials','{"type":"jwk","jwk":"{\\"kty\\":\\"OKP\\",\\"d\\":\\"JWrcbb4CrkvgF4DWBr1fTBVlxGOyHFhvRGtFWF-vxL0\\",\\"crv\\":\\"Ed25519\\",\\"kid\\":\\"BsmUragmz_pRBv1R0xSIq07UNZkEJqn6J_N22DklCbk\\",\\"x\\":\\"eAnNnM1oLbyoQMSlqQvZgdBoJf2J8BXk2OPfilwcSzs\\"}"}', now(), 'System', now(), 'System');
select @wallet_key_id := id
from wallet_key
where wallet_id = @walletId
  and kid = 'BsmUragmz_pRBv1R0xSIq07UNZkEJqn6J_N22DklCbk';


INSERT INTO issuer (wallet_id, did, created_at, created_by, last_modified_at, last_modified_by, configuration_url, issue_url, configuration_content)
(select wallet.id,
        'did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0',
        now(), 'System', now(), 'System',
        'https://mijnkvk.acc.credenco.com/.well-known/openid-credential-issuer',
        'https://mijnkvk.acc.credenco.com/issue',
        ''
from wallet
left join issuer on issuer.wallet_id = wallet.id and issuer.did = 'did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0'
where issuer.id is null);

select @issuer_id := id
from issuer
where wallet_id = @walletId
  and did =
      'did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0';


INSERT IGNORE INTO did (wallet_id, display_name, did, document, wallet_key_id, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'DID KvK Credentials', 'did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0', '{"@context":["https://www.w3.org/ns/did/v1","https://w3id.org/security/suites/jws-2020/v1"],"id":"did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0","verificationMethod":[{"id":"did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0#0","type":"JsonWebKey2020","controller":"did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0","publicKeyJwk":{"kty":"OKP","crv":"Ed25519","kid":"BsmUragmz_pRBv1R0xSIq07UNZkEJqn6J_N22DklCbk","x":"eAnNnM1oLbyoQMSlqQvZgdBoJf2J8BXk2OPfilwcSzs"}}],"assertionMethod":["did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0#0"],"authentication":["did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0#0"],"capabilityInvocation":["did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0#0"],"capabilityDelegation":["did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0#0"],"keyAgreement":["did:jwk:eyJrdHkiOiJPS1AiLCJjcnYiOiJFZDI1NTE5Iiwia2lkIjoiQnNtVXJhZ216X3BSQnYxUjB4U0lxMDdVTlprRUpxbjZKX04yMkRrbENiayIsIngiOiJlQW5Obk0xb0xieW9RTVNscVF2WmdkQm9KZjJKOEJYazJPUGZpbHdjU3pzIn0#0"]}', @wallet_key_id, now(), 'System', now(), 'System');
select @issuer_did_id := id
from did
where wallet_id = @walletId
  and display_name = 'DID KvK Credentials';


INSERT IGNORE INTO credential_issue_template (id, wallet_id, credential_configuration_id, display_name, template, issuer_did_id)
VALUES (2, @walletId, 'KVKRegistration_jwt_vc_json', '', '{}', @issuer_did_id);

UPDATE credential_issue_template
SET display_name         = 'KvK number VC template',
    template             = '{
                              "@context": [
                                "https://www.w3.org/2018/credentials/v1",
                                "https://www.w3.org/2018/credentials/examples/v1"
                              ],
                              "id": "http://example.gov/credentials/3732",
                              "type": [
                                "VerifiableCredential",
                                "VerifiableAttestation",
                                "KVKRegistration"
                              ],
                              "issuer": {
                                "id": "did:web:vc.transmute.world"
                              },
                              "issued": "<timestamp>",
                              "validFrom": "<timestamp>",
                              "issuanceDate": "<timestamp>",
                              "credentialSubject": {
                                "id": "did:example:ebfeb1f712ebc6f1c276e12ec21",
                                "kvkNummer": "",
                                "naam":  "",
                                "rechtsvorm": "",
                                "startdatum":  "",
                                "einddatum": ""
                              },
                              "termsOfUse": [{
                                "id": "https://api-pilot.ebsi.eu/trusted-issuers-registry/v5/issuers/did:ebsi:zivtiw8mjYWEV8vDWodTKoj/attributes/e9c28a971ce537d5681b159b05807850163fad7f391c5271d9c5be5688ac8f68",
                                "type": "IssuanceCertificate"
                              }],
                              "credentialSchema": {
                                "id": "https://api-pilot.ebsi.eu/trusted-schemas-registry/v3/schemas/0xd2204647818f9c0c93f64b70f1c892ea2a8e0a747ceaa2f77373b41996bb764d",
                                "type": "FullJsonSchemaValidator2021"
                              }
                            }

',
    issuer_did_id = @issuer_did_id
WHERE wallet_id = @walletId
  and credential_configuration_id = 'KVKRegistration_jwt_vc_json';

INSERT IGNORE INTO credential_issue_template (id, wallet_id, credential_configuration_id, display_name, template, issuer_did_id)
VALUES (3, @walletId, 'RSIN_jwt_vc_json', '', '{}', @issuer_did_id);

UPDATE credential_issue_template
SET display_name         = 'KvK RCIN VC template',
    template             = '{
  "@context": [
    "https://www.w3.org/2018/credentials/v1",
    "https://www.w3.org/2018/credentials/examples/v1"
  ],
  "id": "http://example.gov/credentials/3732",
  "type": [
    "VerifiableCredential",
    "VerifiableAttestation",
    "RSIN"
  ],
  "issuer": {
    "id": "did:web:vc.transmute.world"
  },
  "issuanceDate": "2020-03-10T04:24:12.164Z",
  "credentialSubject": {
    "id": "did:example:ebfeb1f712ebc6f1c276e12ec21",
    "rsin": "",
    "naam":  ""
  }
}
',
    issuer_did_id = @issuer_did_id
WHERE wallet_id = @walletId
  and credential_configuration_id = 'RSIN_jwt_vc_json';

INSERT IGNORE INTO credential_issue_template (id, wallet_id, credential_configuration_id, display_name, template, issuer_did_id)
VALUES (4, @walletId, 'BevoegdheidUittreksel_jwt_vc_json', '', '{}', @issuer_did_id);

UPDATE credential_issue_template
SET display_name         = 'Bevoegdheid uitreksel VC template',
    template             = '{
  "@context": [
    "https://www.w3.org/2018/credentials/v1",
    "https://www.w3.org/2018/credentials/examples/v1"
  ],
  "id": "http://example.gov/credentials/3732",
  "type": [
    "VerifiableCredential",
    "BevoegdheidUittreksel"
  ],
  "issuer": {
    "id": "did:web:vc.transmute.world"
  },
  "issuanceDate": "2020-03-10T04:24:12.164Z",
  "credentialSubject": {
    "id": "did:example:ebfeb1f712ebc6f1c276e12ec21",
    "kvkNummer": "",
    "naam":  "",
    "persoonRechtsvorm": "",
    "functionarisNaam":  "",
    "functie": "",
    "soortBevoegdheid": ""
  }
}
',
    issuer_did_id = @issuer_did_id
WHERE wallet_id = @walletId
  and credential_configuration_id = 'BevoegdheidUittreksel_jwt_vc_json';

INSERT IGNORE INTO credential_issue_template (wallet_id, credential_configuration_id, display_name, template, issuer_did_id)
VALUES (@walletId, 'LPID_jwt_vc_json', '', '{}', @issuer_did_id);

UPDATE credential_issue_template
SET display_name         = 'LPID VC template',
    template             = '{
  "@context": [
    "https://www.w3.org/2018/credentials/v1",
    "https://www.w3.org/2018/credentials/examples/v1"
  ],
  "id": "http://example.gov/credentials/3732",
  "type": [
    "VerifiableCredential",
    "VerifiableAttestation",
    "PersonIdentificationData"
  ],
  "issuer": {
    "id": "did:web:vc.transmute.world"
  },
  "issuanceDate": "2020-03-10T04:24:12.164Z",
  "credentialSubject": {
    "id": "did:example:ebfeb1f712ebc6f1c276e12ec21",
    "legal_person_identifier": "",
    "legal_person_name":  ""
  }
}
',
    issuer_did_id = @issuer_did_id
WHERE wallet_id = @walletId
  and credential_configuration_id = 'LPID_jwt_vc_json';

INSERT IGNORE INTO credential_issue_template (wallet_id, credential_configuration_id, display_name, template, issuer_did_id)
VALUES (@walletId, 'UBO_jwt_vc_json', '', '{}', @issuer_did_id);

UPDATE credential_issue_template
SET display_name         = 'UBO VC template',
    template             = '{
  "@context": [
    "https://www.w3.org/2018/credentials/v1",
    "https://www.w3.org/2018/credentials/examples/v1"
  ],
  "id": "http://example.gov/credentials/3732",
  "type": [
    "VerifiableCredential",
    "VerifiableAttestation",
    "UltimateBeneficiaryOwner"
  ],
  "issuer": {
    "id": "did:web:vc.transmute.world"
  },
  "issuanceDate": "2020-03-10T04:24:12.164Z",
  "credentialSubject": {
    "id": "did:example:ebfeb1f712ebc6f1c276e12ec21"
  }
}
',
    issuer_did_id = @issuer_did_id
WHERE wallet_id = @walletId
  and credential_configuration_id = 'UBO_jwt_vc_json';

# delete
# from credential
# where wallet_id = @walletId;

# insert into credential (wallet_id, credential_format, document, disclosures, metadata, credential_configuration_id, credential_configuration, valid_from, valid_until, issuance_date,
#                         issuer_id, created_at, created_by,
#                         last_modified_at,
#                         last_modified_by, search_data)
# values (@walletId, 'JWT',
#         'eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCIsImtpZCI6ImRpZDpqd2s6ZXlKcmRIa2lPaUpQUzFBaUxDSmpjbllpT2lKRlpESTFOVEU1SWl3aWEybGtJam9pZURsaVpuQm5UMXBHTTFJemMwcFViMWRzVURsbFRYUjRTVEJUTlZvNVRqTTRSSFZsVkRKRmVEbEtaeUlzSW5naU9pSnNkRE53YUhaRFNFNXFZamd5UnpWaFFtNUZiMDR4WkV0NGRYVldPVzlMUmpKd1NubFpTMkpaYkdrMEluMCJ9.eyJpc3MiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaWVEbGlabkJuVDFwR00xSXpjMHBVYjFkc1VEbGxUWFI0U1RCVE5WbzVUak00UkhWbFZESkZlRGxLWnlJc0luZ2lPaUpzZEROd2FIWkRTRTVxWWpneVJ6VmhRbTVGYjA0eFpFdDRkWFZXT1c5TFJqSndTbmxaUzJKWmJHazBJbjAiLCJzdWIiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVUwbEVaalZsUWtoWFVsOUdaME5hUmpSZlpYTk9VR2R3ZUZvME1UUktNR3QxVVdzNU1GVkhXWFU0Y3lJc0luZ2lPaUl5Wld4Vk5rVlhha3hCVERCT01WcDZXRGhhYkdwT1luWTFUM3BxYmtwa1IzaHlZVTh3TkVKcFNGTm5JbjAiLCJ2YyI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL2V4YW1wbGVzL3YxIl0sImlkIjoidXJuOnV1aWQ6MmNmNmM5N2MtYmJiOS00YTEwLWE1MGMtMTNiMmQwMGI4MjY3IiwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlZlcmlmaWFibGVBdHRlc3RhdGlvbiIsIktWS1JlZ2lzdHJhdGlvbiJdLCJpc3N1ZXIiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaWVEbGlabkJuVDFwR00xSXpjMHBVYjFkc1VEbGxUWFI0U1RCVE5WbzVUak00UkhWbFZESkZlRGxLWnlJc0luZ2lPaUpzZEROd2FIWkRTRTVxWWpneVJ6VmhRbTVGYjA0eFpFdDRkWFZXT1c5TFJqSndTbmxaUzJKWmJHazBJbjAiLCJpc3N1YW5jZURhdGUiOiIyMDI0LTA1LTA4VDExOjEzOjUzLjg1MDUxODY2M1oiLCJjcmVkZW50aWFsU3ViamVjdCI6eyJpZCI6ImRpZDpqd2s6ZXlKcmRIa2lPaUpQUzFBaUxDSmpjbllpT2lKRlpESTFOVEU1SWl3aWEybGtJam9pVTBsRVpqVmxRa2hYVWw5R1owTmFSalJmWlhOT1VHZHdlRm8wTVRSS01HdDFVV3M1TUZWSFdYVTRjeUlzSW5naU9pSXlaV3hWTmtWWGFreEJUREJPTVZwNldEaGFiR3BPWW5ZMVQzcHFia3BrUjNoeVlVOHdORUpwU0ZObkluMCIsImt2a051bW1lciI6IjkwMDAxMDAxIiwibmFhbSI6IkRlbW8gMSIsInJlY2h0c3Zvcm0iOiJCZXNsb3RlbiBWZW5ub290c2NoYXAiLCJzdGFydGRhdHVtIjpbMjAyMiw3LDFdLCJlaW5kZGF0dW0iOiIifX0sImp0aSI6InVybjp1dWlkOjJjZjZjOTdjLWJiYjktNGExMC1hNTBjLTEzYjJkMDBiODI2NyIsImlhdCI6MTcxNTE2NjgzMywibmJmIjoxNzE1MTY2ODMzfQ.GiEDsON6SQ0wVPv1fwxgf6FfkCLNKX_8EB-DiimSdQZCu_noDqktadhNTXqYbqYEuxuAUIJlA87H2LoRyqS9AA',
#         '{}', '{}', 'KVKRegistration_jwt_vc_json',
#         '{"issuer": "https://mijnkvk.acc.credenco.com", "authorization_endpoint": "https://mijnkvk.acc.credenco.com/authorize", "pushed_authorization_request_endpoint": "https://mijnkvk.acc.credenco.com/par", "token_endpoint": "https://mijnkvk.acc.credenco.com/token", "jwks_uri": "https://mijnkvk.acc.credenco.com/jwks", "scopes_supported": ["openid"], "response_types_supported": ["code", "vp_token", "id_token"], "response_modes_supported": ["query", "fragment"], "grant_types_supported": ["authorization_code", "urn:ietf:params:oauth:grant-type:pre-authorized_code"], "subject_types_supported": ["public"], "id_token_signing_alg_values_supported": ["ES256"], "credential_issuer": "https://mijnkvk.acc.credenco.com", "credential_endpoint": "https://mijnkvk.acc.credenco.com/credential", "credential_configurations_supported": {"KVKRegistration_jwt_vc_json": {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "KVK Registratie", "background_color": "#e6f2f5", "text_color": "#00526e", "logo": {"url": "https://mijnkvk.acc.credenco.com/kvk_logo.png", "alt_text": "KVK Logo"}}, {"name": "KVK Registratie", "locale": "nl-NL"}, {"name": "Chamber of Commerce Registration", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "KVKRegistration"], "credentialSubject": {"kvkNummer": {"mandatory": true, "display": [{"name": "KVK nummer"}, {"name": "KVK nummer", "locale": "nl-NL"}, {"name": "CoC number", "locale": "en-US"}]}, "naam": {"mandatory": true, "display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}, "rechtsvorm": {"display": [{"name": "Rechtsvorm"}, {"name": "Rechtsvorm", "locale": "nl-NL"}, {"name": "Legal form", "locale": "en-US"}]}, "startdatum": {"mandatory": true, "display": [{"name": "Startdatum"}, {"name": "Startdatum", "locale": "nl-NL"}, {"name": "Start date", "locale": "en-US"}]}, "einddatum": {"display": [{"name": "Einddatum"}, {"name": "Einddatum", "locale": "nl-NL"}, {"name": "End date", "locale": "en-US"}]}}}}, "RSIN_jwt_vc_json": {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "RSIN"], "credentialSubject": {"rsin": {"display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}]}, "naam": {"display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}}}}}, "batch_credential_endpoint": "https://mijnkvk.acc.credenco.com/batch_credential", "deferred_credential_endpoint": "https://mijnkvk.acc.credenco.com/credential_deferred", "display": [{"name": "Kamer van Koophandel", "logo": {"url": "https://mijnkvk.acc.credenco.com/kvk_logo.png"}}, {"name": "Kamer van Koophandel", "locale": "nl-NL"}, {"name": "Chamber of Commerce", "locale": "en-US"}], "authorization_server": "https://mijnkvk.acc.credenco.com", "credentials_supported": [{"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "KVK Registratie"}, {"name": "KVK Registratie", "locale": "nl-NL"}, {"name": "Chamber of Commerce Registration", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "KVKRegistration"], "credentialSubject": {"kvkNummer": {"mandatory": true, "display": [{"name": "KVK nummer"}, {"name": "KVK nummer", "locale": "nl-NL"}, {"name": "CoC number", "locale": "en-US"}]}, "naam": {"mandatory": true, "display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}, "rechtsvorm": {"display": [{"name": "Rechtsvorm"}, {"name": "Rechtsvorm", "locale": "nl-NL"}, {"name": "Legal form", "locale": "en-US"}]}, "startdatum": {"mandatory": true, "display": [{"name": "Startdatum"}, {"name": "Startdatum", "locale": "nl-NL"}, {"name": "Start date", "locale": "en-US"}]}, "einddatum": {"display": [{"name": "Einddatum"}, {"name": "Einddatum", "locale": "nl-NL"}, {"name": "End date", "locale": "en-US"}]}}}}, {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "RSIN"], "credentialSubject": {"rsin": {"display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}]}, "naam": {"display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}}}}]}'
#             , '2024-05-09 12:20', null, '2024-05-09 12:20', @issuer_id, now(), 'System', now(), 'System', '');
# insert into credential (wallet_id, credential_format, document, disclosures, metadata, credential_configuration_id, credential_configuration, valid_from, valid_until, issuance_date,
#                         issuer_id, created_at, created_by,
#                         last_modified_at,
#                         last_modified_by, search_data)
# values (@walletId, 'JWT',
#         'eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCIsImtpZCI6ImRpZDpqd2s6ZXlKcmRIa2lPaUpQUzFBaUxDSmpjbllpT2lKRlpESTFOVEU1SWl3aWEybGtJam9pZURsaVpuQm5UMXBHTTFJemMwcFViMWRzVURsbFRYUjRTVEJUTlZvNVRqTTRSSFZsVkRKRmVEbEtaeUlzSW5naU9pSnNkRE53YUhaRFNFNXFZamd5UnpWaFFtNUZiMDR4WkV0NGRYVldPVzlMUmpKd1NubFpTMkpaYkdrMEluMCJ9.eyJpc3MiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaWVEbGlabkJuVDFwR00xSXpjMHBVYjFkc1VEbGxUWFI0U1RCVE5WbzVUak00UkhWbFZESkZlRGxLWnlJc0luZ2lPaUpzZEROd2FIWkRTRTVxWWpneVJ6VmhRbTVGYjA0eFpFdDRkWFZXT1c5TFJqSndTbmxaUzJKWmJHazBJbjAiLCJzdWIiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVUwbEVaalZsUWtoWFVsOUdaME5hUmpSZlpYTk9VR2R3ZUZvME1UUktNR3QxVVdzNU1GVkhXWFU0Y3lJc0luZ2lPaUl5Wld4Vk5rVlhha3hCVERCT01WcDZXRGhhYkdwT1luWTFUM3BxYmtwa1IzaHlZVTh3TkVKcFNGTm5JbjAiLCJ2YyI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL2V4YW1wbGVzL3YxIl0sImlkIjoidXJuOnV1aWQ6YjM1YzUwNTEtYTNkNC00NjM5LThmM2MtMDA2Yzc2MDdkNjE2IiwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlZlcmlmaWFibGVBdHRlc3RhdGlvbiIsIlJTSU4iXSwiaXNzdWVyIjoiZGlkOmp3azpleUpyZEhraU9pSlBTMUFpTENKamNuWWlPaUpGWkRJMU5URTVJaXdpYTJsa0lqb2llRGxpWm5CblQxcEdNMUl6YzBwVWIxZHNVRGxsVFhSNFNUQlROVm81VGpNNFJIVmxWREpGZURsS1p5SXNJbmdpT2lKc2RETndhSFpEU0U1cVlqZ3lSelZoUW01RmIwNHhaRXQ0ZFhWV09XOUxSakp3U25sWlMySlpiR2swSW4wIiwiaXNzdWFuY2VEYXRlIjoiMjAyNC0wNS0wOFQxMToxODoyNy43NDc5Njc4MTlaIiwiY3JlZGVudGlhbFN1YmplY3QiOnsiaWQiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVUwbEVaalZsUWtoWFVsOUdaME5hUmpSZlpYTk9VR2R3ZUZvME1UUktNR3QxVVdzNU1GVkhXWFU0Y3lJc0luZ2lPaUl5Wld4Vk5rVlhha3hCVERCT01WcDZXRGhhYkdwT1luWTFUM3BxYmtwa1IzaHlZVTh3TkVKcFNGTm5JbjAiLCJyc2luIjoiNTYwMDEwMTAxMCIsIm5hYW0iOiJEZW1vIDEifX0sImp0aSI6InVybjp1dWlkOmIzNWM1MDUxLWEzZDQtNDYzOS04ZjNjLTAwNmM3NjA3ZDYxNiIsImlhdCI6MTcxNTE2NzEwNywibmJmIjoxNzE1MTY3MTA3fQ.7nvWZejuoIZMhS2NdSGvC5qNbklb1FW3TwBqbQ0_OmJE9EyH2MgwbkKzfqCrO6UFabE91KzY76oxfECFQWZfCA',
#         '{}', '{}', 'RSIN_jwt_vc_json',
#         '{"issuer": "https://mijnkvk.acc.credenco.com", "authorization_endpoint": "https://mijnkvk.acc.credenco.com/authorize", "pushed_authorization_request_endpoint": "https://mijnkvk.acc.credenco.com/par", "token_endpoint": "https://mijnkvk.acc.credenco.com/token", "jwks_uri": "https://mijnkvk.acc.credenco.com/jwks", "scopes_supported": ["openid"], "response_types_supported": ["code", "vp_token", "id_token"], "response_modes_supported": ["query", "fragment"], "grant_types_supported": ["authorization_code", "urn:ietf:params:oauth:grant-type:pre-authorized_code"], "subject_types_supported": ["public"], "id_token_signing_alg_values_supported": ["ES256"], "credential_issuer": "https://mijnkvk.acc.credenco.com", "credential_endpoint": "https://mijnkvk.acc.credenco.com/credential", "credential_configurations_supported": {"KVKRegistration_jwt_vc_json": {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "KVK Registratie"}, {"name": "KVK Registratie", "locale": "nl-NL"}, {"name": "Chamber of Commerce Registration", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "KVKRegistration"], "credentialSubject": {"kvkNummer": {"mandatory": true, "display": [{"name": "KVK nummer"}, {"name": "KVK nummer", "locale": "nl-NL"}, {"name": "CoC number", "locale": "en-US"}]}, "naam": {"mandatory": true, "display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}, "rechtsvorm": {"display": [{"name": "Rechtsvorm"}, {"name": "Rechtsvorm", "locale": "nl-NL"}, {"name": "Legal form", "locale": "en-US"}]}, "startdatum": {"mandatory": true, "display": [{"name": "Startdatum"}, {"name": "Startdatum", "locale": "nl-NL"}, {"name": "Start date", "locale": "en-US"}]}, "einddatum": {"display": [{"name": "Einddatum"}, {"name": "Einddatum", "locale": "nl-NL"}, {"name": "End date", "locale": "en-US"}]}}}}, "RSIN_jwt_vc_json": {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "RSIN", "background_image": {"url": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAARAAAACqCAYAAABlLBCyAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAkHSURBVHgB7dldjuO2EobhqsbZ31leNpS1ZAEBcpvKtCWSVUWyB/iu3weYsWxRFMWfT2rb//r7nzAAEHwZAIgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABIPvfn///49dLmLuV18f73uP96Pv9s28vF7PSuf3r1W2V8+9Xz9ujnlHm+efvsfU87fNxzDhHbn88r/E5Jlab3n29DavusT3a/5YZbU19ErnP+ud2uX6PuZ33Hfu8telz/kjt9VV+1d3G5fscsfo5oo/fv792+dsfq22lXz4F/730116+vn+28xjatc9stqOORe2Tdb2jX2ofrzlr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", "alt_text": "KVK styled card background image"}}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "RSIN"], "credentialSubject": {"rsin": {"display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}]}, "naam": {"display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}}}}}, "batch_credential_endpoint": "https://mijnkvk.acc.credenco.com/batch_credential", "deferred_credential_endpoint": "https://mijnkvk.acc.credenco.com/credential_deferred", "display": [{"name": "Kamer van Koophandel", "logo": {"url": "https://mijnkvk.acc.credenco.com/kvk_logo.png"}}, {"name": "Kamer van Koophandel", "locale": "nl-NL"}, {"name": "Chamber of Commerce", "locale": "en-US"}], "authorization_server": "https://mijnkvk.acc.credenco.com", "credentials_supported": [{"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "KVK Registratie"}, {"name": "KVK Registratie", "locale": "nl-NL"}, {"name": "Chamber of Commerce Registration", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "KVKRegistration"], "credentialSubject": {"kvkNummer": {"mandatory": true, "display": [{"name": "KVK nummer"}, {"name": "KVK nummer", "locale": "nl-NL"}, {"name": "CoC number", "locale": "en-US"}]}, "naam": {"mandatory": true, "display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}, "rechtsvorm": {"display": [{"name": "Rechtsvorm"}, {"name": "Rechtsvorm", "locale": "nl-NL"}, {"name": "Legal form", "locale": "en-US"}]}, "startdatum": {"mandatory": true, "display": [{"name": "Startdatum"}, {"name": "Startdatum", "locale": "nl-NL"}, {"name": "Start date", "locale": "en-US"}]}, "einddatum": {"display": [{"name": "Einddatum"}, {"name": "Einddatum", "locale": "nl-NL"}, {"name": "End date", "locale": "en-US"}]}}}}, {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "RSIN", "description": "Rechtspersonen Samenwerkingsverbanden Informatie Nummer", "text_color": "#FFFFFF"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "RSIN"], "credentialSubject": {"rsin": {"display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}]}, "naam": {"display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}}}}]}'
#            , '2024-05-10 17:56', null, '2024-05-10 10:20', @issuer_id, now(), 'System', now(), 'System', '');
#
# insert into credential (wallet_id, credential_format, document, disclosures, metadata, credential_configuration_id, credential_configuration, valid_from, valid_until, issuance_date,
#                         issuer_id, created_at, created_by,
#                         last_modified_at,
#                         last_modified_by, search_data)
# values (@walletId, 'JWT',
#         'eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCIsImtpZCI6ImRpZDpqd2s6ZXlKcmRIa2lPaUpQUzFBaUxDSmpjbllpT2lKRlpESTFOVEU1SWl3aWEybGtJam9pZURsaVpuQm5UMXBHTTFJemMwcFViMWRzVURsbFRYUjRTVEJUTlZvNVRqTTRSSFZsVkRKRmVEbEtaeUlzSW5naU9pSnNkRE53YUhaRFNFNXFZamd5UnpWaFFtNUZiMDR4WkV0NGRYVldPVzlMUmpKd1NubFpTMkpaYkdrMEluMCJ9.eyJpc3MiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaWVEbGlabkJuVDFwR00xSXpjMHBVYjFkc1VEbGxUWFI0U1RCVE5WbzVUak00UkhWbFZESkZlRGxLWnlJc0luZ2lPaUpzZEROd2FIWkRTRTVxWWpneVJ6VmhRbTVGYjA0eFpFdDRkWFZXT1c5TFJqSndTbmxaUzJKWmJHazBJbjAiLCJzdWIiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVUwbEVaalZsUWtoWFVsOUdaME5hUmpSZlpYTk9VR2R3ZUZvME1UUktNR3QxVVdzNU1GVkhXWFU0Y3lJc0luZ2lPaUl5Wld4Vk5rVlhha3hCVERCT01WcDZXRGhhYkdwT1luWTFUM3BxYmtwa1IzaHlZVTh3TkVKcFNGTm5JbjAiLCJ2YyI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL2V4YW1wbGVzL3YxIl0sImlkIjoidXJuOnV1aWQ6YjM1YzUwNTEtYTNkNC00NjM5LThmM2MtMDA2Yzc2MDdkNjE2IiwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlZlcmlmaWFibGVBdHRlc3RhdGlvbiIsIlJTSU4iXSwiaXNzdWVyIjoiZGlkOmp3azpleUpyZEhraU9pSlBTMUFpTENKamNuWWlPaUpGWkRJMU5URTVJaXdpYTJsa0lqb2llRGxpWm5CblQxcEdNMUl6YzBwVWIxZHNVRGxsVFhSNFNUQlROVm81VGpNNFJIVmxWREpGZURsS1p5SXNJbmdpT2lKc2RETndhSFpEU0U1cVlqZ3lSelZoUW01RmIwNHhaRXQ0ZFhWV09XOUxSakp3U25sWlMySlpiR2swSW4wIiwiaXNzdWFuY2VEYXRlIjoiMjAyNC0wNS0wOFQxMToxODoyNy43NDc5Njc4MTlaIiwiY3JlZGVudGlhbFN1YmplY3QiOnsiaWQiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVUwbEVaalZsUWtoWFVsOUdaME5hUmpSZlpYTk9VR2R3ZUZvME1UUktNR3QxVVdzNU1GVkhXWFU0Y3lJc0luZ2lPaUl5Wld4Vk5rVlhha3hCVERCT01WcDZXRGhhYkdwT1luWTFUM3BxYmtwa1IzaHlZVTh3TkVKcFNGTm5JbjAiLCJyc2luIjoiNTYwMDEwMTAxMCIsIm5hYW0iOiJEZW1vIDEifX0sImp0aSI6InVybjp1dWlkOmIzNWM1MDUxLWEzZDQtNDYzOS04ZjNjLTAwNmM3NjA3ZDYxNiIsImlhdCI6MTcxNTE2NzEwNywibmJmIjoxNzE1MTY3MTA3fQ.7nvWZejuoIZMhS2NdSGvC5qNbklb1FW3TwBqbQ0_OmJE9EyH2MgwbkKzfqCrO6UFabE91KzY76oxfECFQWZfCA',
#         '{}', '{}', 'RSIN_jwt_vc_json',
#         '{"issuer": "https://mijnkvk.acc.credenco.com", "authorization_endpoint": "https://mijnkvk.acc.credenco.com/authorize", "pushed_authorization_request_endpoint": "https://mijnkvk.acc.credenco.com/par", "token_endpoint": "https://mijnkvk.acc.credenco.com/token", "jwks_uri": "https://mijnkvk.acc.credenco.com/jwks", "scopes_supported": ["openid"], "response_types_supported": ["code", "vp_token", "id_token"], "response_modes_supported": ["query", "fragment"], "grant_types_supported": ["authorization_code", "urn:ietf:params:oauth:grant-type:pre-authorized_code"], "subject_types_supported": ["public"], "id_token_signing_alg_values_supported": ["ES256"], "credential_issuer": "https://mijnkvk.acc.credenco.com", "credential_endpoint": "https://mijnkvk.acc.credenco.com/credential", "credential_configurations_supported": {"KVKRegistration_jwt_vc_json": {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "KVK Registratie"}, {"name": "KVK Registratie", "locale": "nl-NL"}, {"name": "Chamber of Commerce Registration", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "KVKRegistration"], "credentialSubject": {"kvkNummer": {"mandatory": true, "display": [{"name": "KVK nummer"}, {"name": "KVK nummer", "locale": "nl-NL"}, {"name": "CoC number", "locale": "en-US"}]}, "naam": {"mandatory": true, "display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}, "rechtsvorm": {"display": [{"name": "Rechtsvorm"}, {"name": "Rechtsvorm", "locale": "nl-NL"}, {"name": "Legal form", "locale": "en-US"}]}, "startdatum": {"mandatory": true, "display": [{"name": "Startdatum"}, {"name": "Startdatum", "locale": "nl-NL"}, {"name": "Start date", "locale": "en-US"}]}, "einddatum": {"display": [{"name": "Einddatum"}, {"name": "Einddatum", "locale": "nl-NL"}, {"name": "End date", "locale": "en-US"}]}}}}, "RSIN_jwt_vc_json": {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "Conference Attendee", "description": "The DBC Conference Attendee credential is given to all visitors of the DBC conference.", "background_color": "#3B6F6D", "text_color": "#FFFFFF", "logo": {"url": "https://dutchblockchaincoalition.org/assets/images/icons/Logo-DBC.png", "alt_text": "An orange block shape, with the text Dutch Blockchain Coalition next to it, portraying the logo of the Dutch Blockchain Coalition."}, "background_image": {"url": "https://i.ibb.co/CHqjxrJ/dbc-card-hig-res.png", "alt_text": "Connected open cubes in blue with one orange cube as a background of the card"}}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "RSIN"], "credentialSubject": {"rsin": {"display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}]}, "naam": {"display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}}}}}, "batch_credential_endpoint": "https://mijnkvk.acc.credenco.com/batch_credential", "deferred_credential_endpoint": "https://mijnkvk.acc.credenco.com/credential_deferred", "display": [{"name": "Dutch Blockchain Coalition", "logo": {"url": "https://dutchblockchaincoalition.org/assets/images/icons/Logo-DBC.png", "alt_text": "An orange block shape, with the text Dutch Blockchain Coalition next to it, portraying the logo of the Dutch Blockchain Coalition."}}], "authorization_server": "https://mijnkvk.acc.credenco.com", "credentials_supported": [{"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "KVK Registratie"}, {"name": "KVK Registratie", "locale": "nl-NL"}, {"name": "Chamber of Commerce Registration", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "KVKRegistration"], "credentialSubject": {"kvkNummer": {"mandatory": true, "display": [{"name": "KVK nummer"}, {"name": "KVK nummer", "locale": "nl-NL"}, {"name": "CoC number", "locale": "en-US"}]}, "naam": {"mandatory": true, "display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}, "rechtsvorm": {"display": [{"name": "Rechtsvorm"}, {"name": "Rechtsvorm", "locale": "nl-NL"}, {"name": "Legal form", "locale": "en-US"}]}, "startdatum": {"mandatory": true, "display": [{"name": "Startdatum"}, {"name": "Startdatum", "locale": "nl-NL"}, {"name": "Start date", "locale": "en-US"}]}, "einddatum": {"display": [{"name": "Einddatum"}, {"name": "Einddatum", "locale": "nl-NL"}, {"name": "End date", "locale": "en-US"}]}}}}, {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "RSIN"], "credentialSubject": {"rsin": {"display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}]}, "naam": {"display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}}}}]}'
#            , '2024-05-10 17:56', null, '2024-05-10 10:20', @issuer_id, now(), 'System', now(), 'System', '');
#
# insert into credential (wallet_id, credential_format, document, disclosures, metadata, credential_configuration_id, credential_configuration, valid_from, valid_until, issuance_date,
#                         issuer_id, created_at, created_by,
#                         last_modified_at,
#                         last_modified_by, search_data)
# values (@walletId, 'JWT',
#         'eyJhbGciOiJFZERTQSIsInR5cCI6IkpXVCIsImtpZCI6ImRpZDpqd2s6ZXlKcmRIa2lPaUpQUzFBaUxDSmpjbllpT2lKRlpESTFOVEU1SWl3aWEybGtJam9pZURsaVpuQm5UMXBHTTFJemMwcFViMWRzVURsbFRYUjRTVEJUTlZvNVRqTTRSSFZsVkRKRmVEbEtaeUlzSW5naU9pSnNkRE53YUhaRFNFNXFZamd5UnpWaFFtNUZiMDR4WkV0NGRYVldPVzlMUmpKd1NubFpTMkpaYkdrMEluMCJ9.eyJpc3MiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaWVEbGlabkJuVDFwR00xSXpjMHBVYjFkc1VEbGxUWFI0U1RCVE5WbzVUak00UkhWbFZESkZlRGxLWnlJc0luZ2lPaUpzZEROd2FIWkRTRTVxWWpneVJ6VmhRbTVGYjA0eFpFdDRkWFZXT1c5TFJqSndTbmxaUzJKWmJHazBJbjAiLCJzdWIiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVUwbEVaalZsUWtoWFVsOUdaME5hUmpSZlpYTk9VR2R3ZUZvME1UUktNR3QxVVdzNU1GVkhXWFU0Y3lJc0luZ2lPaUl5Wld4Vk5rVlhha3hCVERCT01WcDZXRGhhYkdwT1luWTFUM3BxYmtwa1IzaHlZVTh3TkVKcFNGTm5JbjAiLCJ2YyI6eyJAY29udGV4dCI6WyJodHRwczovL3d3dy53My5vcmcvMjAxOC9jcmVkZW50aWFscy92MSIsImh0dHBzOi8vd3d3LnczLm9yZy8yMDE4L2NyZWRlbnRpYWxzL2V4YW1wbGVzL3YxIl0sImlkIjoidXJuOnV1aWQ6YjM1YzUwNTEtYTNkNC00NjM5LThmM2MtMDA2Yzc2MDdkNjE2IiwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlZlcmlmaWFibGVBdHRlc3RhdGlvbiIsIlJTSU4iXSwiaXNzdWVyIjoiZGlkOmp3azpleUpyZEhraU9pSlBTMUFpTENKamNuWWlPaUpGWkRJMU5URTVJaXdpYTJsa0lqb2llRGxpWm5CblQxcEdNMUl6YzBwVWIxZHNVRGxsVFhSNFNUQlROVm81VGpNNFJIVmxWREpGZURsS1p5SXNJbmdpT2lKc2RETndhSFpEU0U1cVlqZ3lSelZoUW01RmIwNHhaRXQ0ZFhWV09XOUxSakp3U25sWlMySlpiR2swSW4wIiwiaXNzdWFuY2VEYXRlIjoiMjAyNC0wNS0wOFQxMToxODoyNy43NDc5Njc4MTlaIiwiY3JlZGVudGlhbFN1YmplY3QiOnsiaWQiOiJkaWQ6andrOmV5SnJkSGtpT2lKUFMxQWlMQ0pqY25ZaU9pSkZaREkxTlRFNUlpd2lhMmxrSWpvaVUwbEVaalZsUWtoWFVsOUdaME5hUmpSZlpYTk9VR2R3ZUZvME1UUktNR3QxVVdzNU1GVkhXWFU0Y3lJc0luZ2lPaUl5Wld4Vk5rVlhha3hCVERCT01WcDZXRGhhYkdwT1luWTFUM3BxYmtwa1IzaHlZVTh3TkVKcFNGTm5JbjAiLCJyc2luIjoiNTYwMDEwMTAxMCIsIm5hYW0iOiJEZW1vIDEifX0sImp0aSI6InVybjp1dWlkOmIzNWM1MDUxLWEzZDQtNDYzOS04ZjNjLTAwNmM3NjA3ZDYxNiIsImlhdCI6MTcxNTE2NzEwNywibmJmIjoxNzE1MTY3MTA3fQ.7nvWZejuoIZMhS2NdSGvC5qNbklb1FW3TwBqbQ0_OmJE9EyH2MgwbkKzfqCrO6UFabE91KzY76oxfECFQWZfCA',
#         '{}', '{}', 'RSIN_jwt_vc_json',
#         '{"issuer": "https://mijnkvk.acc.credenco.com", "authorization_endpoint": "https://mijnkvk.acc.credenco.com/authorize", "pushed_authorization_request_endpoint": "https://mijnkvk.acc.credenco.com/par", "token_endpoint": "https://mijnkvk.acc.credenco.com/token", "jwks_uri": "https://mijnkvk.acc.credenco.com/jwks", "scopes_supported": ["openid"], "response_types_supported": ["code", "vp_token", "id_token"], "response_modes_supported": ["query", "fragment"], "grant_types_supported": ["authorization_code", "urn:ietf:params:oauth:grant-type:pre-authorized_code"], "subject_types_supported": ["public"], "id_token_signing_alg_values_supported": ["ES256"], "credential_issuer": "https://mijnkvk.acc.credenco.com", "credential_endpoint": "https://mijnkvk.acc.credenco.com/credential", "credential_configurations_supported": {"KVKRegistration_jwt_vc_json": {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "KVK Registratie"}, {"name": "KVK Registratie", "locale": "nl-NL"}, {"name": "Chamber of Commerce Registration", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "KVKRegistration"], "credentialSubject": {"kvkNummer": {"mandatory": true, "display": [{"name": "KVK nummer"}, {"name": "KVK nummer", "locale": "nl-NL"}, {"name": "CoC number", "locale": "en-US"}]}, "naam": {"mandatory": true, "display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}, "rechtsvorm": {"display": [{"name": "Rechtsvorm"}, {"name": "Rechtsvorm", "locale": "nl-NL"}, {"name": "Legal form", "locale": "en-US"}]}, "startdatum": {"mandatory": true, "display": [{"name": "Startdatum"}, {"name": "Startdatum", "locale": "nl-NL"}, {"name": "Start date", "locale": "en-US"}]}, "einddatum": {"display": [{"name": "Einddatum"}, {"name": "Einddatum", "locale": "nl-NL"}, {"name": "End date", "locale": "en-US"}]}}}}, "RSIN_jwt_vc_json": {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "RSIN"], "credentialSubject": {"rsin": {"display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}]}, "naam": {"display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}}}}}, "batch_credential_endpoint": "https://mijnkvk.acc.credenco.com/batch_credential", "deferred_credential_endpoint": "https://mijnkvk.acc.credenco.com/credential_deferred", "display": [{"name": "Kamer van Koophandel"}, {"name": "Kamer van Koophandel", "locale": "nl-NL"}, {"name": "Chamber of Commerce", "locale": "en-US"}], "authorization_server": "https://mijnkvk.acc.credenco.com", "credentials_supported": [{"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "KVK Registratie"}, {"name": "KVK Registratie", "locale": "nl-NL"}, {"name": "Chamber of Commerce Registration", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "KVKRegistration"], "credentialSubject": {"kvkNummer": {"mandatory": true, "display": [{"name": "KVK nummer"}, {"name": "KVK nummer", "locale": "nl-NL"}, {"name": "CoC number", "locale": "en-US"}]}, "naam": {"mandatory": true, "display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}, "rechtsvorm": {"display": [{"name": "Rechtsvorm"}, {"name": "Rechtsvorm", "locale": "nl-NL"}, {"name": "Legal form", "locale": "en-US"}]}, "startdatum": {"mandatory": true, "display": [{"name": "Startdatum"}, {"name": "Startdatum", "locale": "nl-NL"}, {"name": "Start date", "locale": "en-US"}]}, "einddatum": {"display": [{"name": "Einddatum"}, {"name": "Einddatum", "locale": "nl-NL"}, {"name": "End date", "locale": "en-US"}]}}}}, {"format": "jwt_vc_json", "cryptographic_binding_methods_supported": ["did:web", "did:jwk", "did:ebsi"], "credential_signing_alg_values_supported": ["EdDSA", "ES256", "ES256K", "RSA"], "display": [{"name": "RSIN", "description": "Rechtspersonen Samenwerkingsverbanden Informatie Nummer", "text_color": "#FFFFFF", "background_image": {"url": "iVBORw0KGgoAAAANSUhEUgAAARAAAACqCAYAAABlLBCyAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAkHSURBVHgB7dldjuO2EobhqsbZ31leNpS1ZAEBcpvKtCWSVUWyB/iu3weYsWxRFMWfT2rb//r7nzAAEHwZAIgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABICNAAMgIEAAyAgSAjAABIPvfn///49dLmLuV18f73uP96Pv9s28vF7PSuf3r1W2V8+9Xz9ujnlHm+efvsfU87fNxzDhHbn88r/E5Jlab3n29DavusT3a/5YZbU19ErnP+ud2uX6PuZ33Hfu8telz/kjt9VV+1d3G5fscsfo5oo/fv792+dsfq22lXz4F/730116+vn+28xjatc9stqOORe2Tdb2jX2ofrzlr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", "alt_text": "KVK styled card background image"}}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}], "credential_definition": {"type": ["VerifiableCredential", "VerifiableAttestation", "RSIN"], "credentialSubject": {"rsin": {"display": [{"name": "RSIN"}, {"name": "RSIN", "locale": "nl-NL"}, {"name": "RSIN", "locale": "en-US"}]}, "naam": {"display": [{"name": "Naam"}, {"name": "Naam", "locale": "nl-NL"}, {"name": "Name", "locale": "en-US"}]}}}}]}'
#            , '2024-05-10 17:56', null, '2024-05-10 10:20', @issuer_id, now(), 'System', now(), 'System', '');

-- Issuer definition for company credentials
insert ignore into credential_issuer_definition (wallet_id, external_key, name, description, issuer_url, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'company', 'MijnKVK Company', '', 'https://mijnkvk.acc.credenco.com/company', now(), 'System', now(), 'System');

select LAST_INSERT_ID() into @credential_issuer_id;

insert ignore into credential_issuer_display (credential_issuer_id, display_name, logo_url, logo_alt_text, locale)
values
    (@credential_issuer_id, 'Kamer van Koophandel', 'https://mijnkvk.acc.credenco.com/kvk_logo.png', 'KvK logo', null),
    (@credential_issuer_id, 'Kamer van Koophandel', null, null, 'nl-NL'),
    (@credential_issuer_id, 'Chamber of Commerce', null, null, 'en-US');

insert ignore into credential_issuer_credential_definition (credential_issuer_id, credential_configuration_id, format)
values
    (@credential_issuer_id, 'KVKRegistration_jwt_vc_json', 'jwt_vc_json');

select LAST_INSERT_ID() into @credential_definition_id;

insert ignore into credential_issuer_credential_type (credential_definition_id, credential_type, type_order)
values
    (@credential_definition_id, 'VerifiableCredential', 1),
    (@credential_definition_id, 'VerifiableAttestation', 2),
    (@credential_definition_id, 'KVKRegistration', 3);

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'kvkNummer', 1, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'KVK nummer', null),
    (@credential_attribute_id, 'KVK nummer', 'nl-NL'),
    (@credential_attribute_id, 'CoC number', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'naam', 2, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Naam', null),
    (@credential_attribute_id, 'Naam', 'nl-NL'),
    (@credential_attribute_id, 'Name', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'rechtsvorm', 3, 0);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Rechtsvorm', null),
    (@credential_attribute_id, 'Rechtsvorm', 'nl-NL'),
    (@credential_attribute_id, 'Legal form', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'startdatum', 4, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Startdatum', null),
    (@credential_attribute_id, 'Startdatum', 'nl-NL'),
    (@credential_attribute_id, 'Start date', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'einddatum', 5, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Einddatum', null),
    (@credential_attribute_id, 'Einddatum', 'nl-NL'),
    (@credential_attribute_id, 'End date', 'en-US');

insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale)
values
    (@credential_definition_id, 'KVK Registratie', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', null),
    (@credential_definition_id, 'KVK Registratie', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', 'nl-NL'),
    (@credential_definition_id, 'Chamber of Commerce Registration', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK styled card Background', 'en-US');

insert ignore into credential_issuer_credential_definition (credential_issuer_id, credential_configuration_id, format)
values
    (@credential_issuer_id, 'RSIN_jwt_vc_json', 'jwt_vc_json');

select LAST_INSERT_ID() into @credential_definition_id;

insert ignore into credential_issuer_credential_type (credential_definition_id, credential_type, type_order)
values
    (@credential_definition_id, 'VerifiableCredential', 1),
    (@credential_definition_id, 'VerifiableAttestation', 2),
    (@credential_definition_id, 'RSIN', 3);

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'rsin', 1, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'RSIN', null),
    (@credential_attribute_id, 'RSIN', 'nl-NL'),
    (@credential_attribute_id, 'RSIN', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'naam', 2, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Naam', null),
    (@credential_attribute_id, 'Naam', 'nl-NL'),
    (@credential_attribute_id, 'Name', 'en-US');

insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale)
values
    (@credential_definition_id, 'RSIN', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', null),
    (@credential_definition_id, 'RSIN', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', 'nl-NL'),
    (@credential_definition_id, 'RSIN', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK styled card Background', 'en-US');

insert ignore into credential_issuer_credential_definition (credential_issuer_id, credential_configuration_id, format)
values
    (@credential_issuer_id, 'LPID_jwt_vc_json', 'jwt_vc_json');

select LAST_INSERT_ID() into @credential_definition_id;

insert ignore into credential_issuer_credential_type (credential_definition_id, credential_type, type_order)
values
    (@credential_definition_id, 'VerifiableCredential', 1),
    (@credential_definition_id, 'VerifiableAttestation', 2),
    (@credential_definition_id, 'PersonIdentificationData', 3);

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'legal_person_identifier', 1, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Legal person identifier', null),
    (@credential_attribute_id, 'Rechtspersoon id', 'nl-NL'),
    (@credential_attribute_id, 'Legal person identifier', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'legal_person_name', 2, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Legal person name', null),
    (@credential_attribute_id, 'Statutaire naam', 'nl-NL'),
    (@credential_attribute_id, 'Legal person name', 'en-US');

insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale)
values
    (@credential_definition_id, 'LPID', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', null),
    (@credential_definition_id, 'LPID', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', 'nl-NL'),
    (@credential_definition_id, 'LPID', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK styled card Background', 'en-US');

insert ignore into credential_issuer_credential_definition (credential_issuer_id, credential_configuration_id, format)
values
    (@credential_issuer_id, 'UBO_jwt_vc_json', 'jwt_vc_json');

select LAST_INSERT_ID() into @credential_definition_id;

insert ignore into credential_issuer_credential_type (credential_definition_id, credential_type, type_order)
values
    (@credential_definition_id, 'VerifiableCredential', 1),
    (@credential_definition_id, 'VerifiableAttestation', 2),
    (@credential_definition_id, 'UltimateBeneficiaryOwner', 3);

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'kvknummer', 1, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'KVK nummer', null),
    (@credential_attribute_id, 'KVK nummer', 'nl-NL'),
    (@credential_attribute_id, 'CoC number', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'inOnderzoek', 2, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'In onderzoek', null),
    (@credential_attribute_id, 'In onderzoek', 'nl-NL'),
    (@credential_attribute_id, 'Under investigation', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'aardEconomischBelang', 2, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Aard economisch belang', null),
    (@credential_attribute_id, 'Aard economisch belang', 'nl-NL'),
    (@credential_attribute_id, 'Nature of economic interest', 'en-US');

insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale)
values
    (@credential_definition_id, 'UBO', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', null),
    (@credential_definition_id, 'UBO', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', 'nl-NL'),
    (@credential_definition_id, 'UBO', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK styled card Background', 'en-US');

-- Issuer definition for personal credentials
insert ignore into credential_issuer_definition (wallet_id, external_key, name, description, issuer_url, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'personal', 'MijnKVK Personal', '', 'https://mijnkvk.acc.credenco.com/personal', now(), 'System', now(), 'System');

select LAST_INSERT_ID() into @credential_issuer_id;

insert ignore into credential_issuer_display (credential_issuer_id, display_name, logo_url, logo_alt_text, locale)
values
    (@credential_issuer_id, 'Kamer van Koophandel', 'https://mijnkvk.acc.credenco.com/kvk_logo.png', 'KvK logo', null),
    (@credential_issuer_id, 'Kamer van Koophandel', null, null, 'nl-NL'),
    (@credential_issuer_id, 'Chamber of Commerce', null, null, 'en-US');

insert ignore into credential_issuer_credential_definition (credential_issuer_id, credential_configuration_id, format)
values
    (@credential_issuer_id, 'BevoegdheidUittreksel_jwt_vc_json', 'jwt_vc_json');

select LAST_INSERT_ID() into @credential_definition_id;

insert ignore into credential_issuer_credential_type (credential_definition_id, credential_type, type_order)
values
    (@credential_definition_id, 'VerifiableCredential', 1),
    (@credential_definition_id, 'BevoegdheidUittreksel', 2);

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'kvkNummer', 1, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'KVK nummer', null),
    (@credential_attribute_id, 'KVK nummer', 'nl-NL'),
    (@credential_attribute_id, 'CoC number', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'naam', 2, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Naam', null),
    (@credential_attribute_id, 'Naam', 'nl-NL'),
    (@credential_attribute_id, 'Name', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'persoonRechtsvorm', 3, 0);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Rechtsvorm', null),
    (@credential_attribute_id, 'Rechtsvorm', 'nl-NL'),
    (@credential_attribute_id, 'Legal form', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'functionarisNaam', 4, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Functionaris naam', null),
    (@credential_attribute_id, 'Functionaris naam', 'nl-NL'),
    (@credential_attribute_id, 'Officer name', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'functie', 5, 0);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Functie', null),
    (@credential_attribute_id, 'Functie', 'nl-NL'),
    (@credential_attribute_id, 'Function', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'soortBevoegdheid', 6, 0);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Soort bevoegdheid', null),
    (@credential_attribute_id, 'Soort bevoegdheid', 'nl-NL'),
    (@credential_attribute_id, 'Type of authority', 'en-US');

insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale)
values
    (@credential_definition_id, 'Bevoegdheid uittreksel', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', null),
    (@credential_definition_id, 'Bevoegdheid uittreksel', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', 'nl-NL'),
    (@credential_definition_id, 'Authorized representative', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK styled card Background', 'en-US');



insert ignore into credential_issuer_definition (wallet_id, external_key, name, description, issuer_url, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'fake', 'Nep KVK Company', '', 'http://localhost:8088/public/df803dd4-32c6-4a69-afe5-d807c6701ed7/fake', now(), 'System', now(), 'System');

select LAST_INSERT_ID() into @credential_issuer_id;

insert ignore into credential_issuer_display (credential_issuer_id, display_name, logo_url, logo_alt_text, locale)
values
    (@credential_issuer_id, 'Phony handelsregister', 'https://mijnkvk.acc.credenco.com/kvk_logo.png', 'KvK logo', null),
    (@credential_issuer_id, 'Phony handelsregister', null, null, 'nl-NL'),
    (@credential_issuer_id, 'Phony business register', null, null, 'en-US');

insert ignore into credential_issuer_credential_definition (credential_issuer_id, credential_configuration_id, format)
values
    (@credential_issuer_id, 'FakeKVKRegistration_jwt_vc_json', 'jwt_vc_json');

select LAST_INSERT_ID() into @credential_definition_id;

insert ignore into credential_issuer_credential_type (credential_definition_id, credential_type, type_order)
values
    (@credential_definition_id, 'VerifiableCredential', 1),
    (@credential_definition_id, 'VerifiableAttestation', 2),
    (@credential_definition_id, 'KVKRegistration', 3);

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'kvkNummer', 1, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'KVK nummer', null),
    (@credential_attribute_id, 'KVK nummer', 'nl-NL'),
    (@credential_attribute_id, 'CoC number', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'naam', 2, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Naam', null),
    (@credential_attribute_id, 'Naam', 'nl-NL'),
    (@credential_attribute_id, 'Name', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'rechtsvorm', 3, 0);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Rechtsvorm', null),
    (@credential_attribute_id, 'Rechtsvorm', 'nl-NL'),
    (@credential_attribute_id, 'Legal form', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'startdatum', 4, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Startdatum', null),
    (@credential_attribute_id, 'Startdatum', 'nl-NL'),
    (@credential_attribute_id, 'Start date', 'en-US');

insert ignore into credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory)
values
    (@credential_definition_id, 'einddatum', 5, 1);

select LAST_INSERT_ID() into @credential_attribute_id;

insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale)
values
    (@credential_attribute_id, 'Einddatum', null),
    (@credential_attribute_id, 'Einddatum', 'nl-NL'),
    (@credential_attribute_id, 'End date', 'en-US');

insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale)
values
    (@credential_definition_id, 'Nep KVK Registratie', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', null),
    (@credential_definition_id, 'Nep KVK Registratie', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK gestylede achtergrond', 'nl-NL'),
    (@credential_definition_id, 'Fake Chamber of Commerce Registration', '#21436f', '#ffffff', 'https://mijnkvk.acc.credenco.com/kvk_card_background.png', 'KvK styled card Background', 'en-US');

INSERT IGNORE INTO credential_issue_template (id, wallet_id, credential_configuration_id, display_name, template, issuer_did_id, credential_issuer_id)
VALUES (23, @walletId, 'FakeKVKRegistration_jwt_vc_json', '', '{}', @issuer_did_id, @credential_issuer_id);

UPDATE credential_issue_template
SET display_name         = 'Fake KvK number VC template',
    template             = '{
                              "@context": [
                                "https://www.w3.org/2018/credentials/v1",
                                "https://www.w3.org/2018/credentials/examples/v1"
                              ],
                              "id": "http://example.gov/credentials/3732",
                              "type": [
                                "VerifiableCredential",
                                "VerifiableAttestation",
                                "KVKRegistration"
                              ],
                              "issuer": {
                                "id": "did:web:vc.transmute.world"
                              },
                              "issued": "<timestamp>",
                              "validFrom": "<timestamp>",
                              "issuanceDate": "<timestamp>",
                              "credentialSubject": {
                                "id": "did:example:ebfeb1f712ebc6f1c276e12ec21",
                                "kvkNummer": "",
                                "naam":  "",
                                "rechtsvorm": "",
                                "startdatum":  "",
                                "einddatum": ""
                              },
                              "termsOfUse": [{
                                "id": "https://api-pilot.ebsi.eu/trusted-issuers-registry/v5/issuers/did:ebsi:zivtiw8mjYWEV8vDWodTKoj/attributes/e9c28a971ce537d5681b159b05807850163fad7f391c5271d9c5be5688ac8f68",
                                "type": "IssuanceCertificate"
                              }],
                              "credentialSchema": {
                                "id": "https://api-pilot.ebsi.eu/trusted-schemas-registry/v3/schemas/0xd2204647818f9c0c93f64b70f1c892ea2a8e0a747ceaa2f77373b41996bb764d",
                                "type": "FullJsonSchemaValidator2021"
                              }
                            }
',
    issuer_did_id = @issuer_did_id
WHERE wallet_id = @walletId
  and credential_configuration_id = 'FakeKVKRegistration_jwt_vc_json';


INSERT INTO issuer (wallet_id, did, created_at, created_by, last_modified_at, last_modified_by, configuration_url, issue_url, configuration_content)
(select wallet.id,
        '',
        now(), 'System', now(), 'System',
        'http://localhost:8088/public/df803dd4-32c6-4a69-afe5-d807c6701ed7/fake/.well-known/openid-credential-issuer',
        'http://localhost:3003/issue',
        ''
from wallet
left join issuer on issuer.wallet_id = wallet.id and issuer.configuration_url = 'http://localhost:8088/public/df803dd4-32c6-4a69-afe5-d807c6701ed7/fake/.well-known/openid-credential-issuer'
where issuer.id is null);
