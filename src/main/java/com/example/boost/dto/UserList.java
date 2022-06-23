package com.example.boost.dto;

import java.util.List;

public class UserList {

    private List<User> data;

    private Integer total;

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
