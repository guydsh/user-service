package com.smc.user.service;

import com.smc.user.dto.PageDto;
import org.springframework.data.domain.Page;

public interface IPageService {
    <S, T> PageDto<T> convertToPageDto(Page<S> page, Converter<S, T> converter);
}
