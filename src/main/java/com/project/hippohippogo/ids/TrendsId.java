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
        return Objects.hash(person, region);
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
        final TrendsId other = (TrendsId) obj;
        if (!Objects.equals(this.person, other.getPerson())) {
            return false;
        }
        return Objects.equals(this.region, other.getRegion());
    }
}

