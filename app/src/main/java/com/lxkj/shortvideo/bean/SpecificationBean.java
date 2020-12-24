package com.lxkj.shortvideo.bean;

import java.util.List;

/**
 * Time:2020/9/4
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class SpecificationBean {

    /**
     * name :
     * attributes : [{"name":""}]
     */

    private String name;
    private List<AttributesBean> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AttributesBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributesBean> attributes) {
        this.attributes = attributes;
    }

    public static class AttributesBean {
        /**
         * name :
         */

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
