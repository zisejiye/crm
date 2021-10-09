package com.bipowernode.crm.vo;

import java.util.List;

public class PageinationVo<T> {
    private int total;
    private List<T> dataList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "PageinationVo{" +
                "total=" + total +
                ", dataList=" + dataList +
                '}';
    }
}
