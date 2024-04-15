package com.project.core.models;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SideNavigationSectionLinkListModel {

    @Getter
    @ValueMapValue
    private String label;


    @Getter
    @ValueMapValue
    private String url;

    @Getter
    @ValueMapValue
    private boolean openInNewTab;

    @Getter
    @ChildResource
    private List<SideNavigationLinkLabelModel> links;
}

