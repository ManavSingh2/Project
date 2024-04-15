package com.project.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Country Scheduler"
)
public @interface SchedulerConfiguration {

    @AttributeDefinition(
            name = "Cron Expression",
            description = "Cron expression used by the scheduler",
            type = AttributeType.STRING)
    public String timer() default "0/20 * * * * ?"; // runs every 10 seconds

    @AttributeDefinition(
            name = "API Path",
            description = "API Path for Country List",
            type = AttributeType.STRING)
    public String api() default "https://mocki.io/v1/7af6acd8-8d5a-4ee6-8da6-39db5d22862e"; // API Path
}
