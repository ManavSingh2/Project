package com.project.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/CFCreatedDate")
public class CFDatefetchServlet extends SlingAllMethodsServlet {

    private static final String JCR_CREATED = "jcr:created";
    private static final String CF_PATH = "/content/dam/aem-project/cfscheduler";
    private static final String TYPE = "dam:Asset";

    @Reference
    transient QueryBuilder queryBuilder;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {
        try {

            var resourceResolver = request.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(searchCFData()), session);
            SearchResult result = query.getResult();
            List<Hit> hits = result.getHits();
            for (Hit hit : hits) {
                Resource resource = hit.getResource();
                Node node = resource.adaptTo(Node.class);
                if (Objects.nonNull(node) && node.hasProperty(JCR_CREATED)) {
                    Property property = node.getProperty(JCR_CREATED);
                    if (Objects.nonNull(property)) {
                        String value = property.getValue().toString();
                        String cfDate = dateFormatterMethod(value);
                        boolean dateExistOrNot = checkCfDateExistInlast30Days("2024-04-26", cfDate);
                        if (dateExistOrNot) {
                            updateCFData(resource);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>Error in DoGet Method " + e);
        }
    }

    boolean checkCfDateExistInlast30Days(String givenDate, String cfDate) {
        LocalDate given = LocalDate.parse(givenDate, DateTimeFormatter.ISO_DATE);
        LocalDate contentFragment = LocalDate.parse(cfDate, DateTimeFormatter.ISO_DATE);
        LocalDate thirtyDaysBeforeGiven = given.minusDays(30);
        return !contentFragment.isBefore(thirtyDaysBeforeGiven) && !contentFragment.isAfter(given);
    }

    void updateCFData(Resource resource) throws RepositoryException, PersistenceException {
        String path = resource.getPath();
        String cfPath = path.replace(path, path + "/jcr:content/data/master");
        Resource cfResource = resource.getResourceResolver().getResource(cfPath);
        Node node = cfResource.adaptTo(Node.class);
        if (Objects.nonNull(node) && node.hasProperty("archived")) {
            node.setProperty("archived", true);
            cfResource.getResourceResolver().commit();
        }
    }


    String dateFormatterMethod(String inputDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = inputFormat.parse(inputDate);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        return outputFormat.format(date);
    }

    public Map<String, String> searchCFData() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", CF_PATH);
        queryMap.put("type", TYPE);
        queryMap.put("property", JCR_CREATED);
        queryMap.put("p.limit", "-1");
        return queryMap;
    }
}
