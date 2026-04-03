package br.com.AuthenticationJWTAndSpringSecurity.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {}