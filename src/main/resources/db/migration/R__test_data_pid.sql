INSERT IGNORE INTO wallet (external_key, display_name, api_key)
VALUES ('8c12af10-0847-43d8-9421-03c62fbbadb0', 'PID demo issuer', 'rn9WRQTFggYX19RVlHjE6qedYKi');

select @walletId := id
from wallet
where external_key = '8c12af10-0847-43d8-9421-03c62fbbadb0';

UPDATE wallet
SET display_name = 'PID demo'
WHERE id = @walletId;

INSERT IGNORE INTO wallet_key (wallet_id, kid, display_name, document, created_at, created_by, last_modified_at, last_modified_by) VALUES (@walletId, 'OTcwD2EYUOkRJRq8DDBJ_uJYdBFfB0ckWrdpRq6ZQBU', 'PID jwk Key', '{"type":"jwk","jwk":"{\\"kty\\":\\"EC\\",\\"d\\":\\"p_a2I2EUQg1gwiPaXYOOyebFm5_mjz2BGXmSK4hqv-o\\",\\"crv\\":\\"P-256\\",\\"kid\\":\\"OTcwD2EYUOkRJRq8DDBJ_uJYdBFfB0ckWrdpRq6ZQBU\\",\\"x\\":\\"XoZOtiXGl6bXpOOJCh1ob1i602PcXWWE9NvS7lCIqbE\\",\\"y\\":\\"bwOOz8fJT7Ixlivo7OS7K33E9lq0faAABZMoIPGGlRY\\"}"}', '2024-06-29 09:40:01', 'System', '2024-06-29 09:40:01', 'System');

select @walletKeyId := id
from wallet_key
where wallet_id = @walletId
  and kid = 'OTcwD2EYUOkRJRq8DDBJ_uJYdBFfB0ckWrdpRq6ZQBU';

select @did := 'did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ';

INSERT INTO issuer (wallet_id, did, created_at, created_by, last_modified_at, last_modified_by, configuration_url, issue_url, configuration_content)
(select wallet.id,
        @did,
        now(), 'System', now(), 'System',
        'https://wallet.acc.credenco.com/public/8c12af10-0847-43d8-9421-03c62fbbadb0/aktes/.well-known/openid-credential-issuer',
        'https://demopidprovider.acc.credenco.com/',
        ''
from wallet
left join issuer on issuer.wallet_id = wallet.id and issuer.did = @did
where issuer.id is null);



INSERT IGNORE INTO did (wallet_id, display_name, did, document, wallet_key_id, created_at, created_by, last_modified_at, last_modified_by, environment)
VALUES (11, 'JWK', @did, '{"@context":["https://www.w3.org/ns/did/v1","https://w3id.org/security/suites/jws-2020/v1"],"id":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ","verificationMethod":[{"id":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ#0","type":"JsonWebKey2020","controller":"did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ","publicKeyJwk":{"kty":"EC","crv":"P-256","kid":"OTcwD2EYUOkRJRq8DDBJ_uJYdBFfB0ckWrdpRq6ZQBU","x":"XoZOtiXGl6bXpOOJCh1ob1i602PcXWWE9NvS7lCIqbE","y":"bwOOz8fJT7Ixlivo7OS7K33E9lq0faAABZMoIPGGlRY"}}],"assertionMethod":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ#0"],"authentication":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ#0"],"capabilityInvocation":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ#0"],"capabilityDelegation":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ#0"],"keyAgreement":["did:jwk:eyJrdHkiOiJFQyIsImNydiI6IlAtMjU2Iiwia2lkIjoiT1Rjd0QyRVlVT2tSSlJxOEREQkpfdUpZZEJGZkIwY2tXcmRwUnE2WlFCVSIsIngiOiJYb1pPdGlYR2w2YlhwT09KQ2gxb2IxaTYwMlBjWFdXRTlOdlM3bENJcWJFIiwieSI6ImJ3T096OGZKVDdJeGxpdm83T1M3SzMzRTlscTBmYUFBQlpNb0lQR0dsUlkifQ#0"]}',
    @walletKeyId, '2024-06-28 07:34:32', 'System', '2024-06-28 07:34:32', 'System', null);

select @issuer_id := id
from issuer
where wallet_id = @walletId
  and did = @did;

select @issuer_did_id := id
from did
where wallet_id = @walletId
  and did = @did;

-- Issuer definition for company credentials
delete from credential_issue_template
where wallet_id = @walletId
  and credential_issuer_id = @credential_issuer_id;

delete from credential_issue_template
WHERE wallet_id = @walletId
  and credential_configuration_id = 'pid_jwt_vc_json';

delete from credential_issuer_definition
where wallet_id = @walletId
  and external_key = 'pid';

insert ignore into credential_issuer_definition (wallet_id, external_key, name, description, issuer_url, created_at, created_by, last_modified_at, last_modified_by)
values (@walletId, 'pid', 'Personal IDs', '', 'https://wallet.acc.credenco.com/public/8c12af10-0847-43d8-9421-03c62fbbadb0/pid', now(), 'System', now(), 'System');

select @credential_issuer_id := id
from credential_issuer_definition
where wallet_id = @walletId
  and external_key = 'pid';

INSERT IGNORE INTO credential_issue_template (wallet_id, credential_configuration_id, display_name, template, issuer_did_id)
VALUES (@walletId, 'pid_jwt_vc_json', '', '{}', @issuer_did_id);

UPDATE credential_issue_template
SET display_name         = 'Pid VC template',
    template             = '{
   "@context": [
     "https://www.w3.org/2018/credentials/v1",
     "https://www.w3.org/2018/credentials/examples/v1"
   ],
   "id": "http://example.gov/credentials/3732",
   "type": [
     "VerifiableCredential",
     "VerifiableAttestation",
     "Pid"
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
  and credential_configuration_id = 'pid_jwt_vc_json';


insert ignore into credential_issuer_display (credential_issuer_id, display_name, logo_url, logo_alt_text, locale)
values
    (@credential_issuer_id, 'Koninkrijk der Nederlanden', 'https://demopidprovider.acc.credenco.com/card_logo.png', 'Babylon logo', null),
    (@credential_issuer_id, 'Koninkrijk der Nederlanden', null, null, 'nl-NL'),
    (@credential_issuer_id, 'Kingdom of the Netherlands', null, null, 'en-US');

insert ignore into credential_issuer_credential_definition (credential_issuer_id, credential_configuration_id, format)
values
    (@credential_issuer_id, 'pid_jwt_vc_json', 'jwt_vc_json');

select @credential_definition_id := id
from credential_issuer_credential_definition
where credential_issuer_id = @credential_issuer_id
  and credential_configuration_id = 'pid_jwt_vc_json';

insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale) VALUES (@credential_definition_id, 'Persoonlijk ID', '#0c2a8d', '#ffffff', 'https://demopidprovider.acc.credenco.com/card_background.png', 'Persoonlijk ID gestylede achtergrond', null);
insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale) VALUES (@credential_definition_id, 'Persoonlijk ID', '#0c2a8d', '#ffffff', 'https://demopidprovider.acc.credenco.com/card_background.png', 'Persoonlijk ID gestylede achtergrond', 'nl-NL');
insert ignore into credential_issuer_credential_display (credential_definition_id, display_name, background_color, text_color, background_image_url, background_alt_text, locale) VALUES (@credential_definition_id, 'Personal ID', '#0c2a8d', '#ffffff', 'https://demopidprovider.acc.credenco.com/card_background.png', 'Personal ID styled background', 'en-GB');

insert ignore into credential_issuer_credential_type (credential_definition_id, credential_type, type_order)
values
    (@credential_definition_id, 'VerifiableCredential', 1),
    (@credential_definition_id, 'VerifiableAttestation', 2),
    (@credential_definition_id, 'Pid', 3);

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'familyName', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'familyName';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Familienaam', null), (@credential_attribute_id, 'Familienaam', 'nl-NL'), (@credential_attribute_id, 'Family name', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'givenName', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'givenName';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Voornamen', null), (@credential_attribute_id, 'Voornamen', 'nl-NL'), (@credential_attribute_id, 'Given name', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'birthDate', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'birthDate';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geboortedatum', null), (@credential_attribute_id, 'Geboortedatum', 'nl-NL'), (@credential_attribute_id, 'Date of borth', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'birthPlace', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'birthPlace';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geboorte plaats', null), (@credential_attribute_id, 'Geboorte plaats', 'nl-NL'), (@credential_attribute_id, 'Place of birth', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'birthCountry', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'birthCountry';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geboorte land', null), (@credential_attribute_id, 'Geboorte land', 'nl-NL'), (@credential_attribute_id, 'Country of birth', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'birthState', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'birthState';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geboorte provincie', null), (@credential_attribute_id, 'Geboorte provincie', 'nl-NL'), (@credential_attribute_id, 'State of birth', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'birthCity', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'birthCity';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geboorte plaats', null), (@credential_attribute_id, 'Geboorte plaats', 'nl-NL'), (@credential_attribute_id, 'City of birth', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'residentAddress', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'residentAddress';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Woonadres', null), (@credential_attribute_id, 'Woonadres', 'nl-NL'), (@credential_attribute_id, 'Residential address', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'residentCountry', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'residentCountry';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Woonland', null), (@credential_attribute_id, 'Woonland', 'nl-NL'), (@credential_attribute_id, 'Residential Country', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'residentState', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'residentState';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Woonprovincie', null), (@credential_attribute_id, 'Woonprovincie', 'nl-NL'), (@credential_attribute_id, 'Residential state', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'residentCity', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'residentCity';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Woonplaats', null), (@credential_attribute_id, 'Woonplaats', 'nl-NL'), (@credential_attribute_id, 'Residential city', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'residentPostal', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'residentPostal';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Woonpostcode', null), (@credential_attribute_id, 'Woonpostcode', 'nl-NL'), (@credential_attribute_id, 'Residential postal code', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'residentStreet', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'residentStreet';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Woonstraat', null), (@credential_attribute_id, 'Woonstraat', 'nl-NL'), (@credential_attribute_id, 'Residential street', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'residentHouseNumber', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'residentHouseNumber';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Woonhuisnummer', null), (@credential_attribute_id, 'Woonhuisnummer', 'nl-NL'), (@credential_attribute_id, 'Residential house number', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'gender', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'gender';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geslacht', null), (@credential_attribute_id, 'Geslacht', 'nl-NL'), (@credential_attribute_id, 'Gender', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'nationality', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'nationality';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Nationaliteit', null), (@credential_attribute_id, 'Nationaliteit', 'nl-NL'), (@credential_attribute_id, 'Nationality', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'documentNumber', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'documentNumber';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Documentnummer', null), (@credential_attribute_id, 'Documentnummer', 'nl-NL'), (@credential_attribute_id, 'Document number', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'issuingCountry', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'issuingCountry';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Land van uitgifte', null), (@credential_attribute_id, 'Land van uitgifte', 'nl-NL'), (@credential_attribute_id, 'Issuing country', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'issuingJurisdiction', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'issuingJurisdiction';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Jurisdictie', null), (@credential_attribute_id, 'Jurisdictie', 'nl-NL'), (@credential_attribute_id, 'issuing Jurisdiction', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'ageOver18', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'ageOver18';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Ouder dan 18', null), (@credential_attribute_id, 'Ouder dan 18', 'nl-NL'), (@credential_attribute_id, 'Age over 18', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'ageOverNN', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'ageOverNN';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Jonger dan 18', null), (@credential_attribute_id, 'Jonger dan 18', 'nl-NL'), (@credential_attribute_id, 'Under the age of 18', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'ageInYears', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'ageInYears';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Leeftijd in jaren', null), (@credential_attribute_id, 'Leeftijd in jaren', 'nl-NL'), (@credential_attribute_id, 'Age in years', 'en-US');

INSERT IGNORE INTO credential_issuer_credential_attribute (credential_definition_id, attribute_name, attribute_order, mandatory) VALUES (@credential_definition_id, 'ageBirthYear', 1, 0);
select @credential_attribute_id := id from credential_issuer_credential_attribute where credential_definition_id = @credential_definition_id and attribute_name = 'ageBirthYear';
insert ignore into credential_issuer_credential_attribute_display (credential_attribute_id, display_name, locale) values (@credential_attribute_id, 'Geboortejaar', null), (@credential_attribute_id, 'Geboortejaar', 'nl-NL'), (@credential_attribute_id, 'Year of birth', 'en-US');

