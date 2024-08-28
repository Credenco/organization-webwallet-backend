package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.IssuerCredentialType;
import com.credenco.webwallet.backend.domain.PresentationDefinitionCredentialType;
import com.credenco.webwallet.backend.domain.Wallet;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuerCredentialTypeRepository extends JpaRepository<IssuerCredentialType, Long>, JpaSpecificationExecutor<IssuerCredentialType> {

    @Query("""
            SELECT ic 
            FROM IssuerCredentialType ic 
            WHERE ic.issuer.wallet = :wallet
            AND CONCAT(ic.issuer.configurationUrl, "_", ic.credentialConfigurationId) IN :typeKeys
            """)
    List<IssuerCredentialType> findAllByTypeKeys(Wallet wallet, List<String> typeKeys);

    default Specification<IssuerCredentialType> hasWallet(Wallet wallet) {
        return (issuerCredentialType, cq, cb) -> cb.equal(issuerCredentialType.get("issuer").get("wallet"), wallet);
    }

    default Specification<IssuerCredentialType> containsSearchText(String searchText) {
        return (issuerCredentialType, cq, cb) -> cb.like(cb.upper(issuerCredentialType.get("searchData")), "%" + upper(searchText) + "%");
    }

    private String upper(String text) {
        return text != null ? text.toUpperCase(Locale.ROOT) : null;
    }

    default Specification<IssuerCredentialType> allOf(List<Specification<IssuerCredentialType>> specifications) {
        return (issuerCredentialType, cq, cb) -> {
            Predicate[] predicates = specifications.stream().map(specification -> specification.toPredicate(issuerCredentialType, cq, cb))
                    .toArray(Predicate[]::new);
            return cb.and(predicates);
        };
    }

}



