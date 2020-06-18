package com.example.shorturl.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
public class CommonResponseDto<T> {

  private Result<T> result;
  private String code;
  private String message;
}
