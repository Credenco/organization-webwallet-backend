alter table credential
    add column credential_configuration_id varchar(255) default '' not null;

alter table credential
    alter column credential_configuration_id drop default;
