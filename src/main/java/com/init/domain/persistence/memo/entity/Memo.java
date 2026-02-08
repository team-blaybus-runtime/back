package com.init.domain.persistence.memo.entity;


import com.init.domain.persistence.common.model.DateAuditable;
import com.init.domain.persistence.engineering.entity.ProductType;
import com.init.domain.persistence.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "memo")
@Entity
public class Memo extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Comment("메모 작성자")
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private ProductType productType;
    @Comment("메모 제목")
    private String title;
    @Column(columnDefinition = "TEXT")
    @Comment("메모 내용")
    private String content;

    public static Memo of(User user, ProductType productType, String title, String content) {
        Memo memo = new Memo();
        memo.productType = productType;
        memo.user = user;
        memo.title = title;
        memo.content = content;
        return memo;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
