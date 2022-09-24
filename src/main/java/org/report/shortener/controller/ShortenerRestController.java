package org.report.shortener.controller;

import lombok.RequiredArgsConstructor;
import org.report.shortener.dto.ShortenerDto;
import org.report.shortener.service.ShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class ShortenerRestController {

    private final ShortenerService shortenerService;

    /*
    * 단축 URL 생성 및 단축 URL 상세정보 조회
    * */
    @PostMapping("short-links")
    public ResponseEntity<Object> shortLinks(@RequestBody ShortenerDto shortenerDto) throws NoSuchAlgorithmException {
        return new ResponseEntity<>(shortenerService.shortenUrlProcess(shortenerDto), HttpStatus.OK);
    }

    /*
    * 단축 URL 상세정보 조회
    * */
    @GetMapping("short-links/{shortId}")
    public ResponseEntity<Object> getShortLink(@PathVariable String shortId){
        return new ResponseEntity<>(shortenerService.getShortLink(shortId), HttpStatus.OK);
    }

}
