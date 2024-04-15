package com.project.core.servlets;


import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/New")
@Slf4j
public class CFQueryBuilderServlet extends SlingAllMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {

        try {
            PrintWriter writer = response.getWriter();
            response.setContentType("text/html");
            ResourceResolver resourceResolver = request.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(searchCFData()), session);
            SearchResult result = query.getResult();
            var hits = result.getHits();
            for (Hit hit : hits) {
                Resource resource = hit.getResource();
                String path = resource.getPath();
                String mainPath = StringUtils.substringBeforeLast(path, "/jcr:content");
                ContentFragment contentFragment = resourceResolver.getResource(mainPath).adaptTo(ContentFragment.class);
                ContentElement firstname = contentFragment.getElement("firstname");

                String name = firstname.getContent();
                writer.print(name);
            }
        } catch (RepositoryException ex) {
            ex.printStackTrace();
        }
    }


    public Map<String, String> searchCFData() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content/dam/aem-project");
        queryMap.put("1_property", "cq:tags");
        queryMap.put("1_property.value", "workflow:");
        queryMap.put("p.limit", Long.toString(-1));
        return queryMap;
    }

}

