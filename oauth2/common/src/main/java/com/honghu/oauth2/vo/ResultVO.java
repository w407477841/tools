package com.honghu.oauth2.vo;

import lombok.Data;

@Data
public class ResultVO<T> {

    private Integer status;

    private String msg;

    private T data;

    public ResultVO() {
    }

    public ResultVO(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(Integer status, String msg) {
        this.status = status;
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
