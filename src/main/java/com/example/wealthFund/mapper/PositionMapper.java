package com.example.wealthFund.mapper;


import com.example.wealthFund.dto.PositionDto;
import com.example.wealthFund.repository.entity.PositionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PositionMapper {

    PositionDto positionEntityToPositionDto(PositionEntity positionEntity);
}
