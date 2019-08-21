package com.honghu.oauth2.server.vo;

import lombok.Data;

@Data
public class ResultVO<T> {

    private Integer code;

    private String msg;

    private T data;

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static  ResultVO success(){
        return new ResultVO(200,"成功");
    }
    public static <T> ResultVO<T> success(T data){
        return new ResultVO(200,"成功",data);
    }
    public static  ResultVO error(String msg){
        return new ResultVO(400,msg);
    }
}
