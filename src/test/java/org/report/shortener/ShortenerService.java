package org.report.shortener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.report.shortener.common.component.UrlEncoder;
import org.report.shortener.entity.ShortenerEntity;
import org.report.shortener.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@SpringBootTest
public class ShortenerService {

    @Autowired
    private UrlEncoder urlEncoder;

    @Autowired
    private ShortenerRepository shortenerRepository;

    @Nested
    @DisplayName("URL 축약, 원본 URL 디코딩 테스트")
    class categorySave{

        List<String> dupShortIdList = Arrays.asList("SKbtb", "SKbtH", "SKbtB");

        @BeforeEach
        void save_setup(){
            shortenerRepository.save(
                    ShortenerEntity.builder()
                            .url("https://naver.com")
                            .shortId("SKbtb")
                            .build()
            );

            shortenerRepository.save(
                    ShortenerEntity.builder()
                            .url("https://naver.com")
                            .shortId("SKbtH")
                            .build()
            );

            shortenerRepository.save(
                    ShortenerEntity.builder()
                            .url("https://naver.com")
                            .shortId("SKbtB")
                            .build()
            );
        }

        @Test
        void url단축_테스트() throws NoSuchAlgorithmException {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            String generatedString = urlEncoder.urlEncoder(1259874550L);

            log.info("result => {}", generatedString+ RandomStringUtils.randomAlphabetic(4));
        }

        @Test
        void url단축_중복검사() throws NoSuchAlgorithmException {
            Long savedId = 18L;
            String shortId = "SKbtb";

            while(shortenerRepository.findByShortId(shortId).isPresent()){
                shortId = urlEncoder.urlEncoder(savedId);
            }

            shortId = "SKbtH";

            while(shortenerRepository.findByShortId(shortId).isPresent()){
                shortId = urlEncoder.urlEncoder(savedId);
            }

            shortId = "SKbtB";

            while(shortenerRepository.findByShortId(shortId).isPresent()){
                shortId = urlEncoder.urlEncoder(savedId);
            }

            log.info("result => {}", shortId);
        }

    }


}
