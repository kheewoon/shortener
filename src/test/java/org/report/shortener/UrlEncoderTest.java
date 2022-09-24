package org.report.shortener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.report.shortener.common.component.UrlEncoder;
import org.report.shortener.repository.ShortenerRepository;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UrlEncoderTest {

    @Mock
    private UrlEncoder urlEncoder;

    @Mock
    private ShortenerRepository shortenerRepository;

    @Nested
    @DisplayName("URL 단축 단위 테스트")
    class encode{

        Long param;

        String BASE62_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        int RANDOM_STRING_LEN = 4;

        int BASE62 = 62;

        @BeforeEach
        void init(){
            param = 4895581540L;
        }

        @Test
        void URL_단축테스트(){
            StringBuffer sb = new StringBuffer();
            while(param > 0) {
                sb.append(BASE62_CHAR.charAt((int) (param % BASE62)));
                param /= BASE62;
            }
            String shortId = sb.toString() + RandomStringUtils.randomAlphabetic(RANDOM_STRING_LEN);

            log.info("result ==> {}", shortId);

            //단축 URL 생성 여부 검증
            assertNotNull(shortId);
            //단축 URL 데이터 길이 검증
            Assertions.assertThat(shortId.length()).isEqualTo(10);
        }
    }

}
