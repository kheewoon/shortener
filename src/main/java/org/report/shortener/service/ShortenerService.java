package org.report.shortener.service;

import lombok.RequiredArgsConstructor;
import org.report.shortener.common.code.ShortenerEnum;
import org.report.shortener.common.component.UrlEncoder;
import org.report.shortener.common.exception.ShortenerException;
import org.report.shortener.dto.ShortenerApiDto;
import org.report.shortener.dto.ShortenerDto;
import org.report.shortener.entity.ShortenerEntity;
import org.report.shortener.repository.ShortenerQueryRepository;
import org.report.shortener.repository.ShortenerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShortenerService {

    private final UrlEncoder urlEncoder;

    private final ShortenerRepository shortenerRepository;

    private final ShortenerQueryRepository shortenerQueryRepository;

    /*
    * 단축 URL 생성 및 단축 URL 상세정보 조회
    * */
    @Transactional
    public ShortenerApiDto shortenUrlProcess(ShortenerDto shortenerDto) throws NoSuchAlgorithmException {

        //파라미터로 넘어온 URL 데이터로 단축 URL 존재 여부 조회
        Optional<ShortenerEntity> findShortenerEntity = shortenerRepository.findByUrl(shortenerDto.getUrl());

        //생성된 단축 URL 저장 변수
        String shortId = "";

        //URL 존재시 기존 URL 정보 반환
        if(findShortenerEntity.isPresent()){
            var shortenerEntity = findShortenerEntity.get();
            return new ShortenerApiDto(shortenerEntity.getShortId(), shortenerEntity.getUrl(), shortenerEntity.getCreatedAt());
        }
        //URL 미존재시 URL 정보 등록
        else{
            //URL 정보 등록 및 PK 반환
            Long savedId = shortenerRepository.save(
                                ShortenerEntity.builder()
                                    .url(shortenerDto.getUrl())
                                    .build()
                            ).getId();

            //단축 URL ID 생성 및 중복검사
            shortId = getShortId(savedId);

            //단축 URL ID 업데이트
            shortenerQueryRepository.shortIdUpdate(savedId, shortId);

        }
        
        //등록된 URL 정보 조회후 리턴
        return shortenerQueryRepository.findShortener(shortId).orElseThrow(() -> new ShortenerException(ShortenerEnum.URL_FIND_FAIL));
    }

    /*
    * 단축 URL 상세정보 조회
    * */
    @Transactional
    public ShortenerApiDto getShortLink(String shortId){
        //등록된 URL 정보 조회후 리턴
        return shortenerQueryRepository.findShortener(shortId).orElseThrow(() -> new ShortenerException(ShortenerEnum.URL_FIND_FAIL));
    }

    /*
    * 단축 URL ID 생성 및 중복검사
    * */
    @Transactional
    public String getShortId(Long savedId) throws NoSuchAlgorithmException {
        String shortId = urlEncoder.urlEncoder(savedId);

        //중복된 단축 URL이 없을시 while문 종료
        while(shortenerRepository.findByShortId(shortId).isPresent()){
            shortId = urlEncoder.urlEncoder(savedId);
        }

        return shortId;
    }
}
