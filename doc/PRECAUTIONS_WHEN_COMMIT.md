## 커밋 시 주의사항
- 현 프로젝트는 husky + githook을 통한 commitlint와 spotless가 적용되어 있습니다.
- commitlint는 ./commitlint.config.js에 정의된 규칙에 따라 커밋 메시지의 형식을 검사합니다.
- spotless는 ./build.gradle에 정의된 규칙이 자동 적용되도록 spotlessApply 작업을 커밋 전에 실행합니다.
- 단, spotlessApply를 통해 자동으로 코드를 변경하고 이를 다시 staging하는 과정에서 해당 파일의 변경사항이 모두 업로드됩니다.
- 즉, 한 파일에서 일부 코드만 staging하여 커밋했더라도, spotlessApply 과정에서 변경된 모든 코드가 커밋에 포함될 수 있습니다.

## 'Unsupported class file major version 69' 오류 해결법
- 이 오류는 JDK 버전 불일치 또는 빌드 도구(Gradle 등)와 JDK의 호환성 문제로 발생합니다.
- Gradle에 설정된 JDK21로 실행되도록 로컬 JDK를 21로 맞춰서 해결할 수 있습니다.
- 해결 방법
  1. `export JAVA_HOME=$(/usr/libexec/java_home -v 21)`
  2. `./gradlew --stop clean build`
