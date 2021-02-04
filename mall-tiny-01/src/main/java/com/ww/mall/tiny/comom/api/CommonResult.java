package com.ww.mall.tiny.comom.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-11-26 15:07
 * @describe:   返回结果封装类 注意要加上lombok
 */
@Data
public class CommonResult<T> {

    private long code;
    private String message;
    private T data;

    public CommonResult() {
    }

    public CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResult(long code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public CommonResult(IResultCode iResultCode) {
        this.code = iResultCode.getCode();
        this.message = iResultCode.getMessage();
        this.data = null;
    }


    //封装常用返回方法

    /**
     * 成功返回 无需传入参数
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<>(ResultCode.SUCCESS);
    }

    /**
     * 成功返回 无需传入参数
     */
    public static <T> CommonResult<T> success(String message,T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(),message,data);
    }

    /**
     * 成功返回 第一个<T>代表这是一个泛型方法  CommonResult<T>代表返回值是CommonResult且CommonResult中的data是T类型
     * 成功返回 传入data
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }


    /**
     * 操作失败 不需要传入信息
     */
    public static <T> CommonResult<T> failed() {
        return new CommonResult<T>(ResultCode.FAILED);
    }

    /**
     * 操作失败 传入错误代码
     */
    public static <T> CommonResult<T> failed(long code) {
        return new CommonResult<T>(code, ResultCode.FAILED.getMessage());
    }

    /**
     * 操作失败 传入错误信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(),message);
    }

    /**
     * 操作失败 传入错误代码和错误信息
     */
    public static <T> CommonResult<T> failed(long code, String message) {
        return new CommonResult<T>(code, message);
    }

    /**
     * 参数验证失败
     */
    public static <T> CommonResult<T> validDateFailed(String message) {
        return new CommonResult<>(ResultCode.VALIDATE.getCode(),message);
    }

    /**
     * 未登入返回结果
     */
    public static<T> CommonResult<T> unauthorized(T data) {
        return new CommonResult<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(),data);
    }

    /**
     * 未授权返回结果
     */
    public static<T> CommonResult<T>forbidden(T data) {
        return new CommonResult<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }





}
