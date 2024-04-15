package com.project.core.models;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = NewCountryListModel.RESOURCE_TYPE)
@Exporter(name = "jackson", extensions = "json")
public class NewCountryListModel {

    protected static final String RESOURCE_TYPE = "aem-project/components/custom/countrystate";

    @Getter
    @ChildResource
    public List<NewStateListModel> countryList;

}
