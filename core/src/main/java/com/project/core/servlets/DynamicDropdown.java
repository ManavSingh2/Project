package com.project.core.servlets;
import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.project.core.service.CountryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/countryList")
@Slf4j
public class DynamicDropdown extends SlingAllMethodsServlet {

    @Reference
    CountryServiceImpl countryService;


    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        final ResourceResolver resourceResolver = request.getResourceResolver();
        if (Objects.nonNull(countryService)) {
            List<String> listOfCountry = countryService.getListOfCountry();
            if (Objects.nonNull(listOfCountry)) {
                final List<Resource> countryResource = new ArrayList<>();
                for (String country : listOfCountry) {
                    ValueMapDecorator valueMapDecorator = new ValueMapDecorator(new HashMap<>());
                    valueMapDecorator.put("text", country);
                    valueMapDecorator.put("value", country);
                    ValueMapResource valueMapResource = new ValueMapResource(resourceResolver, "", "", valueMapDecorator);
                    countryResource.add(valueMapResource);
                }
                DataSource simpleDataSource = new SimpleDataSource(countryResource.iterator());
                request.setAttribute(DataSource.class.getName(), simpleDataSource);
            } else {
                log.error("\n ====> List of countries is null");
            }
        } else {
            log.error("\n ====> Country service is null");
        }
    }
}