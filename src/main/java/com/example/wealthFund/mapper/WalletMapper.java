package com.example.wealthFund.mapper;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.dto.walletDtos.WalletResponseDto;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {

    Set<WalletResponseDto> walletSetEntityToWalletResponseDtoSet(Set<WalletEntity> wallets);

}
