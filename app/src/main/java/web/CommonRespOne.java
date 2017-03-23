package web;

import java.util.List;

/**
 * Created by qiansh on 2017/3/18.
 */

public class CommonRespOne<T> {

    private Integer count;
    private List<T> data;
    private String errMsg;

    private String msg;//仅仅用于提交二维码操作时的返回值
    /**
     * 目前用来传二维码单个验证之后，返回的单品编码
     */
    private String cinvcode;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getCinvcode() {
        return cinvcode;
    }

    public void setCinvcode(String cinvcode) {
        this.cinvcode = cinvcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
