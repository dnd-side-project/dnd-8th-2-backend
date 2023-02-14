package com.dnd.reetplace.app.domain;

import com.dnd.reetplace.app.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE s3_file SET deleted_at = CURRENT_TIMESTAMP WHERE s3_file_id = ?")
@Entity
public class S3File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "s3_file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String uploaderIp;

    @Column(nullable = false)
    private String originalFileName;

    @Column(unique = true, nullable = false)
    private String storedFileName;

    @Column(unique = true, nullable = false)
    private String url;

    private LocalDateTime deletedAt;

    @Builder
    private S3File(Question question, String uploaderIp, String originalFileName, String storedFileName, String url) {
        this.question = question;
        this.uploaderIp = uploaderIp;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.url = url;
    }
}
