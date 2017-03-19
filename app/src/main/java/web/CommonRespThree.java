package web;

import java.util.List;

/**
 * Created by qiansh on 2017/3/18.
 */

public class CommonRespThree<T,S,P> {

    private Integer count;
    private List<T> data;//rdrecord
    private List<S> dataTwo;//rdrecords。可能出入库、可能调拨单明细。对象相同


    private List<P> dataThree;//Transvouch调拨单对象
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

    public List<P> getDataThree() {
        return dataThree;
    }

    public void setDataThree(List<P> dataThree) {
        this.dataThree = dataThree;
    }
}
