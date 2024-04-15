package com.project.core.models;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SideNavigationLinkLabelModel {


    @Getter
    @ValueMapValue
    private String label;

    @Getter
    @ValueMapValue
    private String url;

    @Getter
    @ValueMapValue
    private boolean openInNewTab;
}
