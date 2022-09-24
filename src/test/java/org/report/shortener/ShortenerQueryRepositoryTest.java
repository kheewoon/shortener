package org.report.shortener;

import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.report.shortener.config.QueryDslConfiguration;
import org.report.shortener.dto.ShortenerApiDto;
import org.report.shortener.entity.QShortenerEntity;
import org.report.shortener.entity.ShortenerEntity;
import org.report.shortener.repository.ShortenerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfiguration.class)
@Slf4j
public class ShortenerQueryRepositoryTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    private ShortenerRepository shortenerRepository;

    private Long savedId3;

    @BeforeEach
    void setup(){
        shortenerRepository.save(
                ShortenerEntity.builder()
                        .shortId("KopQXFG")
                        .url("https://naver.com")
                        .build()
        );

        shortenerRepository.save(
                ShortenerEntity.builder()
                        .shortId("PjHEQFG")
                        .url("https://daum.net")
                        .build()
        );

        savedId3 = shortenerRepository.save(
                ShortenerEntity.builder()
                        .shortId("McvQer")
                        .url("https://google.co.kr")
                        .build()
        ).getId();
    }

    @Test
    void 단축_URL_단건조회(){

        var shortenerEntity = QShortenerEntity.shortenerEntity;
        String shortId = "McvQer";

        var findShortenerData = Optional.ofNullable(this.queryFactory
                .select(
                        Projections.fields(
                                ShortenerApiDto.class,
                                shortenerEntity.shortId.as("shortId"),
                                shortenerEntity.url.as("url"),
                                shortenerEntity.createdAt.as("createdAt")
                        )
                )
                .from(shortenerEntity)
                .where(
                        shortenerEntity.shortId.eq(shortId)
                )
                .fetchOne());

        //단축 URL 단건조회 결과 검증
        assertTrue(findShortenerData.isPresent());
        //단축 URL 데이터 검증
        Assertions.assertThat(findShortenerData.get().getShortId()).isEqualTo("McvQer");
        //원본 URL 데이터 검증
        Assertions.assertThat(findShortenerData.get().getUrl()).isEqualTo("https://google.co.kr");
    }

    @Test
    void 단축_URL_업데이트_조회후_업데이트_데이터_검증(){
        var shortenerEntity = QShortenerEntity.shortenerEntity;
        String shortId = "McvQerF";

        UpdateClause<JPAUpdateClause> updateBuilder = this.queryFactory.update(shortenerEntity);

         updateBuilder
            .set(shortenerEntity.shortId, shortId)
            .set(shortenerEntity.lastModifiedAt, LocalDateTime.now())
            .where(shortenerEntity.id.eq(savedId3))
            .execute();

        var findShortenerData = Optional.ofNullable(this.queryFactory
                .select(
                        Projections.fields(
                                ShortenerApiDto.class,
                                shortenerEntity.shortId.as("shortId"),
                                shortenerEntity.url.as("url"),
                                shortenerEntity.createdAt.as("createdAt")
                        )
                )
                .from(shortenerEntity)
                .where(
                        shortenerEntity.shortId.eq(shortId)
                )
                .fetchOne());

        //단축 URL 단건조회 결과 검증
        assertTrue(findShortenerData.isPresent());
        //단축 URL 데이터 검증
        Assertions.assertThat(findShortenerData.get().getShortId()).isEqualTo("McvQerF");
        //원본 URL 데이터 검증
        Assertions.assertThat(findShortenerData.get().getUrl()).isEqualTo("https://google.co.kr");
    }
}
