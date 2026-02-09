package com.init.domain.persistence.userstudyhis.entity;

import com.init.domain.persistence.common.model.DateAuditable;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.domain.persistence.user.entity.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Map;

@Getter
@Table(name = "user_study_his")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UserStudyHis extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK")
    private Long id;

    @Comment("회원 정보")
    @ManyToOne
    private User user;

    @Comment("제품 간단 분류")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Comment("학습 내용 제목")
    private String title;

    @Type(JsonType.class)
    @Column(name = "view_info", columnDefinition = "text")
    private Object viewInfo;

    public static UserStudyHis of(User user, ProductType productType, String title, Object viewInfo) {
        UserStudyHis userStudyHis = new UserStudyHis();
        userStudyHis.user = user;
        userStudyHis.productType = productType;
        userStudyHis.title = title;
        userStudyHis.viewInfo = viewInfo;
        return userStudyHis;
    }

    public void update(String title, Object viewInfo) {
        this.title = title;
        this.viewInfo = viewInfo;
    }
}
