alter table did
    add column environment varchar(32) null;

update did set environment = 'conformance' where did like 'did:ebsi:%'
