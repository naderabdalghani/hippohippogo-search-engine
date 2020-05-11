package com.project.hippohippogo.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dummy_items")
public class DummyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long _id;

    private String name;

    public DummyItem() {
    }

    public DummyItem(Long _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this._id);
        hash = 79 * hash + Objects.hashCode(this.name);
        return hash;
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
        final DummyItem other = (DummyItem) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this._id, other._id);
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + _id +
                ", name='" + name + '\'' +
                '}';
    }
}
