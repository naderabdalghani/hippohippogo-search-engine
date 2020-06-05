package com.project.hippohippogo.entities;


import com.project.hippohippogo.ids.UserFrequentDomainId;

import javax.persistence.*;

@Entity
@IdClass(UserFrequentDomainId.class)
@Table(name = "users_frequent_domains")
public class UserFrequentDomain {
    @Id
    @Column(name= "\"user_ip\"")
    private String userIp;
    @Id
    @Column(name="\"domain\"")
    private String domain;
    @Column(name="\"hits\"")
    private int hits;

    public UserFrequentDomain() {

    }

    public UserFrequentDomain(String userIp, String domain) {
        this.userIp = userIp;
        this.domain = domain;
        hits = 1;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String ip) {
        this.userIp = ip;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getHits() {
        return hits;
    }

    public void incrementHits() {
        hits++;
    }
}
