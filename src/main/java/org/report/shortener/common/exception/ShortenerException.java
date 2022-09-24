package org.report.shortener.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.report.shortener.common.code.ShortenerEnum;

@Getter
@AllArgsConstructor
public class ShortenerException extends RuntimeException {
    private final ShortenerEnum shortenerEnum;
}
