package org.starcoin.starswap.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "页码数据")
public class PageResult<T> {

    @ApiModelProperty("总页码")
    private int totalPage = 0;

    @ApiModelProperty("当前页")
    private int currentPage = 0;

    @ApiModelProperty("总条数")
    private long totalElements = 0;

    @ApiModelProperty("列表数据")
    private List<T> list = null;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
