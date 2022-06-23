package com.example.boost.dto;

import java.util.List;

public class CommentList {

    private List<Comment> data;

    private Integer total;

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
