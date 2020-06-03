package com.project.hippohippogo.ids;

import java.io.Serializable;
import java.util.Objects;

public class UserFrequentDomainId implements Serializable {
    private String userIp;
    private String domain;

    public UserFrequentDomainId() {}

    public UserFrequentDomainId(String userIp, String domain) {
        this.userIp = userIp;
        this.domain = domain;
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

    public void setDomain(String query) {
        this.domain = domain;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIp, domain);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserFrequentDomainId other = (UserFrequentDomainId) obj;
        if (!Objects.equals(this.domain, other.getDomain())) {
            return false;
        }
        return Objects.equals(this.userIp, other.getUserIp());
    }
}
