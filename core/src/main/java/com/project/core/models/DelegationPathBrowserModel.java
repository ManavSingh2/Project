package com.project.core.models;

import com.adobe.cq.wcm.core.components.models.Title;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Slf4j
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
        adapters = DelegationPathBrowserInterface.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class DelegationPathBrowserModel implements DelegationPathBrowserInterface {

    @Self
    @Delegate
    @Via(type = ResourceSuperType.class)
    private Title title;

    @ValueMapValue
    private String pathBrowser;

    @Override
    public String getPathBrowser() {
        return pathBrowser;
    }
}
