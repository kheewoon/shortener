package org.report.shortener.entity;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Entity
@Table(name = "shortener")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class ShortenerEntity extends BaseEntity{

    @Id @Column(name = "id", nullable = false)
    @GeneratedValue
    @Comment("id")
    private Long id;

    @Column(name = "short_id", length = 30)
    @Comment("단축 URL ID")
    private String shortId;

    @Column(name = "url", nullable = false, length = 2000)
    @Comment("원본 URL")
    private String url;

}
