package com.project.core.jobs;

import com.project.core.service.CFServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Slf4j
@Component(service = JobConsumer.class,
        immediate = true,
        property = {JobConsumer.PROPERTY_TOPICS + "=UpdateCF/job"
        })
public class UpdateCFJobConsumer implements JobConsumer {


    @Reference
    private CFServiceImpl cfService;

    @Override
    public JobResult process(Job job) {
        try {
            log.info("\n >>>>>>>>>>>> Into Job Consumer");
            Object cfRoot = job.getProperty("cfRootPath");
            Object date = job.getProperty("date");
            String cfPath = cfRoot.toString();
            String cfDate = date.toString();
            if (StringUtils.isNotEmpty(cfPath) && StringUtils.isNotEmpty(cfDate)) {
                cfService.doSomething(cfDate, cfPath);
            }
            log.info("\n >>>>>>>>>>>> Out Job Consumer");
            return JobResult.OK;
        } catch (Exception e) {
            log.info("\n >>>>>>>>>>>> Exception in Job Consumer");
            return JobResult.FAILED;
        }
    }
}
