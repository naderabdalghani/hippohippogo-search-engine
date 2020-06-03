package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.UserFrequentDomain;
import com.project.hippohippogo.ids.UserFrequentDomainId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersFrequentDomainsRepository extends JpaRepository<UserFrequentDomain, UserFrequentDomainId> {

    // Getting number of hits of a domain
    @Query(value = "SELECT hits FROM users_frequent_domains WHERE user_ip = ?1 and domain = ?2", nativeQuery = true)
    int getDomainHits(int user_id, String domain);

    // Getting domain hits
    @Query(value = "SELECT SUM(hits) FROM users_frequent_domains WHERE user_ip = ?1", nativeQuery = true)
    int getUserDomainSum(int user_id, String domain);
}