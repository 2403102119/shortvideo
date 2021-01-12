package com.lxkj.shortvideo.bean;


import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;


/**
 * Created ：李迪迦
 * on:2019/11/12 0012.
 * Describe :
 */

public class Jsontwobean implements IPickerViewData {

    /**
     * name : 北京市
     * id : 1
     * pid : 0
     * c : [{"name":"北京市","id":"35","pid":"1","d":[{"name":"东城区","id":"424","pid":"35"}]}]
     */

    private String name;
    private String id;
    private String pid;
    private List<CBean> c;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public List<CBean> getC() {
        return c;
    }

    public void setC(List<CBean> c) {
        this.c = c;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }



    public static class CBean {
        /**
         * name : 北京市
         * id : 35
         * pid : 1
         * d : [{"name":"东城区","id":"424","pid":"35"}]
         */

        private String name;
        private String id;
        private String pid;
        private List<DBean> d;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public List<DBean> getD() {
            return d;
        }

        public void setD(List<DBean> d) {
            this.d = d;
        }

        public static class DBean {
            /**
             * name : 东城区
             * id : 424
             * pid : 35
             */

            private String name;
            private String id;
            private String pid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }
        }
    }
}
