package com.project.hippohippogo.repositories;

import com.project.hippohippogo.entities.UserFrequentDomain;
import com.project.hippohippogo.ids.UserFrequentDomainId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersFrequentDomainsRepository extends JpaRepository<UserFrequentDomain, UserFrequentDomainId> {

    // Getting top 20 pages the user went to
    @Query(value = "SELECT * FROM users_frequent_domains WHERE user_ip=?1 ORDER BY rank DESC LIMIT 20", nativeQuery = true)
    List<UserFrequentDomain> getUserBestDomains(int user_id);

}