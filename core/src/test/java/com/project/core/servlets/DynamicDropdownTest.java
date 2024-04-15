package com.project.core.servlets;

import com.project.core.service.CountryServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, AemContextExtension.class})
class DynamicDropdownTest {

    @Mock
    MockSlingHttpServletRequest request;
    @Mock
    MockSlingHttpServletResponse response;
    @Mock
    CountryServiceImpl countryService;
    @Mock
    private ResourceResolver resourceResolver;

    @InjectMocks
    private DynamicDropdown dynamicDropdown;


    @BeforeEach
    void setUp() {
        when(request.getResourceResolver()).thenReturn(resourceResolver);

    }

    @Test
    void testDoGetWithNonNullCountryServiceAndListOfCountries() throws IOException, ServletException {
        List<String> countries = Arrays.asList("Country1", "Country2");
        when(countryService.getListOfCountry()).thenReturn(countries);
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        dynamicDropdown.doGet(request, response);
        assertNotNull(dynamicDropdown.countryService.getListOfCountry());
        assertEquals(2, dynamicDropdown.countryService.getListOfCountry().size());

        ListIterator<String> stringListIterator = dynamicDropdown.countryService.getListOfCountry().listIterator();
        String next = stringListIterator.next();
        assertEquals("Country1", next);
        System.out.println(next);
    }

    @Test
    void testDoGetWithNullListOfCountries() throws IOException, ServletException {
        when(countryService.getListOfCountry()).thenReturn(null);
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        dynamicDropdown.doGet(request, response);
        verify(request, never()).setAttribute(any(), any());
    }

    @Test
    void testArrayListCountryFirstName() throws IOException, ServletException {
        List<String> countries = Arrays.asList("Country1", "Country2");
        when(countryService.getListOfCountry()).thenReturn(countries);
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        dynamicDropdown.doGet(request, response);
        ListIterator<String> stringListIterator = dynamicDropdown.countryService.getListOfCountry().listIterator();
        String next = stringListIterator.next();
        assertEquals("Country1", next);
        System.out.println(next);
    }
}
