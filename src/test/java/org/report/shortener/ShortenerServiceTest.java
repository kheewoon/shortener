package org.report.shortener;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.report.shortener.common.code.ShortenerEnum;
import org.report.shortener.common.component.UrlEncoder;
import org.report.shortener.common.exception.ShortenerException;
import org.report.shortener.dto.ShortenerApiDto;
import org.report.shortener.dto.ShortenerDto;
import org.report.shortener.entity.ShortenerEntity;
import org.report.shortener.repository.ShortenerQueryRepository;
import org.report.shortener.repository.ShortenerRepository;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class ShortenerServiceTest {

    @Mock
    private ShortenerRepository shortenerRepository;

    @Mock
    private ShortenerQueryRepository shortenerQueryRepository;

    @Mock
    private UrlEncoder urlEncoder;

    @Nested
    @DisplayName("단축 URL 상세정보 저장 로직")
    class ShortenerSave{

        ShortenerDto shortenerDto = new ShortenerDto();
        ShortenerEntity parameterEntity;

        @BeforeEach
        void init(){
            shortenerDto.setUrl("https://naver.com");

            parameterEntity = ShortenerEntity.builder()
                    .url(shortenerDto.getUrl())
                    .build();
        }

        @Test
        void 원본URL미존재시_URL_단축정보_저장_및_단축정보_상세정보_조회() throws NoSuchAlgorithmException {

            given(shortenerRepository.findByUrl(anyString())).willReturn(Optional.ofNullable(null));
            given(shortenerRepository.save(any(ShortenerEntity.class))).willReturn(ShortenerEntity.builder().id(17L).url("https://daum.net").shortId("kYWTVFRYwx").build());
            given(shortenerQueryRepository.findShortener(anyString())).willReturn(Optional.of(new ShortenerApiDto("kYWTVFRYwx", "https://daum.net", LocalDateTime.now())));
            given(shortenerQueryRepository.shortIdUpdate(any(),any())).willReturn(1L);

            ShortenerApiDto resultShortenerApiDto;

            //URL 존재 여부 조회
            Optional<ShortenerEntity> findShortenerEntity = shortenerRepository.findByUrl(shortenerDto.getUrl());

            //
            String shortId = "";

            //URL 존재시 기존 URL 정보 반환
            if(findShortenerEntity.isPresent()){
                var shortenerEntity = findShortenerEntity.get();
                resultShortenerApiDto = new ShortenerApiDto(shortenerEntity.getShortId(), shortenerEntity.getUrl(), shortenerEntity.getCreatedAt());
            }
            //URL 미존재시 URL 정보 등록
            else{
                //URL 정보 등록 및 PK 반환
                Long savedId = shortenerRepository.save(
                        parameterEntity
                ).getId();

                //단축 URL ID 생성 및 중복검사
                shortId = getShortId(savedId);

                //단축 URL ID 업데이트
                shortenerQueryRepository.shortIdUpdate(savedId, shortId);

            }

            //등록된 URL 정보 조회후 리턴
            var findShortenerData = shortenerQueryRepository.findShortener(shortId).orElseThrow(() -> new ShortenerException(ShortenerEnum.URL_FIND_FAIL));

            //단축 URL 상세정보 NULL 여부 검증
            assertNotNull(findShortenerData);
            //단축 URL ID 검증
            Assertions.assertThat(findShortenerData.getShortId()).isEqualTo("kYWTVFRYwx");
            //원본 URL 검증
            Assertions.assertThat(findShortenerData.getUrl()).isEqualTo("https://daum.net");

            //함수 호출 검증
            verify(shortenerRepository).findByUrl(shortenerDto.getUrl());
            verify(shortenerRepository).save(parameterEntity);
            verify(shortenerQueryRepository).shortIdUpdate(17L,"kYWTVFRYwx");
            verify(shortenerQueryRepository).findShortener("kYWTVFRYwx");
        }

        @Test
        void 원본URL존재시_URL_단축정보_저장_및_단축정보_상세정보_조회() throws NoSuchAlgorithmException {

            given(shortenerRepository.findByUrl(anyString())).willReturn(Optional.ofNullable(ShortenerEntity.builder().id(17L).url("https://daum.net").shortId("kYWTVFRYwx").build()));

            ShortenerApiDto resultShortenerApiDto = null;

            //URL 존재 여부 조회
            Optional<ShortenerEntity> findShortenerEntity = shortenerRepository.findByUrl(shortenerDto.getUrl());

            //URL 존재시 기존 URL 정보 반환
            if(findShortenerEntity.isPresent()){
                var shortenerEntity = findShortenerEntity.get();
                resultShortenerApiDto = new ShortenerApiDto(shortenerEntity.getShortId(), shortenerEntity.getUrl(), shortenerEntity.getCreatedAt());
            }
            
            //단축 URL 상세정보 NULL 여부 검증
            assertNotNull(resultShortenerApiDto);
            //단축 URL ID 검증
            Assertions.assertThat(resultShortenerApiDto.getShortId()).isEqualTo("kYWTVFRYwx");
            //원본 URL 검증
            Assertions.assertThat(resultShortenerApiDto.getUrl()).isEqualTo("https://daum.net");

            //함수 호출 검증
            verify(shortenerRepository).findByUrl(shortenerDto.getUrl());
        }

        @Test
        void 단축_URL_ID_생성_및_중복검사_중복X() throws NoSuchAlgorithmException {

            given(urlEncoder.urlEncoder(any())).willReturn("kYWTVFRYwx");
            given(shortenerRepository.findByShortId(anyString())).willReturn(Optional.ofNullable(null));
            Long savedId = 17L;

            String shortId = urlEncoder.urlEncoder(savedId);

            //중복된 단축 URL이 없을시 while문 종료
            while(shortenerRepository.findByShortId(shortId).isPresent()){
                shortId = urlEncoder.urlEncoder(savedId);
            }

            assertNotNull(shortId);
            Assertions.assertThat(shortId).isEqualTo("kYWTVFRYwx");

            //함수 호출 검증
            verify(urlEncoder).urlEncoder(savedId);
            verify(shortenerRepository).findByShortId(shortId);
        }

        @Test
        void 단축_URL_ID_생성_및_중복검사_중복O() throws NoSuchAlgorithmException {

            given(urlEncoder.urlEncoder(any())).willReturn("kYWTVFRYwx");
            given(shortenerRepository.findByShortId(anyString())).willReturn(Optional.ofNullable(ShortenerEntity.builder().id(17L).url("https://daum.net").shortId("kYWTVFRYwx").build()));
            Long savedId = 17L;

            String shortId = urlEncoder.urlEncoder(savedId);

            //중복 체크 루프문 반복 횟수
            int loopCnt = 0;

            //중복된 단축 URL이 없을시 while문 종료
            while(shortenerRepository.findByShortId(shortId).isPresent()){
                shortId = "kYWTVFRYwx5";
                loopCnt++;
                break;
            }

            assertNotNull(shortId);
            Assertions.assertThat(shortId).isNotEqualTo("kYWTVFRYwx");
            Assertions.assertThat(loopCnt).isEqualTo(1);

            //함수 호출 검증
            verify(urlEncoder).urlEncoder(savedId);
            verify(shortenerRepository).findByShortId("kYWTVFRYwx");
        }

        //단축 URL ID 생성 및 중복검사
        public String getShortId(Long savedId) throws NoSuchAlgorithmException {

            given(urlEncoder.urlEncoder(any())).willReturn("kYWTVFRYwx");
            given(shortenerRepository.findByShortId(anyString())).willReturn(Optional.ofNullable(null));

            String shortId = urlEncoder.urlEncoder(savedId);

            //중복된 단축 URL이 없을시 while문 종료
            while(shortenerRepository.findByShortId(shortId).isPresent()){
                shortId = urlEncoder.urlEncoder(savedId);
            }

            return shortId;
        }

    }

    @Nested
    @DisplayName("단축 URL 상세정보 조회 로직")
    class ShortenerFind{

        @Test
        void 단축_URL_상세정보_단건_조회(){

            String shortId = "kYWTVFRYwx";
            given(shortenerQueryRepository.findShortener(anyString())).willReturn(Optional.of(new ShortenerApiDto("kYWTVFRYwx", "https://daum.net", LocalDateTime.now())));

            var findShortenerData = shortenerQueryRepository.findShortener(shortId).orElseThrow(() -> new ShortenerException(ShortenerEnum.URL_FIND_FAIL));

            //단축 URL 상세정보 NULL 여부 검증
            assertNotNull(findShortenerData);
            //단축 URL ID 검증
            Assertions.assertThat(findShortenerData.getShortId()).isEqualTo("kYWTVFRYwx");
            //원본 URL 검증
            Assertions.assertThat(findShortenerData.getUrl()).isEqualTo("https://daum.net");

            //함수 호출 검증
            verify(shortenerQueryRepository).findShortener(shortId);
        }
    }
}
