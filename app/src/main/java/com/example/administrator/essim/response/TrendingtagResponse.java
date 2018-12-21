package com.example.administrator.essim.response;

import com.example.administrator.essim.response_re.IllustsBean;

import java.util.List;

public class TrendingtagResponse {
    private List<TrendTagsBean> trend_tags;

    public List<TrendTagsBean> getTrend_tags() {
        return this.trend_tags;
    }

    public void setTrend_tags(List<TrendTagsBean> paramList) {
        this.trend_tags = paramList;
    }

    public static class TrendTagsBean {
        private IllustsBean illust;
        private String tag;

        public IllustsBean getIllust() {
            return this.illust;
        }

        public void setIllust(IllustsBean paramIllustsBean) {
            this.illust = paramIllustsBean;
        }

        public String getTag() {
            return this.tag;
        }

        public void setTag(String paramString) {
            this.tag = paramString;
        }
    }
}