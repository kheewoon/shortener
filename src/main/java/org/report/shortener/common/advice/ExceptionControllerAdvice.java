package org.report.shortener.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.report.shortener.common.code.CommonEnum;
import org.report.shortener.common.code.ErrorEnum;
import org.report.shortener.common.exception.ErrorException;
import org.report.shortener.common.exception.ShortenerException;
import org.report.shortener.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler({
            ShortenerException.class
    })
    public ResponseEntity ShortenerException(final ShortenerException ex) {
        //log.error("CommonException: {}", ex.getErrorCode());

        return new ResponseEntity<>(new ErrorResponse<>(CommonEnum.STATUS_FAIL.getName(), ex.getShortenerEnum().getCode(), ex.getShortenerEnum().getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 400
    @ExceptionHandler({
            ErrorException.class
    })
    public ResponseEntity BadRequestException(final ErrorException ex) {
        //log.error("CommonException: {}", ex.getErrorCode());

        return new ResponseEntity<>(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), ErrorEnum.BAD_REQUEST.getStatus().value(), ErrorEnum.BAD_REQUEST.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 405
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity handleBadCredentialsException(final HttpRequestMethodNotSupportedException ex) {
        log.warn("error", ex);
        return new ResponseEntity<>(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), ErrorEnum.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity handleAll(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        return new ResponseEntity<>(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), ErrorEnum.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
