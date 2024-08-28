create table linked_presentation
(
    id                       int auto_increment primary key,
    wallet_id                int           not null,
    external_key             varchar(255)  not null,
    document                 text          not null,
    notes                    varchar(2048) null,
    created_at               datetime      not null,
    created_by               varchar(255)  not null,
    last_modified_at         datetime      not null,
    last_modified_by         varchar(255)  not null
);

alter table linked_presentation
    add constraint linked_presentation_wallet_fk
        foreign key (wallet_id) references wallet (id);
