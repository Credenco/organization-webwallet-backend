INSERT IGNORE INTO wallet (external_key, display_name, api_key)
VALUES ('11618aee-d38b-4a35-b71d-51d50de98d6e', 'Auckland Notarissen', 'l7nAZ3aLRDZ680GynKpPTOFtlVPGs');

select @walletId := id
from wallet
where external_key = '11618aee-d38b-4a35-b71d-51d50de98d6e';

UPDATE wallet
SET display_name = 'Auckland Notarissen'
WHERE id = @walletId;

INSERT IGNORE INTO wallet_key (wallet_id, kid, display_name, document, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'Mu6efTLCimFk4Proh8uP5_F4bo5OVed6gYH5yEgqGls', 'JWT Wallet Key', '{"type":"jwk","jwk":"{\\"kty\\":\\"EC\\",\\"d\\":\\"iEKIquqJ0NLReKrFhAeEMzICmVsd8mwO6aGabaXgkgU\\",\\"crv\\":\\"P-256\\",\\"kid\\":\\"Mu6efTLCimFk4Proh8uP5_F4bo5OVed6gYH5yEgqGls\\",\\"x\\":\\"foZuswE0ZclU9NZs5KjDquFrD2HFQy7Ak8iMDKoRcPM\\",\\"y\\":\\"rVMqv1gJ-S1il8qJLMwmTZAwh8ZZKEP8sP37KXHHljQ\\"}"}', now(), 'System', now(), 'System');
select @wallet_key_id := id
from wallet_key
where wallet_id = @walletId
  and kid = 'Mu6efTLCimFk4Proh8uP5_F4bo5OVed6gYH5yEgqGls';

select @walletKeyId := id
from wallet_key
where wallet_id = @walletId
  and kid = 'Mu6efTLCimFk4Proh8uP5_F4bo5OVed6gYH5yEgqGls';

select @did := 'did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ';

INSERT INTO issuer (wallet_id, did, created_at, created_by, last_modified_at, last_modified_by, configuration_url, issue_url, configuration_content)
(select wallet.id,
        @did,
        now(), 'System', now(), 'System',
        'https://aucklandnotaris.acc.credenco.com/.well-known/openid-credential-issuer',
        'https://aucklandnotaris.acc.credenco.com/',
        ''
from wallet
left join issuer on issuer.wallet_id = wallet.id and issuer.did = @did
where issuer.id is null);



INSERT IGNORE INTO did (wallet_id, display_name, did, document, wallet_key_id, created_at, created_by, last_modified_at, last_modified_by, environment)
VALUES (11, 'JWK', @did, '{"@context":["https://www.w3.org/ns/did/v1","https://w3id.org/security/suites/jws-2020/v1"],"id":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ","verificationMethod":[{"id":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0","type":"JsonWebKey2020","controller":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ","publicKeyJwk":{"kty":"EC","crv":"P-256","kid":"KZpECBn0X8QCVya9e4QwI_fwR9CsG4R627J2vwL9G64","x":"I9w7QWk_6YTMHQbKQeylztb_mFCbzAuStEHk_YvhwhU","y":"XNU-XV6wm3qcAjUDoJ2p6oG5HvJo2X3MZMvRcp5uWrg"}}],"assertionMethod":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"],"authentication":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"],"capabilityInvocation":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"],"capabilityDelegation":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"],"keyAgreement":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"]}',
    @walletKeyId, '2024-06-28 07:34:32', 'auckland_admin', '2024-06-28 07:34:32', 'auckland_admin', null);

#
select @issuer_id := id
from issuer
where wallet_id = @walletId
  and did = @did;


INSERT IGNORE INTO did (wallet_id, display_name, did, document, wallet_key_id, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'DID Auckland Notarissen Credentials', 'did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ', '{"@context":["https://www.w3.org/ns/did/v1","https://w3id.org/security/suites/jws-2020/v1"],"id":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ","verificationMethod":[{"id":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0","type":"JsonWebKey2020","controller":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ","publicKeyJwk":{"kty":"OKP","crv":"Ed25519","kid":"BsmUragmz_pRBv1R0xSIq07UNZkEJqn6J_N22DklCbk","x":"eAnNnM1oLbyoQMSlqQvZgdBoJf2J8BXk2OPfilwcSzs"}}],"assertionMethod":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"],"authentication":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"],"capabilityInvocation":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"],"capabilityDelegation":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"],"keyAgreement":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiS1pwRUNCbjBYOFFDVnlhOWU0UXdJX2Z3UjlDc0c0UjYyN0oydndMOUc2NCIsIngiOiJJOXc3UVdrXzZZVE1IUWJLUWV5bHp0Yl9tRkNiekF1U3RFSGtfWXZod2hVIiwieSI6IlhOVS1YVjZ3bTNxY0FqVURvSjJwNm9HNUh2Sm8yWDNNWk12UmNwNXVXcmcifQ#0"]}', @wallet_key_id, now(), 'System', now(), 'System');

select @issuer_did_id := id
from did
where wallet_id = @walletId
  and did = @did;




-- Issuer definition for company credentials
select @credential_issuer_id := id
from credential_issuer_definition
where wallet_id = @walletId
  and external_key = 'aktes';

delete from credential_issue_template
where wallet_id = @walletId
  and credential_issuer_id = @credential_issuer_id;


delete from credential_issuer_definition
where wallet_id = @walletId
  and external_key = 'aktes';

insert ignore into credential_issuer_definition (wallet_id, external_key, name, description, issuer_url, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'aktes', 'Aktes', '', 'https://aucklandnotaris.acc.credenco.com', now(), 'System', now(), 'System');

select @credential_issuer_id := id
from credential_issuer_definition
where wallet_id = @walletId
  and external_key = 'aktes';

INSERT IGNORE INTO credential_issue_template (wallet_id, credential_configuration_id, display_name, template, issuer_did_id)
VALUES (@walletId, 'oprichtingsakte_jwt_vc_json', '', '{}', @issuer_did_id);

UPDATE credential_issue_template
SET display_name         = 'Oprichtingsakte VC template',
    template             = '{
   "@context": [
     "https://www.w3.org/2018/credentials/v1",
     "https://www.w3.org/2018/credentials/examples/v1"
   ],
   "id": "http://example.gov/credentials/3732",
   "type": [
     "VerifiableCredential",
     "VerifiableAttestation",
     "Oprichtingsakte"
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
    issuer_did_id = @issuer_did_id,
    credential_issuer_id = @credential_issuer_id
WHERE wallet_id = @walletId
  and credential_configuration_id = 'oprichtingsakte_jwt_vc_json';


insert ignore into credential_issuer_display (credential_issuer_id, display_name, logo_url, logo_alt_text, locale)
values
    (@credential_issuer_id, 'Auckland Notarissen', 'https://aucklandnotaris.acc.credenco.com/card_logo.png', 'Auckland logo', null),
    (@credential_issuer_id, 'Auckland Notarissen', null, null, 'nl-NL'),
    (@credential_issuer_id, 'Van Auckland Notary', null, null, 'en-US');

insert ignore into credential_issuer_credential_definition (credential_issuer_id, credential_configuration_id, format)
values
    (@credential_issuer_id, 'oprichtingsakte_jwt_vc_json', 'jwt_vc_json');

select @credential_definition_id := id
from credential_issuer_credential_definition
where credential_issuer_id = @credential_issuer_id
  and credential_configuration_id = 'oprichtingsakte_jwt_vc_json';

insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale) VALUES (@credential_definition_id, 'Akte van oprichting', '#21436f', '#ffffff', 'https://aucklandnotaris.acc.credenco.com/card_background.png', 'Akte van Oprichting gestylede achtergrond', null);
insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale) VALUES (@credential_definition_id, 'Deed of Incorporation ', '#21436f', '#ffffff', 'https://aucklandnotaris.acc.credenco.com/card_background.png', 'Deed of Incorporation  styled background', 'en-US');
insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale) VALUES (@credential_definition_id, 'Akte van oprichting', '#21436f', '#ffffff', 'https://aucklandnotaris.acc.credenco.com/card_background.png', 'Akte van Oprichting gestylede achtergrond', 'nl-NL');

insert ignore into credential_issuer_credential_type (credential_definition_id, credential_type, type_order)
values
    (@credential_definition_id, 'VerifiableCredential', 1),
    (@credential_definition_id, 'VerifiableAttestation', 2),
    (@credential_definition_id, 'OprichtingsAkte', 3);

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'statutaireNaamVennootschap', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'statutaireNaamVennootschap';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Statutaire naam vennootschap', null), (@credential_attribute_id, 'Statutaire naam vennootschap', 'nl-NL'), (@credential_attribute_id, 'Statutory name Company (NL)', 'en-US');


INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'soortVennootschap', 3, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'soortVennootschap';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Soort vennootschap', null), (@credential_attribute_id, 'Soort vennootschap', 'nl-NL'), (@credential_attribute_id, 'Legal Entity Type (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'adresVennootschap', 2, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'adresVennootschap';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Adres vennootschap', null), (@credential_attribute_id, 'Adres vennootschap', 'nl-NL'), (@credential_attribute_id, 'Company Address (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'datumVanOprichtingVennootschap', 4, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'datumVanOprichtingVennootschap';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Datum van oprichting vennootschap', null), (@credential_attribute_id, 'Datum van oprichting vennootschap', 'nl-NL'), (@credential_attribute_id, 'Date of Establishment Legal Entity (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'maatschappelijkKapitaal', 5, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'maatschappelijkKapitaal';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Maatschappelijk kapitaal', null), (@credential_attribute_id, 'Maatschappelijk kapitaal', 'nl-NL'), (@credential_attribute_id, 'Share Capital (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'geplaatstKapitaal', 6, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'geplaatstKapitaal';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geplaatst kapitaal', null), (@credential_attribute_id, 'Geplaatst kapitaal', 'nl-NL'), (@credential_attribute_id, 'Issued Capital (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'bevoegdheden', 7, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'bevoegdheden';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Bevoegdheden', null), (@credential_attribute_id, 'Bevoegdheden', 'nl-NL'), (@credential_attribute_id, 'Authorities', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'bestuurder', 8, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'bestuurder';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Bestuurder', null), (@credential_attribute_id, 'Bestuurder', 'nl-NL'), (@credential_attribute_id, 'Director (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'geslachtsnaam', 9, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'geslachtsnaam';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geslachtsnaam', null), (@credential_attribute_id, 'Geslachtsnaam', 'nl-NL'), (@credential_attribute_id, 'Family name (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'naam', 10, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'naam';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Naam', null), (@credential_attribute_id, 'Naam', 'nl-NL'), (@credential_attribute_id, 'Name (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'geboortedatum', 11, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'geboortedatum';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geboortedatum', null), (@credential_attribute_id, 'Geboortedatum', 'nl-NL'), (@credential_attribute_id, 'Date of Birth', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'geboorteplaats', 12, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'geboorteplaats';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geboorteplaats', null), (@credential_attribute_id, 'Geboorteplaats', 'nl-NL'), (@credential_attribute_id, 'Place of Birth', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'titel', 13, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'titel';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Titel', null), (@credential_attribute_id, 'Titel', 'nl-NL'), (@credential_attribute_id, 'Title (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'datumInFunctie', 14, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'datumInFunctie';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Datum in functie', null), (@credential_attribute_id, 'Datum in functie', 'nl-NL'), (@credential_attribute_id, 'Date in office  (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'totaalAantalBestuurdersVennootschap', 15, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'totaalAantalBestuurdersVennootschap';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Totaal aantal bestuurders vennootschap', null), (@credential_attribute_id, 'Totaal aantal bestuurders vennootschap', 'nl-NL'), (@credential_attribute_id, 'Total number of directors (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'typeBevoegdheid', 16, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'typeBevoegdheid';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Type bevoegdheid', null), (@credential_attribute_id, 'Type bevoegdheid', 'nl-NL'), (@credential_attribute_id, 'Type of authority (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'beperkingBevoegdheid', 17, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'beperkingBevoegdheid';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Beperking bevoegdheid', null), (@credential_attribute_id, 'Beperking bevoegdheid', 'nl-NL'), (@credential_attribute_id, 'Restriction of authority (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'omschrijvingBevoegdheid', 18, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'omschrijvingBevoegdheid';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Omschrijving bevoegdheid', null), (@credential_attribute_id, 'Omschrijving bevoegdheid', 'nl-NL'), (@credential_attribute_id, 'Description Authority (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'aandeelhoudersVennootschap', 19, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'aandeelhoudersVennootschap';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Aandeelhouders vennootschap', null), (@credential_attribute_id, 'Aandeelhouders vennootschap', 'nl-NL'), (@credential_attribute_id, 'Shareholders', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'aandeelhouder', 20, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'aandeelhouder';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'aandeelhouder', null), (@credential_attribute_id, 'aandeelhouder', 'nl-NL'), (@credential_attribute_id, 'Shareholder', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'aantalAandelenInBezit', 21, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'aantalAandelenInBezit';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Aantal aandelen in bezit', null), (@credential_attribute_id, 'Aantal aandelen in bezit', 'nl-NL'), (@credential_attribute_id, 'Number of shares in possession for this type (NL)Number of shares in possession for this type (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'totaalAantalAandelenUitgegevenDoorVennootschap', 22, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'totaalAantalAandelenUitgegevenDoorVennootschap';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Totaal aantal aandelen uitgegeven door vennootschap', null), (@credential_attribute_id, 'Totaal aantal aandelen uitgegeven door vennootschap', 'nl-NL'), (@credential_attribute_id, 'Total number of issued shares for this type (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'typeAandeel', 23, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'typeAandeel';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Type aandeel', null), (@credential_attribute_id, 'Type aandeel', 'nl-NL'), (@credential_attribute_id, 'Share type (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'datumVanUitgifte', 24, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'datumVanUitgifte';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Datum van uitgifte', null), (@credential_attribute_id, 'Datum van uitgifte', 'nl-NL'), (@credential_attribute_id, 'Date of issue', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'gebruiksvoorwaarden', 25, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'gebruiksvoorwaarden';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Gebruiksvoorwaarden', null), (@credential_attribute_id, 'Gebruiksvoorwaarden', 'nl-NL'), (@credential_attribute_id, 'Terms of Use', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'naamNotaris', 26, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'naamNotaris';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Naam notaris', null), (@credential_attribute_id, 'Naam notaris', 'nl-NL'), (@credential_attribute_id, 'Name Notary (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'registratieNummerNotaris', 27, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'registratieNummerNotaris';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Registratienummer notaris', null), (@credential_attribute_id, 'Registratienummer notaris', 'nl-NL'), (@credential_attribute_id, 'Registration Number Notary (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'aandeelhoudersVennootschapTotaalAantalAandelenUitgegevenDoorVennootschap', 28, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'aandeelhoudersVennootschapTotaalAantalAandelenUitgegevenDoorVennootschap';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Totaal aantal aandelen uitgegeven door vennootschap', null), (@credential_attribute_id, 'Totaal aantal aandelen uitgegeven door vennootschap', 'nl-NL'), (@credential_attribute_id, 'Number of shares in possession for this type (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'aandeelhoudersVennootschapTypeAandeel', 29, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'aandeelhoudersVennootschapTypeAandeel';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Type aandeel', null), (@credential_attribute_id, 'Type aandeel', 'nl-NL'), (@credential_attribute_id, 'Share type (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'datumVanUitgifte', 30, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'datumVanUitgifte';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Datum van uitgifte', null), (@credential_attribute_id, 'Datum van uitgifte', 'nl-NL'), (@credential_attribute_id, 'Date of issue', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'gebruiksvoorwaarden', 31, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'gebruiksvoorwaarden';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Gebruiksvoorwaarden', null), (@credential_attribute_id, 'Gebruiksvoorwaarden', 'nl-NL'), (@credential_attribute_id, 'Terms of Use', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'naamNotaris', 32, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'naamNotaris';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Naam notaris', null), (@credential_attribute_id, 'Naam notaris', 'nl-NL'), (@credential_attribute_id, 'Name Notary (NL)', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'registratieNummerNotaris', 33, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'registratieNummerNotaris';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Registratienummer notaris', null), (@credential_attribute_id, 'Registratienummer notaris', 'nl-NL'), (@credential_attribute_id, 'Registration Number Notary (NL)', 'en-US');


DELETE FROM presentation_definition
where wallet_id = @walletId
  and external_key = 'pid';

INSERT INTO presentation_definition (wallet_id, external_key, name, description, purpose, success_redirect_url, error_redirect_url, client_url, notes, created_at, created_by, last_modified_at, last_modified_by)
VALUES (@walletId, 'pid', 'Inloggen met PID', 'Configuratie voor inloggen met personal ID', 'We willen graag uw PersonalID ontvangen om in te loggen', null, null, 'https://aucklandnotaris.acc.credenco.com', null, '2024-06-30 11:48:47', 'System', '2024-06-30 11:48:47', 'System');

select @presentation_definition_id := id
from presentation_definition
where wallet_id = @walletId
  and external_key = 'pid';

INSERT INTO presentation_definition_credential_type (presentation_definition_id, credential_type_configuration_url, credential_configuration_id, vc_type)
VALUES (@presentation_definition_id, 'https://pid.acc.credenco.com/.well-known/openid-credential-issuer', 'pid_jwt_vc_json', 'Pid');

INSERT INTO presentation_definition_policy (presentation_definition_id, presentation_definition_policy_type, name, args) VALUES (@presentation_definition_id, 'VP', 'signature', null);
INSERT INTO presentation_definition_policy (presentation_definition_id, presentation_definition_policy_type, name, args) VALUES (@presentation_definition_id, 'VP', 'expired', null);
INSERT INTO presentation_definition_policy (presentation_definition_id, presentation_definition_policy_type, name, args) VALUES (@presentation_definition_id, 'VC', 'signature', null);
INSERT INTO presentation_definition_policy (presentation_definition_id, presentation_definition_policy_type, name, args) VALUES (@presentation_definition_id, 'VC', 'expired', null);
