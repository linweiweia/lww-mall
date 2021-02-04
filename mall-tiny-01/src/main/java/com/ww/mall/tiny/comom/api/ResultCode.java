package com.ww.mall.tiny.comom.api;


/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-11-26 09:43
 * @describe:   2.常用操作码
 *              enum枚举：1.先写构造方法包含成员变量（才能定义实例）  2.定义实例（必须卸载上面）
 */

public enum ResultCode implements IResultCode{

    //实例：enum必须先定义实例
    SUCCESS(200,"操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE(404, "参数校验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");

    //变量
    private long code;
    private String message;

    //先写构造方法
    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
