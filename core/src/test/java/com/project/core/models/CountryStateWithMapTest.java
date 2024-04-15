package com.project.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.service.component.annotations.Reference;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
class CountryStateWithMapTest {

    private static final String RESOURCE_TYPE = "aem-project/components/custom/countrystatewithmap";
    private final static String PAGE_PATH = "/content/aem-project/us/en/asd/jcr:content/root/container_1568815302/countrystatewithmap";
    private final static String NODE_RESOURCE_PATH = "/content/aem-project/us/en/asd/jcr:content/root/container_1568815302/countrystatewithmap/countryList/item0";
    private final AemContext context = new AemContext();
    CountryStateWithMap countryStateWithMap = new CountryStateWithMap();

    @Reference
    Resource resource;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(CountryStateWithMap.class);
        context.create().resource(PAGE_PATH + "/countryList/item0",
                "sling:resourceType", RESOURCE_TYPE,
                "countryName", "India"
        );
        context.create().resource(PAGE_PATH + "/countryList/item0/stateList/item0",
                "sling:resourceType", RESOURCE_TYPE,
                "stateName", "MH"
        );
        context.create().resource(PAGE_PATH + "/countryList/item0/stateList/item1",
                "sling:resourceType", RESOURCE_TYPE,
                "stateName", "UP"
        );
    }

    @Test
    void checkSizeOfCountryStateList() {
        Resource resource = context.currentResource(PAGE_PATH);
        if (resource != null) {
            countryStateWithMap = resource.adaptTo(CountryStateWithMap.class);
            Map<String, Set<String>> countryName = countryStateWithMap.getCountryStateListMap();
            int size = countryName.size();
            assertEquals(1, size);
        }
    }
    @Test
    void checkCountryName() {
        Resource resource = context.currentResource(PAGE_PATH);
        if (resource != null) {
            countryStateWithMap = resource.adaptTo(CountryStateWithMap.class);
            Map<String, Set<String>> countryName = countryStateWithMap.getCountryStateListMap();
            Set<Map.Entry<String, Set<String>>> entries = countryName.entrySet();
            Iterator<Map.Entry<String, Set<String>>> iterator = entries.stream().iterator();
            Map.Entry<String, Set<String>> next = iterator.next();
            String key = next.getKey();
            assertEquals("India",key);
        }
    }

    @Test
    void checkStateName() {
        Resource resource = context.currentResource(PAGE_PATH);
        if (resource != null) {
            countryStateWithMap = resource.adaptTo(CountryStateWithMap.class);
            Map<String, Set<String>> countryName = countryStateWithMap.getCountryStateListMap();
            Set<Map.Entry<String, Set<String>>> entries = countryName.entrySet();
            Iterator<Map.Entry<String, Set<String>>> iterator = entries.iterator();
            Map.Entry<String, Set<String>> next = iterator.next();
            Set<String> value = next.getValue();
            Iterator<String> stateIterator = value.iterator();
            String state = stateIterator.next();
            assertEquals("MH",state);
        }
    }

    @Test
    void checkCountryStateListNotNull() {
        Resource resource = context.currentResource(PAGE_PATH);
        countryStateWithMap = resource.adaptTo(CountryStateWithMap.class);
        Map<String, Set<String>> countryName = countryStateWithMap.getCountryStateListMap();
        assertNotNull(countryName);
    }

    @Test
    void checkStateNameNotNull() {
        Resource resource = context.currentResource(PAGE_PATH);
        Resource nextNodeResource = resource.getResourceResolver().getResource(NODE_RESOURCE_PATH);
        countryStateWithMap = resource.adaptTo(CountryStateWithMap.class);
        Set<String> stateName = countryStateWithMap.getStateList(nextNodeResource);
        assertNotNull(stateName);

    }

    @Test
    void checkStateSize() {
        Resource resource = context.currentResource(PAGE_PATH);
        Resource nextNodeResource = resource.getResourceResolver().getResource(NODE_RESOURCE_PATH);
        countryStateWithMap = resource.adaptTo(CountryStateWithMap.class);
        Set<String> stateName = countryStateWithMap.getStateList(nextNodeResource);
        int size = stateName.size();
        assertEquals(2, size);
    }
}