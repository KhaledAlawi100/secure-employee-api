package com.khaled.secure_employee_api.employee.repository;

import com.khaled.secure_employee_api.department.entity.Department;
import com.khaled.secure_employee_api.employee.model.Employee;
import com.khaled.secure_employee_api.position.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserUsername(String username);

    Optional<Employee> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    long countByDepartment(Department department);


    long countByPosition(Position position);
}