package com.fmapp.test01.network.model;

import java.io.Serializable;
import java.util.List;

public class JXNavModel implements Serializable {

    /**
     * id : 1
     * pid : 0
     * name : PPT
     * child : [{"id":"2","pid":"1","name":"教育","child":[{"id":"5","pid":"2","name":"毕业答辩","child":[]},{"id":"6","pid":"2","name":"教学课件","child":[]},{"id":"7","pid":"2","name":"团课设计","child":[]},{"id":"8","pid":"2","name":"毕业纪念","child":[]},{"id":"9","pid":"2","name":"主题班会","child":[]},{"id":"10","pid":"2","name":"社团活动","child":[]},{"id":"11","pid":"2","name":"求职招聘","child":[]},{"id":"12","pid":"2","name":"作业汇报","child":[]}]},{"id":"3","pid":"1","name":"商业","child":[{"id":"13","pid":"3","name":"工作汇报","child":[]},{"id":"14","pid":"3","name":"产品发布","child":[]},{"id":"15","pid":"3","name":"计划总结","child":[]},{"id":"16","pid":"3","name":"企业团建","child":[]},{"id":"17","pid":"3","name":"入职培训","child":[]},{"id":"18","pid":"3","name":"节日庆典","child":[]},{"id":"19","pid":"3","name":"企业宣传","child":[]},{"id":"20","pid":"3","name":"作业汇报","child":[]}]},{"id":"4","pid":"1","name":"政府","child":[{"id":"21","pid":"4","name":"入党培训","child":[]},{"id":"22","pid":"4","name":"主题党课","child":[]},{"id":"23","pid":"4","name":"党建工作","child":[]},{"id":"24","pid":"4","name":"节日庆典","child":[]},{"id":"25","pid":"4","name":"党史宣传","child":[]}]}]
     */

    private String id;
    private String pid;
    private String name;
    private List<ChildBeanX> child;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildBeanX> getChild() {
        return child;
    }

    public void setChild(List<ChildBeanX> child) {
        this.child = child;
    }

    public static class ChildBeanX implements Serializable{
        /**
         * id : 2
         * pid : 1
         * name : 教育
         * child : [{"id":"5","pid":"2","name":"毕业答辩","child":[]},{"id":"6","pid":"2","name":"教学课件","child":[]},{"id":"7","pid":"2","name":"团课设计","child":[]},{"id":"8","pid":"2","name":"毕业纪念","child":[]},{"id":"9","pid":"2","name":"主题班会","child":[]},{"id":"10","pid":"2","name":"社团活动","child":[]},{"id":"11","pid":"2","name":"求职招聘","child":[]},{"id":"12","pid":"2","name":"作业汇报","child":[]}]
         */

        private String id;
        private String pid;
        private String name;
        private List<ChildBean> child;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean implements Serializable{
            /**
             * id : 5
             * pid : 2
             * name : 毕业答辩
             * child : []
             */

            private String id;
            private String pid;
            private String name;
            private List<ChildBean1> child;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ChildBean1> getChild() {
                return child;
            }

            public void setChild(List<ChildBean1> child) {
                this.child = child;
            }
        }
    }

    public static class ChildBean1 implements Serializable {
        /**
         * id : 5
         * pid : 2
         * name : 毕业答辩
         * child : []
         */

        private String id;
        private String pid;
        private String name;
        private List<?> child;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<?> getChild() {
            return child;
        }

        public void setChild(List<ChildBean1> child) {
            this.child = child;
        }
    }
}
