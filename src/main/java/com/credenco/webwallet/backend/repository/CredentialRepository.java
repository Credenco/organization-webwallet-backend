package com.credenco.webwallet.backend.repository;

import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.Wallet;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long>, JpaSpecificationExecutor<Credential> {

    List<Credential> findByWallet(Wallet wallet);

    Optional<Credential> findByWalletAndId(Wallet wallet, Long id);

    List<Credential> findByWalletAndIdIsIn(Wallet wallet, List<Long> ids);

    default Specification<Credential> hasWallet(Wallet wallet) {
        return (credential, cq, cb) -> cb.equal(credential.get("wallet"), wallet);
    }

    default Specification<Credential> containsSearchText(String searchText) {
        return (credential, cq, cb) -> cb.like(cb.upper(credential.get("searchData")), "%" + searchText.toUpperCase(Locale.ROOT) + "%");
    }

    default Specification<Credential> allOf(List<Specification<Credential>> specifications) {
        return (credential, cq, cb) -> {
            Predicate[] predicates = specifications.stream().map(specification -> specification.toPredicate(credential, cq, cb))
                    .toArray(Predicate[]::new);
            return cb.and(predicates);
        };
    }

}



