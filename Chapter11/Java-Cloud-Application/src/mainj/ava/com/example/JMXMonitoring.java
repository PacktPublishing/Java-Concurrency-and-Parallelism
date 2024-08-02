package com.example;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class JMXMonitoring {

    public interface CustomMBean {
        int getMetric();

        void setMetric(int metric);
    }

    public static class Custom implements CustomMBean {
        private int metric = 0;

        @Override
        public int getMetric() {
            return metric;
        }

        @Override
        public void setMetric(int metric) {
            this.metric = metric;
        }
    }

    public static CustomMBean createAndRegisterMBean(MBeanServer mbs) throws Exception {
        CustomMBean customMBean = new Custom();
        ObjectName name = new ObjectName("com.example:type=CustomMBean");
        mbs.registerMBean(customMBean, name);
        return customMBean;
    }
}
