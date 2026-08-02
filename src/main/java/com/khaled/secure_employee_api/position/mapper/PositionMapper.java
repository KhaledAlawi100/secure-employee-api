package com.khaled.secure_employee_api.position.mapper;

import com.khaled.secure_employee_api.position.dto.CreatePositionRequest;
import com.khaled.secure_employee_api.position.dto.PositionResponse;
import com.khaled.secure_employee_api.position.dto.UpdatePositionRequest;
import com.khaled.secure_employee_api.position.entity.Position;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    Position toEntity(CreatePositionRequest request);

    PositionResponse toResponse(Position position);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updatePositionFromRequest(
            UpdatePositionRequest request,
            @MappingTarget Position position
    );
}