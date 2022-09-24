package org.report.shortener.common.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class UrlEncoder {

    @Value("${shortener_env.url_prefix}")
    private String URL_PREFIX;

    @Value("${shortener_env.base62_char}")
    private String BASE62_CHAR;

    @Value("${shortener_env.random_string_len}")
    private int RANDOM_STRING_LEN;

    private final int BASE62 = 62;

    private String encoding(long param) {
        StringBuffer sb = new StringBuffer();
        while(param > 0) {
            sb.append(BASE62_CHAR.charAt((int) (param % BASE62)));
            param /= BASE62;
        }
        return sb.toString() + RandomStringUtils.randomAlphabetic(RANDOM_STRING_LEN);
    }

    //Data 시퀀스를 기준으로 인코딩
    public String urlEncoder(Long dataSeq) throws NoSuchAlgorithmException {
        String encodeStr = encoding(dataSeq);
        log.info("base62 encode result:" + encodeStr);
        return encodeStr;
    }

}
