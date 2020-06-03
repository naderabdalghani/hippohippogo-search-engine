package com.project.hippohippogo.ids;

import java.io.Serializable;
import java.util.Objects;

public class QueryId implements Serializable {
    private String userIp;
    private String query;
    private String region;

    public QueryId() {}

    public QueryId(String userIp, String query, String region) {
        this.userIp = userIp;
        this.query = query;
        this.region = region;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIp, query, region);
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
        if (!Objects.equals(this.region, other.getRegion())) {
            return false;
        }
        return Objects.equals(this.userIp, other.getUserIp());
    }
}
