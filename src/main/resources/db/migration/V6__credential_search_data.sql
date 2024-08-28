alter table credential
    add column search_data text null;

update credential
set search_data = '';

alter table credential
    modify column search_data text not null;

