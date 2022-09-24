package org.report.shortener.repository;

import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.RequiredArgsConstructor;
import org.report.shortener.dto.ShortenerApiDto;
import org.report.shortener.dto.ShortenerDto;
import org.report.shortener.entity.QShortenerEntity;
import org.report.shortener.entity.ShortenerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShortenerQueryRepository {

    private final JPAQueryFactory queryFactory;

    /*
    * 단축 URL 정보 단건 조회
    * */
    public Optional<ShortenerApiDto> findShortener(String shortId){
        var shortenerEntity = QShortenerEntity.shortenerEntity;

        return Optional.ofNullable(this.queryFactory
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
    }

    /*
     * 단축 URL ID 업데이트
     * */
    public Long shortIdUpdate(Long id, String shortId) {
        var shortenerEntity = QShortenerEntity.shortenerEntity;

        UpdateClause<JPAUpdateClause> updateBuilder = this.queryFactory.update(shortenerEntity);

        return updateBuilder
                .set(shortenerEntity.shortId, shortId)
                .set(shortenerEntity.lastModifiedAt, LocalDateTime.now())
                .where(shortenerEntity.id.eq(id))
                .execute();
    }

}
