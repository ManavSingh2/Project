package com.project.core.models;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = SideNavigationListModel.RESOURCE_TYPE)
public class SideNavigationListModel {

    protected static final String RESOURCE_TYPE = "aem-project/components/custom/multifield";

    @Getter
    @ChildResource
    public List<SideNavigationSectionLinkListModel> sections;


}
