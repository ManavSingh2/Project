//package com.project.core.models;
//
//import io.wcm.testing.mock.aem.junit5.AemContext;
//import io.wcm.testing.mock.aem.junit5.AemContextExtension;
//import org.apache.sling.api.resource.Resource;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.StringJoiner;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(AemContextExtension.class)
//class NewCountryListModelTest {
//
//    AemContext aemContext = new AemContext();
//
//    NewCountryListModel newCountryListModel;
//    NewStateListModel newStateListModel;
//
//    @BeforeEach
//    void setUp() {
//
//        aemContext.addModelsForClasses(NewCountryListModel.class, NewStateListModel.class, NewStateModel.class);
//        aemContext.create().resource("/content/sample",
//                "countryName", "India",
//                "stateList", new String[]{"state1", "state2"});
//        List<String> states = Arrays.asList("MP", "Maharashtra");
//        for (String state : states) {
//            aemContext.create().resource("/content/sample/resource_" + state, "stateName", state);
//        }
//    }
//
//
//    @Test
//    public void testCountryName() {
//        Resource testResource = aemContext.resourceResolver().getResource("/content/sample");
//        NewStateListModel testStateModel = testResource.adaptTo(NewStateListModel.class);
//        System.out.println("Country: " + testStateModel.getCountryName());
//        assertNotNull(testStateModel);
//        assertEquals("India", testStateModel.getCountryName());
//    }
//
////    @Test
////    public void testCountryListNotNull() {
////        aemContext.create().resource("/content/testResource",
////                "countryList", Collections.emptyList());
////
////        // Initialize the NewCountryListModel instance
////       newCountryListModel = aemContext.resourceResolver().getResource("/content/testResource")
////                .adaptTo(NewCountryListModel.class);
////        assertNotNull(newCountryListModel.getCountryList());
////
////    }
//
//    @Test
//    void testResourceType() {
//        NewCountryListModel countryListModel = new NewCountryListModel();
//        assertEquals("aem-project/components/custom/countrystate", countryListModel.RESOURCE_TYPE);
//    }
//
//    @Test
//    public void testStateNameSize() {
//
//        List<String> states = Arrays.asList("MP", "Maharashtra");
//        StringJoiner addstates = new StringJoiner(", ");
//
//        for (String state : states) {
//            Resource testCountryStateResource = aemContext.resourceResolver().getResource("/content/sample/resource_" + state);
//            NewStateModel testCountryStateModel = testCountryStateResource.adaptTo(NewStateModel.class);
//            addstates.add(testCountryStateModel.getStateName());
//        }
//        assertEquals(2, states.size());
//    }
//
//
////    @Test
////    public void testStateListNotNull() {
////        aemContext.create().resource("/content/testResource",
////                "stateList", Collections.emptyList());
////
////        // Initialize the NewCountryListModel instance
////        newStateListModel = aemContext.resourceResolver().getResource("/content/testResource")
////                .adaptTo(NewStateListModel.class);
////        assertNotNull(newStateListModel.getStateList());
////
////    }
//}