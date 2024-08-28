create table credential_issuer_definition
(
    id                   int auto_increment primary key,
    wallet_id            int           not null,
    external_key         varchar(255)  not null,
    name                 varchar(512)  not null,
    description          varchar(2048) null,
    issuer_url           varchar(1024) not null,
    created_at           datetime      not null,
    created_by           varchar(255)  not null,
    last_modified_at     datetime      not null,
    last_modified_by     varchar(255)  not null
);

alter table credential_issuer_definition
    add constraint credential_issuer_definition_wallet_fk
        foreign key (wallet_id) references wallet (id);

create table credential_issuer_display
(
    id                      int auto_increment primary key,
    credential_issuer_id    int          not null,
    display_name            varchar(512)  not null,
    logo_url                varchar(512)  null,
    logo_alt_text           varchar(512)  null,
    locale                  varchar(10)  null
);

alter table credential_issuer_display
    add constraint credential_issuer_display_cred_iss_fk
        foreign key (credential_issuer_id) references credential_issuer_definition (id) on delete cascade;

create table credential_issuer_credential_definition
(
    id                                int auto_increment primary key,
    credential_issuer_id              int          not null,
    credential_configuration_id       varchar(255) not null,
    format                            varchar(20) not null
);

alter table credential_issuer_credential_definition
    add constraint cred_iss_credential_def_cred_iss_fk
        foreign key (credential_issuer_id) references credential_issuer_definition (id) on delete cascade;


create table credential_issuer_credential_type
(
    id                                  int auto_increment primary key,
    credential_definition_id            int           not null,
    credential_type                     varchar(255)  not null,
    type_order                          int           not null
);

alter table credential_issuer_credential_type
    add constraint cred_iss_credential_type_cred_def_fk
        foreign key (credential_definition_id) references credential_issuer_credential_definition (id) on delete cascade;

create table credential_issuer_credential_display
(
    id                                  int auto_increment primary key,
    credential_definition_id            int           not null,
    display_name                        varchar(255)  not null,
    background_color                    varchar(10)  null,
    text_color                          varchar(10)  null,
    background_image_url                varchar(255) null,
    background_alt_text                 varchar(255) null,
    locale                              varchar(10)  null
);

alter table credential_issuer_credential_display
    add constraint cred_iss_credential_display_cred_def_fk
        foreign key (credential_definition_id) references credential_issuer_credential_definition (id) on delete cascade;


create table credential_issuer_credential_attribute
(
    id                                  int auto_increment primary key,
    credential_definition_id            int           not null,
    attribute_name                      varchar(255)  not null,
    attribute_order                     int           not null,
    mandatory                           bool          not null default false
);

alter table credential_issuer_credential_attribute
    add constraint cred_iss_credential_attribute_cred_def_fk
        foreign key (credential_definition_id) references credential_issuer_credential_definition (id) on delete cascade;

create table credential_issuer_credential_attribute_display
(
    id                                  int auto_increment primary key,
    credential_attribute_id             int           not null,
    display_name                        varchar(255)  not null,
    locale                              varchar(10)  null
);

alter table credential_issuer_credential_attribute_display
    add constraint cred_iss_cred_att_display_cred_att_fk
        foreign key (credential_attribute_id) references credential_issuer_credential_attribute (id) on delete cascade;

alter table credential_issue_template
    add credential_issuer_id int null;

alter table credential_issue_template
    add constraint cred_iss_temp_cred_issuer_fk
        foreign key (credential_issuer_id) references credential_issuer_definition (id);

