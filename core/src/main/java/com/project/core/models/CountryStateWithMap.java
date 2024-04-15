package com.project.core.models;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(adaptables = Resource.class,
        resourceType = CountryStateWithMap.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = "jackson", selector = "CountryStateMapData", extensions = "json")
public class CountryStateWithMap {
    protected static final String RESOURCE_TYPE = "aem-project/components/custom/countrystatewithmap";
    private Map<String, Set<String>> countryStateListMap = new HashMap<>();

    @ChildResource
    private Resource countryList;

    @PostConstruct
    public Map<String, Set<String>> getCountryStateListMap() {
        if (Objects.nonNull(countryList)) {
            final Iterator<Resource> resourceIterator = countryList.listChildren();
            while (resourceIterator.hasNext()) {
                Resource nextResource = resourceIterator.next();
                final String countryName = nextResource.getValueMap().get("countryName", String.class);
                if (StringUtils.isNotEmpty(countryName)) {
                    countryStateListMap.put(countryName, getStateList(nextResource));
                }
            }
        }
        return countryStateListMap;
    }

    public Set<String> getStateList(Resource countryNodeResource) {
        Set<String> stateNameList = new HashSet<>();
        Optional<Resource> nodeResource = Optional.ofNullable(countryNodeResource);
        if (nodeResource.isPresent()) {
            Optional.ofNullable(countryNodeResource.getChild("stateList"))
                    .map(Resource::getChildren)
                    .ifPresent(children -> children.forEach(child -> {
                        final String stateName = child.getValueMap().get("stateName", String.class);
                        if (StringUtils.isNotEmpty(stateName)) {
                            stateNameList.add(stateName);
                        }
                    }));
        }
        return stateNameList;
    }
}
