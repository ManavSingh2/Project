package com.project.core.models;

import com.day.cq.wcm.api.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

    @SlingObject
    Resource resource;

    @Getter
    @ValueMapValue(name = "imagePath")
    private String imagePath;

    @Getter
    @ValueMapValue
    private String altText;

    @ChildResource
    private Resource navLinks;

    @PostConstruct
    public List<Page> getNavList() {
        List<Page> navlist = new ArrayList<>();
        Iterable<Resource> children = navLinks.getChildren();
        Iterator<Resource> iterator = children.iterator();
        while (iterator.hasNext()) {
            Resource next = iterator.next();
            String pagePathLink = next.getValueMap().get("pagePathLink", String.class);
            Resource newresource = resource.getResourceResolver().getResource(pagePathLink);
            Page page = newresource.adaptTo(Page.class);
            navlist.add(page);
        }
        return navlist;
    }
}
