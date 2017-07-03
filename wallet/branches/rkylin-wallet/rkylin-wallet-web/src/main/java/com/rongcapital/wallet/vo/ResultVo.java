package com.rongcapital.wallet.vo;

public class ResultVo {

    private boolean success;
 
    private Integer code=0;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    
   

   

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString(){
        System.out.println(this.success);
        System.out.println("msg:  "+this.msg);
        return "";
     
    }
}
