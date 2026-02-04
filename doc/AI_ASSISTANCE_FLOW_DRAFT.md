---

### 1. 시스템 개요 (System Overview)
본 시스템은 단순한 질의응답을 넘어, 사용자의 이전 대화 맥락을 이해하고 사내 엔지니어링 지식 베이스에서 정확한 정보를 찾아 답변하는 **맥락 유지형 RAG 챗봇**입니다.

*   **핵심 기술**: Query Rewriting(쿼리 재작성), Vector RAG(지식 검색), Cumulative Summary(누적 요약), Event-Driven History(비동기 이력 관리)

---

### 2. 채팅 프로세스 흐름 (Chat Process Flow)

사용자가 질문을 던졌을 때 서버 내부에서 일어나는 상세 단계입니다.

#### ① 문맥 로드 및 준비 (Preparation)
1.  **세션 확인**: 요청된 `chatRoomId`를 확인하고, 없으면 새로운 `ChatRoom`을 생성합니다.
2.  **데이터 로드**: 해당 채팅방의 **이전 대화 요약(Summary)**과 **최근 10개의 대화 이력(History)**을 DB에서 불러옵니다.

#### ② 쿼리 재작성 (Query Rewriting)
- 해당 부분은 현재 완벽하지 않아 최적화가 필요함!
3.  **지시어 해결**: 사용자의 질문에 "그 부품", "거기" 등의 대명사가 있을 경우, 이전 요약과 이력을 바탕으로 GPT가 독립적인 검색이 가능한 쿼리로 재작성합니다.
    *   *예: "그거 수리법은?" → "드론 탄소섬유 암(Arm)의 수리 방법은?"*

#### ③ 지식 검색 (Retrieval)
4.  **벡터 검색**: 재작성된 쿼리를 임베딩하여 Postgres 벡터 DB에서 가장 유사도가 높은 엔지니어링 지식(Engineering Knowledge) TOP 5를 추출합니다.
5.  **메타데이터 매핑**: 검색된 ID를 기반으로 상세 기술 정보를 로드합니다.

#### ④ 답변 생성 (Generation)
6.  **프롬프트 구성**: [전문 지식 + 이전 요약 + 최근 이력 + 현재 질문]을 조합하여 엔지니어급 답변을 생성하도록 최적화된 프롬프트를 만듭니다.
7.  **AI 호출**: OpenAI API를 통해 최종 답변을 생성합니다.

#### ⑤ 비동기 사후 처리 (Post-Processing)
8.  **이벤트 발행**: 답변 반환과 동시에 `ChatProcessEvent`를 발행합니다. (사용자 응답 속도 최적화)
9.  **이력 저장**: 비동기 리스너가 질문과 답변을 DB에 저장합니다.
10. **요약 갱신**: 메시지 개수가 10개 단위가 될 경우, 기존 요약에 새 내용을 합쳐 **누적 요약(Cumulative Summary)**을 업데이트합니다.

---

### 3. 사용자 흐름 (User Flow)

사용자 관점에서의 서비스 이용 시나리오입니다.

#### Step 1: 첫 질문 (New Exploration)
*   **Action**: "드론 프레임 재질이 뭐야?"
*   **Result**: 시스템이 신규 채팅방을 생성하고, RAG를 통해 탄소섬유 재질에 대한 정보를 제공합니다.

#### Step 2: 문맥 기반 후속 질문 (Deep Dive)
*   **Action**: "그 재질의 장점이 뭐야?" (주어 생략)
*   **Result**: 시스템은 이전 대화의 '탄소섬유'를 기억하여 별도의 부연 설명 없이도 정확한 답변을 내놓습니다.

#### Step 3: 장기 대화 및 요약 (Summary Persistence)
*   **Action**: 수십 개의 질문을 주고받은 후 나중에 다시 접속하여 "어제 말한 부품의 유지보수 주기는?"
*   **Result**: 대화가 길어져 과거 이력이 잘리더라도, **누적 요약본**이 프롬프트에 포함되어 있어 어제의 맥락을 잃지 않고 답변합니다.

---

### 4. 주요 컴포넌트 역할 (Key Components)

| 컴포넌트 | 역할 |
| :--- | :--- |
| **`ChatService`** | 전체 채팅 비즈니스 로직의 오케스트레이션 (제어타워) |
| **`ChatQueryRewriter`** | 대화 맥락을 반영하여 질문을 검색용 쿼리로 변환 |
| **`KnowledgeRetriever`** | 벡터 DB 검색을 통한 기술 지식 추출 (RAG 핵심) |
| **`ChatHistoryManager`** | 채팅방, 메시지, 요약 데이터의 CRUD 및 로드 담당 |
| **`ChatSummaryService`** | GPT를 활용한 기존 요약 + 새 대화의 누적 요약 생성 |
| **`ChatEventListener`** | 비동기 스레드에서 메시지 저장 및 요약 트리거 처리 |

---


## API 사용 FLOW 

### 1. API 명세

| 항목 | 내용                                                             |
| :--- |:---------------------------------------------------------------|
| **Endpoint** | `POST /ai-chat`                                                |
| **Content-Type** | `application/json`                                             |
| **Authentication** | `Authorization: Bearer {JWT_TOKEN}`  현재는 사용 x 추후 로그인 방식 확정시 사용 |

#### 요청 객체 (ChatReq)
*   `content` (String): 사용자 질문 내용 (필수)
*   `productType` (Enum): 제품 분류 (예: `Drone`, `Robot_Arm` 등) (필수)
*   `chatRoomId` (Long): 채팅방 ID. **최초 질문 시 생략**, 대화 이어가기 시 필수.
*   `userId` (Long): 현재는 DTO로 전달 (추후 보안 적용 시 인증 정보에서 자동 추출 예정).

#### 응답 객체 (AiChatRes)
*   `answer` (String): AI가 생성한 답변
*   `chatRoomId` (Long): 해당 대화가 속한 채팅방 ID

---

### 2. 세션별 API 활용 시나리오

#### 시나리오 A: 새로운 대화 시작 (New Session)
새로운 주제로 대화를 시작할 때의 흐름입니다.

1.  **API 호출**: `chatRoomId`를 제외하고 요청을 보냅니다.
    ```json
    {
      "content": "드론 프레임의 메인 재질이 무엇인가요?",
      "productType": "Drone",
      "userId": 1
    }
    ```
2.  **서버 응답**: 새로운 `chatRoomId`와 함께 답변이 반환됩니다.
    ```json
    {
      "answer": "드론 프레임은 주로 탄소 섬유(Carbon Fiber) 재질로 제작됩니다...",
       "chatRoomId": 102
    }
    ```
3.  **클라이언트 처리**: 반환된 `chatRoomId: 101`을 로컬 변수나 상태에 저장합니다.

---

#### 시나리오 B: 기존 대화 이어가기 (Context Persistence)
이전 질문의 맥락(Context)을 유지하며 추가 질문을 할 때의 흐름입니다.

1.  **API 호출**: 이전 응답에서 받은 `chatRoomId`를 포함하여 요청을 보냅니다.
    ```json
    {
      "content": "그 재질의 장점과 단점을 알려줘.",
      "productType": "Drone",
      "chatRoomId": 101,
      "userId": 1
    }
    ```
    *   **포인트**: 사용자가 "그 재질"이라고만 해도, 서버는 `chatRoomId: 101`의 이력을 조회하여 이를 "탄소 섬유"로 해석(Query Rewriting)합니다.
2.  **서버 응답**: 동일한 세션 ID와 함께 문맥이 반영된 답변이 반환됩니다.
    ```json
    {
       "answer": "탄소 섬유(Carbon Fiber)의 주요 장점은 경량화와 고강성입니다. 반면 단점으로는...",
       "chatRoomId": 101
    }
    ```

---