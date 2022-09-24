package org.report.shortener.common.response;

import lombok.Getter;

@Getter
public class CommonResponse<T>{

    private final String code;
    private final String message;
    private T data;
    private final String status;


    public CommonResponse(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


}
