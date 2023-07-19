package com.local.openapi.core.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ExceptionResponse {

       LocalDateTime timestamp;
       Integer status;
       private String error;


}
