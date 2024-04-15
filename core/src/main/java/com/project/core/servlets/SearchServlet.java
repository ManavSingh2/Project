package com.project.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/SearchServlet")
public class SearchServlet extends SlingAllMethodsServlet {


    @Reference
    private QueryBuilder queryBuilder;


    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
        JSONObject mainJSONObject = new JSONObject();
        try {
            ResourceResolver resourceResolver = req.getResourceResolver();
            PrintWriter writer = resp.getWriter();
            resp.setContentType("text/html");
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = queryBuilder.createQuery(PredicateGroup.create(createTextSearchQuery("women")), session);
            SearchResult result = query.getResult();
            List<Hit> hits = result.getHits();
            JSONArray jsonArray = new JSONArray();
            for (Hit hit : hits) {
                Page page = hit.getResource().adaptTo(Page.class);
                JSONObject resultObject = new JSONObject();
                resultObject.put("title", page.getTitle());
                resultObject.put("path", page.getPath());
                jsonArray.put(resultObject);
            }
            mainJSONObject.put("result", jsonArray);
            writer.print(mainJSONObject);
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    public Map<String, String> createTextSearchQuery(String searchText) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content");
        queryMap.put("type", "cq:Page");
        queryMap.put("fulltext", searchText);
        queryMap.put("p.limit", Long.toString(10));
        return queryMap;
    }
}

