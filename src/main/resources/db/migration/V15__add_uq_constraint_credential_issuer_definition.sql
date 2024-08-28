alter table credential_issuer_definition
    add constraint credential_issuer_definition_uq_external_key
        unique (wallet_id, external_key);

