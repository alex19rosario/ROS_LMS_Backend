package com.ros.lms.domain.entities;

import com.ros.lms.domain.converters.RoleTypeConverter;
import com.ros.lms.domain.enums.RoleType;
import jakarta.persistence.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Entity
@Table(name = "AUTHORITY_TYPE")
public class AuthorityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_TYPE_ID")
    private Integer id;

    @Column(name = "LABEL", length = 128, nullable = false, unique = true)
    @Convert(converter = RoleTypeConverter.class)
    private RoleType label;

    public AuthorityType() {}

    public AuthorityType(RoleType label) {
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleType getLabel() {
        return label;
    }

    public void setLabel(RoleType label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "AuthorityType{" +
                "id=" + id +
                ", label='" + label.val() + '\'' +
                '}';
    }
}
