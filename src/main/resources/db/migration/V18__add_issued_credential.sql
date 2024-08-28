create table issued_credential
(
    id                                         int auto_increment primary key,
    wallet_id                                  int         not null,
    credential_issuer_credential_definition_id int         not null,
    issuer_id                                  int         not null,
    credential_uuid                            varchar(50) not null,
    credential_format                          varchar(20) not null,
    document                                   text        not null,
    search_data                                text        not null,
    issuance_date                              datetime    not null,
    valid_from                                 datetime    null,
    valid_until                                datetime    null
);

alter table issued_credential
    add constraint issued_credential_wallet_fk
        foreign key (wallet_id) references wallet (id);

alter table issued_credential
    add constraint issued_credential_c_i_cred_def_fk
        foreign key (credential_issuer_credential_definition_id) references credential_issuer_credential_definition (id);

alter table issued_credential
    add constraint issued_credential_issuer_fk
        foreign key (issuer_id) references issuer (id);

