create table wallet
(
    id           int auto_increment primary key,
    external_key char(36)      not null,
    display_name varchar(100)  not null,
    api_key      varchar(2000) not null
);

create unique index wallet_external_key_uindex
    on wallet (external_key);

create table credential_issue_template
(
    id                          int auto_increment primary key,
    credential_configuration_id varchar(255) not null,
    wallet_id                   int          not null,
    display_name                varchar(100) not null,
    template                    text         not null,
    issuer_key_reference        varchar(255) not null,
    issuer_did                  varchar(255) not null
);

create unique index credential_issue_template_external_key_uindex
    on credential_issue_template (credential_configuration_id);

alter table credential_issue_template
    add constraint credential_issue_template_wallet_fk
        foreign key (wallet_id) references wallet (id);
