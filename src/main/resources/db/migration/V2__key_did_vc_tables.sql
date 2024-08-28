create table wallet_key
(
    id               int auto_increment primary key,
    wallet_id        int          not null,
    kid              varchar(255) not null,
    display_name     varchar(255) not null,
    document         text         not null,
    created_at       datetime     not null,
    created_by       varchar(255) not null,
    last_modified_at datetime     not null,
    last_modified_by varchar(255) not null
);

alter table wallet_key
    add constraint key_wallet_fk
        foreign key (wallet_id) references wallet (id);

create unique index wallet_key_key_alias_uindex
    on wallet_key (wallet_id, kid);


create table issuer
(
    id                 int auto_increment primary key,
    wallet_id          int           not null,
    display_properties text          null,
    did                varchar(1024) not null,
    created_at         datetime      not null,
    created_by         varchar(255)  not null,
    last_modified_at   datetime      not null,
    last_modified_by   varchar(255)  not null
);

alter table issuer
    add constraint issuer_wallet_fk
        foreign key (wallet_id) references wallet (id);


create table did
(
    id               int auto_increment primary key,
    wallet_id        int          not null,
    display_name     varchar(255) null,
    did varchar(512) not null,
    document         text         not null,
    wallet_key_id    int          not null,
    created_at       datetime     not null,
    created_by       varchar(255) not null,
    last_modified_at datetime     not null,
    last_modified_by varchar(255) not null
);

create unique index did_did_uindex
    on did (did);

alter table did
    add constraint did_wallet_fk
        foreign key (wallet_id) references wallet (id);
alter table did
    add constraint did_key_fk
        foreign key (wallet_key_id) references wallet_key (id);



create table credential
(
    id                       int auto_increment primary key,
    wallet_id                int           not null,
    document                 text          not null,
    credential_format        varchar(20)   not null,
    disclosures              text          null,
    metadata                 text          null,
    credential_configuration text          not null,
    notes                    varchar(2048) null,
    valid_from               datetime      null,
    valid_until              datetime      null,
    issuance_date            datetime      null,
    issuer_id                int           not null,
    created_at               datetime      not null,
    created_by               varchar(255)  not null,
    last_modified_at         datetime      not null,
    last_modified_by         varchar(255)  not null
);

alter table credential
    add constraint credential_wallet_fk
        foreign key (wallet_id) references wallet (id);

alter table credential
    add constraint credential_issuer_fk
        foreign key (issuer_id) references issuer (id);

delete
from credential_issue_template
where wallet_id < 3;


alter table credential_issue_template
    add issuer_did_id int null;

alter table credential_issue_template
    drop column issuer_key_reference;

alter table credential_issue_template
    drop column issuer_did;

alter table credential_issue_template
    add constraint credential_issue_template_did_id_fk
        foreign key (issuer_did_id) references did (id);

