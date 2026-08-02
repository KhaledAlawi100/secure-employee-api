package com.khaled.secure_employee_api.position.service;

import com.khaled.secure_employee_api.employee.repository.EmployeeRepository;
import com.khaled.secure_employee_api.position.dto.CreatePositionRequest;
import com.khaled.secure_employee_api.position.dto.PositionResponse;
import com.khaled.secure_employee_api.position.dto.UpdatePositionRequest;
import com.khaled.secure_employee_api.position.entity.Position;
import com.khaled.secure_employee_api.position.exception.PositionAlreadyExistsException;
import com.khaled.secure_employee_api.position.exception.PositionInUseException;
import com.khaled.secure_employee_api.position.exception.PositionNotFoundException;
import com.khaled.secure_employee_api.position.mapper.PositionMapper;
import com.khaled.secure_employee_api.position.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PositionService {

    private final PositionRepository positionRepository;

    private final PositionMapper positionMapper;

    private final EmployeeRepository employeeRepository;

    public PositionResponse createPosition(
            CreatePositionRequest request
    ) {

        if (positionRepository.existsByName(request.name())) {

            throw new PositionAlreadyExistsException(
                    "Position '" + request.name() + "' already exists."
            );
        }

        Position position =
                positionMapper.toEntity(request);

        Position savedPosition =
                positionRepository.save(position);

        log.info(
                "Position '{}' created successfully.",
                savedPosition.getName()
        );

        return positionMapper.toResponse(savedPosition);
    }

    @Transactional(readOnly = true)
    public PositionResponse getPosition(Long id) {

        return positionMapper.toResponse(
                findPositionById(id)
        );
    }

    @Transactional(readOnly = true)
    public Page<PositionResponse> getPositions(
            Pageable pageable
    ) {

        return positionRepository.findAll(pageable)
                .map(positionMapper::toResponse);
    }

    public PositionResponse updatePosition(
            Long id,
            UpdatePositionRequest request
    ) {

        Position position =
                findPositionById(id);

        if (!position.getName().equals(request.name())
                && positionRepository.existsByName(request.name())) {

            throw new PositionAlreadyExistsException(
                    "Position '" + request.name() + "' already exists."
            );
        }

        positionMapper.updatePositionFromRequest(
                request,
                position
        );

        Position updatedPosition =
                positionRepository.save(position);

        log.info(
                "Position '{}' updated successfully.",
                updatedPosition.getName()
        );

        return positionMapper.toResponse(updatedPosition);
    }

    public void deletePosition(Long id) {

        Position position =
                findPositionById(id);

        validatePositionCanBeDeleted(position);

        positionRepository.delete(position);

        log.info(
                "Position '{}' deleted successfully.",
                position.getName()
        );
    }

    private Position findPositionById(Long id) {

        return positionRepository.findById(id)
                .orElseThrow(() ->
                        new PositionNotFoundException(
                                "Position with id " + id + " not found."
                        )
                );
    }

    private void validatePositionCanBeDeleted(
            Position position
    ) {

        long employeeCount =
                employeeRepository.countByPosition(position);

        if (employeeCount > 0) {

            throw new PositionInUseException(
                    "Position '%s' contains %d employee%s. " +
                            "Move or remove them before deleting the position."
                                    .formatted(
                                            position.getName(),
                                            employeeCount,
                                            employeeCount == 1 ? "" : "s"
                                    )
            );
        }
    }
}