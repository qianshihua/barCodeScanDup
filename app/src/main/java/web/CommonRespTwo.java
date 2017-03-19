package web;

import java.util.List;

/**
 * Created by qiansh on 2017/3/18.
 */

public class CommonRespTwo<T,S> {

    private Integer count;
    private List<T> data;
    private List<S> dataTwo;
    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<S> getDataTwo() {
        return dataTwo;
    }

    public void setDataTwo(List<S> dataTwo) {
        this.dataTwo = dataTwo;
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

}
