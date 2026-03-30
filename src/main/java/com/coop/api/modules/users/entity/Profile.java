package com.coop.api.modules.users.entity;

import com.coop.api.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile")
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String label; //Should be one word
    private  String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "profile_access_rights", // Name of the join table
            joinColumns = @JoinColumn(name = "profile_id"), // Column in join table referring to Profile
            inverseJoinColumns = @JoinColumn(name = "access_right_id") // Column in join table referring to AccessRight
    )
    private Set<AccessRight> accessRights = new HashSet<>();

    @Override
    public String toString() {
        return String.format("id: %d, label: %s", this.id, this.label);
    }
}
