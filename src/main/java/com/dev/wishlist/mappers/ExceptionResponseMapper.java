package com.dev.wishlist.mappers;

import com.dev.wishlist.exceptions.ExceptionResponse;
import com.dev.wishlist.exceptions.GlobalException;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExceptionResponseMapper {
    ExceptionResponseMapper INSTANCE = Mappers.getMapper(ExceptionResponseMapper.class);

    ExceptionResponse globalExceptionToExceptionResponse(final GlobalException ex);
}