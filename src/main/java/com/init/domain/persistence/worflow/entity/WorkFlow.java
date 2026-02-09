package com.init.domain.persistence.worflow.entity;


import com.init.domain.persistence.common.model.DateAuditable;
import com.init.domain.persistence.user.entity.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_flow")
@Entity
public class WorkFlow extends DateAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_flow_id")
    private Long id;

    @Comment("회원 정보")
    @ManyToOne
    private User user;

    @Comment("학습 내용 제목")
    private String title;

    @Type(JsonType.class)
    @Column(name = "node_info", columnDefinition = "text")
    private Object nodeInfo;

    public static WorkFlow of(User user, String title, Object nodeInfo) {
        WorkFlow workFlow = new WorkFlow();
        workFlow.user = user;
        workFlow.title = title;
        workFlow.nodeInfo = nodeInfo;
        return workFlow;
    }

    public void update(String title, Object nodeInfo) {
        this.title = title;
        this.nodeInfo = nodeInfo;
    }
}
