package com.project.core.service;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.project.core.config.CFConfig;
import com.project.core.utils.ResolverUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component(service = CFServiceImpl.class, immediate = true)
@Designate(ocd = CFConfig.class)
public class CFServiceImpl {


    private static final String JCR_CREATED = "jcr:created";
    private static final String TYPE = "dam:Asset";
    @Reference
    QueryBuilder queryBuilder;
    @Reference
    ResourceResolverFactory resourceResolverFactory;


    public void doSomething(final String cfDate,final String cfPath) throws RepositoryException {
        log.info(">>>>>>>>>>>>>>>Into CfService");

        try {
            ResourceResolver resourceResolver = ResolverUtil.newResolver(resourceResolverFactory);
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(searchCFData(cfPath)), session);
            SearchResult result = query.getResult();
            List<Hit> hits = result.getHits();
            for (Hit hit : hits) {
                Resource resource = hit.getResource();
                Node node = resource.adaptTo(Node.class);
                if (Objects.nonNull(node) && node.hasProperty(JCR_CREATED)) {
                    Property property = node.getProperty(JCR_CREATED);
                    if (Objects.nonNull(property)) {
                        String value = property.getValue().toString();
                        String currentCFDate = dateFormatterMethod(value);
                        boolean dateExistOrNot = checkCfDateExistInlast30Days(cfDate, currentCFDate);
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

    public Map<String, String> searchCFData(String cfPath) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", cfPath);
        queryMap.put("type", TYPE);
        queryMap.put("property", JCR_CREATED);
        queryMap.put("p.limit", "-1");
        return queryMap;
    }
}
