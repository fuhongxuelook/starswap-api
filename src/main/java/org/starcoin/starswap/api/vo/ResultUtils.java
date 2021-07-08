package org.starcoin.starswap.api.vo;

public class ResultUtils {

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return to("SUCCESS", "SUCCESS", data);
    }

    public static <T> Result<T> failure(String msg) {
        return to("FAILURE", msg, null);
    }

    public static <T> Result<T> failure() {
        return failure("FAILURE");
    }

    public static <T> Result<T> to(String code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
