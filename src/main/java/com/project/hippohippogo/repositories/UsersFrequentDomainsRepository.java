package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.UserFrequentDomain;
import com.project.hippohippogo.ids.UserFrequentDomainId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersFrequentDomainsRepository extends JpaRepository<UserFrequentDomain, UserFrequentDomainId> {

}