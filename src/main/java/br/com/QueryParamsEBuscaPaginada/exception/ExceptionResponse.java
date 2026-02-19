package br.com.QueryParamsEBuscaPaginada.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {}