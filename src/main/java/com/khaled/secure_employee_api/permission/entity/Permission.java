package com.khaled.secure_employee_api.permission.entity;

import com.khaled.secure_employee_api.role.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Enumerated(EnumType.STRING)
    private PermissionName  name;

    @ManyToMany(mappedBy = "permissions")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
