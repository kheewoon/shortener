package org.report.shortener.Controller;

import lombok.RequiredArgsConstructor;
import org.report.shortener.dto.ShortenerDto;
import org.report.shortener.service.ShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ShortenerController {

    private final ShortenerService shortenerService;

    @GetMapping("short-links/r/{shortId}")
    public String shortLinks(@PathVariable String shortId){
        return "redirect:" + shortenerService.getShortLink(shortId).getUrl();
    }
}
