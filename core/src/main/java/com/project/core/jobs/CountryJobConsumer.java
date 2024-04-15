package com.project.core.jobs;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.project.core.service.CountryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.fluent.Request;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component(service = JobConsumer.class,
        immediate = true,
        property = {JobConsumer.PROPERTY_TOPICS + "=CountryState/job"
        })
public class CountryJobConsumer implements JobConsumer {

    @Reference
    CountryServiceImpl countryService;

    @Override
    public JobResult process(Job job) {
        log.info("\n ============> Into Job Consumer of CountryJobConsumer");
        try {
            Object api = job.getProperty("API");
            final String apiValue = api.toString();
            if (StringUtils.isNotEmpty(apiValue)) {
                final List<String> countryList = apiListData(apiValue);
                countryService.setListOfCountry(countryList);
            }
            return JobResult.OK;
        } catch (Exception e) {
            log.error("\n ============> Error in Job Consumer from CountryJobConsumer", e);
            return JobResult.FAILED;
        }
    }

    private List<String> apiListData(String apiValue) throws IOException {
        final String apiData = Request.Get(apiValue).execute().returnContent().toString();
        final Map<String, List<String>> countriesMap = new Gson().fromJson(
                apiData, new TypeToken<HashMap<String, List<String>>>() {
                }.getType()
        );
        final List<String> countryList = countriesMap.get("countries");
        return countryList;
    }

}
