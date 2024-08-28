alter table credential_issuer_credential_definition
add column issue_template_id int null;

alter table credential_issuer_credential_definition
    add constraint credential_issuer_credential_definition_template_fk
        foreign key (issue_template_id) references credential_issue_template (id);

update credential_issuer_credential_definition
set issue_template_id = (
    select id from credential_issue_template
    where credential_issue_template.credential_configuration_id = credential_issuer_credential_definition.credential_configuration_id
    and credential_issue_template.credential_issuer_id = credential_issuer_credential_definition.credential_issuer_id
)
