package org.starcoin.starswap.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "响应数据")
public class Result<T> {

    @ApiModelProperty("状态码，成功为SUCCESS，失败为FAILURE")
    private String code;

    @ApiModelProperty("描述信息")
    private String message;

    @ApiModelProperty("响应数据")
    private T data;

    Result(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
