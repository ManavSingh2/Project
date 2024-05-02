package com.project.core.schedulers;


import com.project.core.config.CFConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.commons.scheduler.Job;
import org.apache.sling.commons.scheduler.JobContext;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Designate(ocd = CFConfig.class)
@Component(service = Job.class, immediate = true)
public class UpdateCFScheduler implements Job {

    String date = "";
    String cfRootPath="";
    @Reference
    private JobManager jobManager;
    @Reference
    private Scheduler scheduler;

    @Activate
    protected void activate(final CFConfig config) {
        log.info("\n ====> Into Activate Method");
        ScheduleOptions scheduleOptions = scheduler.EXPR(config.cronTimer());
        scheduler.schedule(this, scheduleOptions);
        log.info("\n ====> Out of Activate Method");
        cfRootPath = config.cfRootPath();
        date = config.date();
    }


    @Override
    public void execute(JobContext jobContext) {
        log.info("\n ====> Into Execute Method");
        try {
            Map<String, Object> jobProperties = new HashMap<>();
            jobProperties.put("cfRootPath", cfRootPath);
            jobProperties.put("date", date);
            jobManager.addJob("UpdateCF/job", jobProperties);

        } catch (Exception e) {
            log.error("\n Error in scheduler Execute Method");
        }
    }
}