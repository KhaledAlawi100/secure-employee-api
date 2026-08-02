package com.khaled.secure_employee_api.department.entity;


import com.khaled.secure_employee_api.employee.model.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "departments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100 )
    private String name;

    @Column(length = 500)
    private String description;

    @Builder.Default
    @OneToMany(
            mappedBy = "department",
            fetch = FetchType.LAZY
    )
    private Set<Employee> employees = new HashSet<>();
}
