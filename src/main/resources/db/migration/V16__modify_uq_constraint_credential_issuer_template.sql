alter table credential_issue_template
    drop key credential_issue_template_external_key_uindex;

alter table credential_issue_template
    add constraint credential_issue_template_external_key_uindex
        unique (credential_configuration_id, wallet_id);

