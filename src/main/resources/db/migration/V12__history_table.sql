create table history
(
    id                       int auto_increment primary key,
    wallet_id                int           not null,
    credential_uuid          varchar(255)  null,
    user_id                  int           null,
    evidence                 text          null,
    event_date               datetime      not null,
    event                    varchar(255)  not null,
    action                   varchar(255)  not null,
    party                    varchar(255)  null
);

alter table history
    add constraint history_wallet_fk
        foreign key (wallet_id) references wallet (id);
alter table history
    add constraint history_wallet_user_fk
        foreign key (user_id) references wallet_user (id);

