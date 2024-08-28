alter table issuer
    add column configuration_url varchar(512) default '' not null;
alter table issuer
    alter column configuration_url drop default;
alter table issuer
    add column configuration_content text null;
update issuer
set configuration_content = '';
alter table issuer
    modify column configuration_content text not null;

alter table issuer
    add column issue_url varchar(512) default '' not null;
alter table issuer
    alter column configuration_url drop default;



alter table issuer
    drop column display_properties;

alter table issuer
    add column skip_issue_to_wallet_confirmation bit not null default 0;
