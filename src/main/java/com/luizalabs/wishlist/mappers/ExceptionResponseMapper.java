package com.luizalabs.wishlist.mappers;

import com.luizalabs.wishlist.exceptions.ExceptionResponse;
import com.luizalabs.wishlist.exceptions.GlobalException;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExceptionResponseMapper {
    ExceptionResponseMapper INSTANCE = Mappers.getMapper(ExceptionResponseMapper.class);

    ExceptionResponse globalExceptionToExceptionResponse(final GlobalException ex);
}