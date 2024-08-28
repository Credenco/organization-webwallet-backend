create table wallet_user
(
    id               int auto_increment primary key,
    wallet_id        int          not null,
    user_name        varchar(255) not null,
    preferred_locale varchar(10)  not null,
    created_at       datetime     not null,
    last_modified_at datetime     not null
);

alter table wallet_user
    add constraint user_wallet_fk
        foreign key (wallet_id) references wallet (id);

