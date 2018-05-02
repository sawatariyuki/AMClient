package com.gift.sawatariyuki.amclient.Utils.okHttp.exception;

/**
 * 描述: 自定义异常类，返回ecode,emsg到业务层
 */

public class OkHttpException extends Exception {
    private static final long serialVersionUID = 1L;

    private int ecode; //错误码
    private Object emsg; //错误消息

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }

}
