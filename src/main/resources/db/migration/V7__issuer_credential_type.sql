create table issuer_credential_type
(
    id                          int auto_increment primary key,
    issuer_id                   int            not null,
    credential_configuration_id varchar(255)   not null,
    search_data                 text not null
);


alter table issuer_credential_type
    add constraint issuer_credential_type_issuer_fk
        foreign key (issuer_id) references issuer (id);

