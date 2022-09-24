package org.report.shortener.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShortenerEnum {
    /*
     * 200010 : URL 정상등록, 정상 단축 완료
     */
    URL_SAVE_AND_SHORTEN_SUCESS("200010", "URL 정상등록, 정상 단축 완료"),

    /*
     * 200020 : 단축 URL 정보 조회 완료
     */
    URL_FIND_SUCESS("200020", "단축 URL 정보 조회 완료"),

    /*
     * 200030 : URL 데이터가 존재하지 않습니다.
     */
    URL_FIND_FAIL("200030", "URL 데이터가 존재하지 않습니다.");

    private final String code;
    private final String message;
}
