package com.example.administrator.essim.response;

import com.example.administrator.essim.response_re.IllustsBean;

import java.util.List;

public class IllustRankingResponse {
    private List<IllustsBean> illusts;
    private String next_url;

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
}