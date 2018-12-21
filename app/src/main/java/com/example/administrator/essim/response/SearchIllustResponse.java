package com.example.administrator.essim.response;

import com.example.administrator.essim.response_re.IllustsBean;

import java.io.Serializable;
import java.util.List;

public class SearchIllustResponse implements Serializable {
    private List<IllustsBean> illusts;
    private String next_url;
    private int search_span_limit;

    public List<IllustsBean> getIllusts() {
        return this.illusts;
    }

    public void setIllusts(List<IllustsBean> paramList) {
        this.illusts = paramList;
    }

    public String getNext_url() {
        return this.next_url;
    }

    public void setNext_url(String paramString) {
        this.next_url = paramString;
    }

    public int getSearch_span_limit() {
        return this.search_span_limit;
    }

    public void setSearch_span_limit(int paramInt) {
        this.search_span_limit = paramInt;
    }
}