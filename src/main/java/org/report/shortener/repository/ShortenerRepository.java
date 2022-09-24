package org.report.shortener.repository;

import org.report.shortener.entity.ShortenerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortenerRepository extends JpaRepository <ShortenerEntity, Long> {

    Optional<ShortenerEntity> findByUrl(String url);

    Optional<ShortenerEntity> findByShortId(String shortId);

}
