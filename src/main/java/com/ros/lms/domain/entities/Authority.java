package com.ros.lms.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "AUTHORITIES", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USERNAME", "AUTHORITY"})
})
public class Authority {
    @EmbeddedId
    private AuthorityId id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USERNAME", insertable = false, updatable = false)
    private User user;

    public Authority() {}

    public Authority(AuthorityId id) {
        this.id = id;
    }

    public Authority(AuthorityId id, User user) {
        this.id = id;
        this.user = user;
    }

    public AuthorityId getId() {
        return id;
    }

    public void setId(AuthorityId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "username=" + id.getUsername() +
                ", role=" + id.getAuthority() +
                '}';
    }
}
