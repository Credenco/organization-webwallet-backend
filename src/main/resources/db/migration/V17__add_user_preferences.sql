create table wallet_user_preference
(
    id               int auto_increment primary key,
    wallet_user_id   int          not null,
    preference_key   varchar(100) not null,
    preference_value varchar(254) not null
);

alter table wallet_user_preference
    add constraint wallet_user_preference_wallet_user_fk
        foreign key (wallet_user_id) references wallet_user (id);
