package com.project.hippohippogo.ids;

import java.io.Serializable;
import java.util.Objects;

public class TrendsId implements Serializable{
    private String person;
    private String region;

    public TrendsId() {
    }

    public TrendsId(String person, String region) {
        this.person = person;
        this.region = region;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, query);
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
        final QueryId other = (QueryId) obj;
        if (!Objects.equals(this.query, other.getQuery())) {
            return false;
        }
        return Objects.equals(this.userIp, other.getUserIp());
    }
}



public class QueryId implements Serializable {
    private String userIp;
    private String query;

    public QueryId() {}

    public QueryId(String userIp, String query) {
        this.userIp = userIp;
        this.query = query;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String ip) {
        this.userIp = ip;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIp, query);
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
        final QueryId other = (QueryId) obj;
        if (!Objects.equals(this.query, other.getQuery())) {
            return false;
        }
        return Objects.equals(this.userIp, other.getUserIp());
    }
}
