package com.init.domain.persistence.chat.repository;

import com.init.domain.persistence.chat.entity.ChatMessage;
import com.init.domain.persistence.chat.entity.QChatMessage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomChatRepositoryImpl implements CustomChatRepository{

    private final JPAQueryFactory factory;

    @Override
    public Slice<ChatMessage> findByUserIdAndHistoryId(Long userId, Long historyId, Long lastId, String order, Integer limit) {
        QChatMessage chatMessage = QChatMessage.chatMessage;

        boolean isAsc = "asc".equalsIgnoreCase(order);

        List<ChatMessage> content = factory.selectFrom(chatMessage)
                .where(
                        chatMessage.userHisId.eq(historyId),
                        ltGtLastId(chatMessage, lastId, isAsc)
                )
                .orderBy(isAsc ? chatMessage.id.asc() : chatMessage.id.desc())
                .limit(limit + 1)
                .fetch();

        boolean hasNext = false;
        if (content.size() > limit) {
            content.remove(limit.intValue());
            hasNext = true;
        }

        return new SliceImpl<>(content, PageRequest.of(0, limit), hasNext);
    }

    /**
     * lastId 기반의 동적 조건 생성 (No-Offset)
     */
    private BooleanExpression ltGtLastId(QChatMessage chatMessage, Long lastId, boolean isAsc) {
        if (lastId == null) {
            return null;
        }

        // 오름차순(asc)이면 lastId보다 큰 데이터(gt), 내림차순(desc)이면 lastId보다 작은 데이터(lt)
        return isAsc ? chatMessage.id.gt(lastId) : chatMessage.id.lt(lastId);
    }
}
