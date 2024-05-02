package com.project.core.config;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@ObjectClassDefinition(name = "Update CF Config")
public @interface CFConfig {

    @AttributeDefinition(
            name = "CF Root Path",
            description = "Root path of Content Fragment",
            type = AttributeType.STRING
    )
    public String cfRootPath() default "/content/dam/aem-project/cfscheduler";

    @AttributeDefinition(
            name = "Cron Expression",
            description = "Cron Expression Description",
            type = AttributeType.STRING
    )
    public String cronTimer() default "0/20 * * * * ?";

    @AttributeDefinition(
            name = "Date",
            description = "Please enter date in this (yyyy-mm-dd)",
            type = AttributeType.STRING
    )
    public String date() default "2024-04-10";

}
