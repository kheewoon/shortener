package org.report.shortener.common.response;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.report.shortener.common.code.ErrorEnum;

@Getter
public class ErrorResponse {

    private final String status;
    private JSONObject errors = new JSONObject();

    public ErrorResponse(String status, int errorCode, String errorMessage) {
        this.status = status;
        this.errors.put("code", errorCode);
        this.errors.put("message", errorMessage);

    }

    public ErrorResponse(String status, ErrorEnum errorEnum) {
        this.status = status;
        this.errors.put("code", errorEnum.getStatus().value());
        this.errors.put("message", errorEnum.getMessage());

    }

}
