alter table credential
add column status varchar(50) not null default 'VALID';
