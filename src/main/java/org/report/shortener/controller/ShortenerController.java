package org.report.shortener.controller;

import lombok.RequiredArgsConstructor;
import org.report.shortener.service.ShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ShortenerController {

    private final ShortenerService shortenerService;

    /*
    * 단축 URL의 원본 URL로 요청 리다이렉트
    * */
    @GetMapping("short-links/r/{shortId}")
    public String shortLinks(@PathVariable String shortId){
        return "redirect:" + shortenerService.getShortLink(shortId).getUrl();
    }
}
