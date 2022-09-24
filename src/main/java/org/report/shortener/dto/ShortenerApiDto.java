package org.report.shortener.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ShortenerApiDto {

    /*
     * 단축 URL ID
     * */
    private String shortId;

    /*
    * 원본 URL(축약할 URL)
    * */
    private String url;
    
    /*
    * 등록 시간
    * */
    private LocalDateTime createdAt;


    public ShortenerApiDto(String shortId, String url, LocalDateTime createdAt) {
        this.shortId = shortId;
        this.url = url;
        this.createdAt = createdAt;
    }
}
