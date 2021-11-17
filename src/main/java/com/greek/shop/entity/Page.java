package com.greek.shop.entity;

import java.util.List;

/**
 * 数据分页使用的对象
 *
 * @author Zhaofeng Zhou
 * @date 2021/11/17/017 17:46
 */
public class Page<T> {
    int pageNum;
    int pageSize;
    int totalPage;
    List<T> data;

    public Page() {
    }

    public static <T> Page<T> of(int pageNum, int pageSize) {
        return null;
    }


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
