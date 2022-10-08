package com.jinpei.re.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * API响应结果基类，所有的请求响应结果都是此类
 *
 * @author Mingo.Liu
 * @date 2022-06-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {
    /**
     * 结果码
     */
    private String code;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 错误内容
     */
    private Object errorData;

    /**
     * 内容
     */
    private T data;

    /**
     * 操作成功
     */
    public static final String SUCCESSFUL = "E000000";

    /**
     * 操作失败
     */
    public static final String FAILED = "E000001";

    /**
     * 构造无返回数据的成功响应结果
     *
     * @param <T> 泛型
     * @return 无返回数据的成功响应结果
     */
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(SUCCESSFUL, null, null, null);
    }

    /**
     * 构造带返回数据的成功响应结果
     *
     * @param <T> 泛型
     * @return 带返回数据的成功响应结果
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(SUCCESSFUL, null, null, data);
    }

    /**
     * 构造通用失败响应结果
     *
     * @param <T> 泛型
     * @return 通用失败响应结果
     */
    public static <T> BaseResponse<T> error() {
        return new BaseResponse<>(FAILED, null, null, null);
    }

    /**
     * 构造通用失败响应结果
     *
     * @param message 错误信息
     * @param <T>     泛型
     * @return 通用失败响应结果
     */
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(FAILED, message, null, null);
    }
}
