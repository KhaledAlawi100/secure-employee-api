package com.khaled.secure_employee_api.position.repository;

import com.khaled.secure_employee_api.position.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository
        extends JpaRepository<Position, Long> {

    Optional<Position> findByName(String name);

    boolean existsByName(String name);

}