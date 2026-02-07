# 빌드 스테이지
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Gradle 래퍼 및 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (캐싱 활용)
# dependencies 태스크를 실행하여 의존성을 미리 다운로드합니다.
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
# 테스트는 제외하고 실행 가능한 JAR 파일을 생성합니다.
RUN ./gradlew bootJar -x test --no-daemon

# 실행 스테이지
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일 복사
# 프로젝트 이름(init)과 버전(0.0.1-SNAPSHOT)에 맞게 복사합니다.
COPY --from=build /app/build/libs/init-0.0.1-SNAPSHOT.jar app.jar

# 기본 포트 노출
EXPOSE 8081

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
