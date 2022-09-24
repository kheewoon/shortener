package org.report.shortener.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.report.shortener.common.code.ErrorEnum;

@Getter
@AllArgsConstructor
public class ErrorException extends RuntimeException{
    private final ErrorEnum errorEnum;
}
