package com.chatio.socket.futures.auth.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.chatio.socket.futures.auth.dto.AuthResponse;
import com.chatio.socket.futures.auth.dto.RegisterRequest;
import com.chatio.socket.futures.user.model.Account;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    AuthResponse toResponse(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "online", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    void updateAccountFromRequest(RegisterRequest request, @MappingTarget Account account);

}
