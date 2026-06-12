package com.chatio.socket.futures.user.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.chatio.socket.futures.user.dto.AccountResponse;
import com.chatio.socket.futures.user.dto.UpdateAccountRequest;
import com.chatio.socket.futures.user.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponse toResponse(Account account);

    List<AccountResponse> toResponses(List<Account> accounts);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "online", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateAccountFromRequest(UpdateAccountRequest request, @MappingTarget Account account);
}
