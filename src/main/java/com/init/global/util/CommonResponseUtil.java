package com.init.global.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.init.global.annotation.Util;

@Util
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CommonResponseUtil<T>(boolean success, T data, String message) {
    public static <T> CommonResponseUtil<T> ok(T data) {
        return new CommonResponseUtil<>(true, data, "success");
    }

    public static <T> CommonResponseUtil<T> ok(T data, String message) {
        return new CommonResponseUtil<>(true, data, message);
    }

    public static <T> CommonResponseUtil<T> fail(String message) {
        return new CommonResponseUtil<>(false, null, message);
    }

}
