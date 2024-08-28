create table presentation_definition
(
    id                   int auto_increment primary key,
    wallet_id            int           not null,
    external_key         varchar(255)  not null,
    name                 varchar(512)  not null,
    description          varchar(2048) null,
    purpose              varchar(1024) not null,
    success_redirect_url varchar(1024) null,
    error_redirect_url   varchar(1024) null,
    client_url           varchar(1024) not null,
    notes                varchar(2048) null,
    created_at           datetime      not null,
    created_by           varchar(255)  not null,
    last_modified_at     datetime      not null,
    last_modified_by     varchar(255)  not null
);

alter table presentation_definition
    add constraint presentation_definition_wallet_fk
        foreign key (wallet_id) references wallet (id);

create unique index presentation_definition_external_key_uindex
    on presentation_definition (wallet_id, external_key);


create table presentation_definition_credential_type
(
    id                                int auto_increment primary key,
    presentation_definition_id        int          not null,
    credential_type_configuration_url varchar(512) not null,
    credential_configuration_id       varchar(255) not null,
    vc_type                           varchar(255) not null
);

alter table presentation_definition_credential_type
    add constraint pres_def_credential_type_pres_def_fk
        foreign key (presentation_definition_id) references presentation_definition (id) on delete cascade;


create table presentation_definition_policy
(
    id                                  int auto_increment primary key,
    presentation_definition_id          int           not null,
    presentation_definition_policy_type char(2)       not null,
    name                                varchar(255)  not null,
    args                                varchar(2048) null
);

alter table presentation_definition_policy
    add constraint presentation_definition_policy_presentation_definition_fk
        foreign key (presentation_definition_id) references presentation_definition (id) on delete cascade;

alter table issuer_credential_type
    add vc_type varchar(255) null;

update issuer_credential_type
set vc_type = 'VerifiableCredential';

alter table issuer_credential_type
    modify vc_type varchar(255) not null;

