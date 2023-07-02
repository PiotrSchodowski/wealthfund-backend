package com.example.wealthFund.mapper;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.model.AssetDirectory;
import com.example.wealthFund.repository.entity.AssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssetMapper {

    List<AssetDto> assetListToAssetDtoList(List<AssetEntity> assetList);

    List<AssetEntity> assetDirectoryListToAssetEntityList(List<AssetDirectory> assetDirectoryList);

    AssetEntity assetDtoToAssetEntity(AssetDto assetDto);

    AssetDto assetEntityToAssetDto(AssetEntity assetEntity);
}
