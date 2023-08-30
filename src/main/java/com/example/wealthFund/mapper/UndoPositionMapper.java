package com.example.wealthFund.mapper;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.dto.positionDtos.UndoPositionDto;
import com.example.wealthFund.repository.entity.OperationHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UndoPositionMapper {

    UndoPositionDto operationHistoryToUndoPositionDto(OperationHistory operationHistory);

    AddPositionDto UndoPositionDtoToAddPositionDto(UndoPositionDto undoPositionDto);

    SubtractPositionDto UndoPositionDtoToSubtractPositionDto(UndoPositionDto undoPositionDto);
}
