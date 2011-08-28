package com.mscg.appstarter.server.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class GenericJob extends QuartzJobBean {

    protected Logger LOG;

    public GenericJob() {
        LOG = LoggerFactory.getLogger(this.getClass());
    }

}
