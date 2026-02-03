## 디렉토리 구조

> - '...'이 추가된 디렉토리는 동일한 구조로 다른 도메인들이 추가될 수 있음을 의미합니다.
> - controller처럼 각 도메인 별로 대부분 하나만 생성된다면 모든 도메인을 한 디렉토리에 모아둡니다.

```
.
├── application             # 웹 계층 (사용자 인터페이스)
│   ├── dto                 # 요청/응답에 대한 DTO 객체
│   │   ├── user
│   │   │   ├── req
│   │   │   └── res
│   │   └── ...
│   ├── mapper              # Response DTO 객체 매핑을 담당
│   └── controller          # 실제 API 스펙 정의
│       └── api             # swagger 어노테이션을 위한 인터페이스
├── domain                  # 도메인 계층
│   ├── business            # 도메인의 비즈니스 로직
│   │   ├── user
│   │   │   ├── dto         # 도메인 서비스 입출력 객체
│   │   │   ├── service     # 도메인 서비스
│   │   │   ├── helper      # 도메인 헬퍼 모듈
│   │   │   └── error       # 도메인 에러 정의
│   │   └── ...
│   ├── cache               # 캐시 관점의 도메인
│   │   ├── refresh
│   │   │   ├── entity
│   │   │   └── repository
│   │   └── ...
│   └── persistence         # 영속성 관점의 도메인
│       ├── user
│       │   ├── entity
│       │   └── repository
│       └── ...
├── global                  # 공통 모듈
│   ├── config
│   ├── util                # 전역 유틸리티 모듈 (컴포넌트 등록 없이, static 메서드만을 활용)
│   ├── helper              # 전역 헬퍼 모듈 (컴포넌트로 등록하여 DI로서 활용)
│   └── ...
└── infra                   # 외부 시스템 연동 모듈 (도메인과 직접 연관되지 않은 모듈)
    ├── security            # JWT를 활용한 인증 관련 모듈
    ├── oauth               # 소셜 로그인 관련 모듈
    └── ...
```