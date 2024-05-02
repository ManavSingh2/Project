package com.project.core.listeners;

import com.project.core.utils.ResolverUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component(service = ResourceChangeListener.class,
        immediate = true,
        property = {ResourceChangeListener.PATHS + "=/content/dam/aem-project/cfscheduler",
                ResourceChangeListener.CHANGES + "=ADDED"})
public class EventListenerAndHaldler implements ResourceChangeListener {


    @Reference
    ResourceResolverFactory resourceResolverFactory;


    @Override
    public void onChange(List<ResourceChange> list) {

        list.stream().forEach(change -> {
            log.info(">>>>>>>>>>>>>>>>>>>Event Listner on change" + change.getPath());
            String path = change.getPath();
            String cfElementPath = path.replace(path, path + "/jcr:content/data/master");

            try {
                ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
                Resource resource = resourceResolver.getResource(cfElementPath);
                Node node = resource.adaptTo(Node.class);
                if (Objects.nonNull(node)) {
                    String currentDate = getCurrentDate();
                    node.setProperty("createdDate", currentDate);
                    resourceResolver.commit();
                }
            } catch (Exception e) {
                log.error(">>>>>>>>Error from  Event Listner Class " + e);
            }
        });
    }

    String getCurrentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        String formattedDate = formattedDateTime.split(" ")[0];
        return formattedDate;
    }
}
