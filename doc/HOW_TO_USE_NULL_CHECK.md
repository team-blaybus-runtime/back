## JSpecify + NullAway

- 이 프로젝트는 JSpecify + NullAway을 활용하여 컴파일 시점에 null 안전성을 강화합니다.
- JSpecify는 Java에서 Null 안전성을 제공하는 주석 표준입니다.
- NullAway는 정적 분석 도구로, Null 관련 오류를 컴파일 타임에 감지합니다.
- 이 프로젝트에서는 @NullMarked가 선언된 클래스에 대해 @Nullable이 명시된 파라미터가 NPE를 유발할 수 있는지 검사합니다.
    - IDE에서 NPE를 유발할 수 있는 코드에 대해 Warning을 표시합니다.
    - NPE를 유발하는 코드가 존재한다면 컴파일이 실패합니다.

## 사용 방법

- Null 안정성을 검사하고 싶은 클래스에 `@NullMarked` 주석을 추가합니다.
- null이 될 수 있는 파라미터에는 `@Nullable` 주석을 추가합니다.
- NullAway가 컴파일 시점에 null 관련 오류를 감지하여 경고를 표시합니다.

## 사용 예시

### 올바른 예

```java
// str은 str.length() 호출 시 NPE를 유발할 수 있으므로 NullAway가 경고를 표시하고, 컴파일이 실패합니다.
@NullMarked
public class Example {
    public void test(@Nullable String str) {
        System.out.println(str.length());
    }
}
```

### 잘못된 예

```java
// String str이 @Nullable을 통해 null이 됐음을 명시하지 않았으므로 NullAway가 경고를 표시하지 않고, 컴파일이 성공합니다.
@NullMarked
public class Example {
    public void test(String str) {
        System.out.println(str.length());
    }
}
```

```java
// str은 null이 될 수 있으나 NPE를 유발하지 않는 안전한 사용법이므로 NullAway가 경고를 표시하지 않고, 컴파일이 성공합니다.
@NullMarked
public class Example {
    public void test(@Nullable String str) {
        System.out.println(str);
    }
}
```

```java
// @NullMarked가 선언되지 않았으므로 NullAway가 검사하지 않고, 컴파일이 성공합니다.
public class Example {
    public void test(@Nullable String str) {
        System.out.println(str.length());
    }
}
```