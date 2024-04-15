package com.project.core.schedulers;

import com.project.core.config.SchedulerConfiguration;
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
@Designate(ocd = SchedulerConfiguration.class)
@Component(service = Job.class, immediate = true)
public class CountryScheduler implements Job {

    String api = null;
    @Reference
    private JobManager jobManager;
    @Reference
    private Scheduler scheduler;

    @Activate
    protected void activate(final SchedulerConfiguration config) {
        ScheduleOptions scheduleOptions = scheduler.EXPR(config.timer());
        scheduler.schedule(this, scheduleOptions);
        api = config.api();
        log.info("\n ====> Completed Activate METHOD");
    }

    @Override
    public void execute(JobContext jobContext) {
        log.info("\n ====> Into Execute Method according to Cron");
        getSlingJobVerification(api);
    }

    public void getSlingJobVerification(String api) {
        try {
            Map<String, Object> jobProperties = new HashMap<>();
            jobProperties.put("API", api);
            jobManager.addJob("CountryState/job", jobProperties);
        } catch (Exception e) {
            log.error("\n Exception getSlingJobVerification Method in Scheduler : {}", e);
        }
    }
}