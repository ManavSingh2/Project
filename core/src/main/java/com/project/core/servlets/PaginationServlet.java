package com.project.core.servlets;

import com.day.cq.commons.Externalizer;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/pagination")
public class PaginationServlet extends SlingAllMethodsServlet {

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) {
        try {
            final String searchText = req.getParameter("searchText");
            if (StringUtils.isNotEmpty(searchText)) {
                JSONObject jsonObj = performQueryBuilderSearch(req, searchText);
                writeResponse(resp, jsonObj);
            }
        } catch (IOException e) {
            log.error("Error in Pagination Servlet", e);
        }
    }

    private JSONObject performQueryBuilderSearch(SlingHttpServletRequest req, final String searchText) {
        JSONObject jsonObj = new JSONObject();
        try {
            ResourceResolver resourceResolver = req.getResourceResolver();
            final Session session = resourceResolver.adaptTo(Session.class);
            final QueryBuilder queryBuilder = req.getResourceResolver().adaptTo(QueryBuilder.class);
            final PredicateGroup predicates = PredicateGroup.create(createTextSearchQuery(searchText));
            final Query query = queryBuilder.createQuery(predicates, session);
            final SearchResult result = query.getResult();
            List<Hit> hits = result.getHits();
            JSONArray jsonArray = getDetailsOfPages(hits, resourceResolver);
            jsonObj.put("data", jsonArray);
        } catch (Exception e) {
            log.error("Error from Pagination Servlet {}", e);
        }
        return jsonObj;
    }

    private JSONArray getDetailsOfPages(List<Hit> hits, ResourceResolver resourceResolver) throws RepositoryException, JSONException {
        JSONArray jsonArray = new JSONArray();
        String externalizePath = null;
        for (Hit hit : hits) {
            final Page page = hit.getResource().adaptTo(Page.class);
            if (Objects.nonNull(page)) {
                JSONObject resultObject = new JSONObject();
                resultObject.put("title", page.getTitle());
                if (Objects.nonNull(resourceResolver)) {
                    Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
                    String path = page.getPath();
                    externalizePath = externalizer.authorLink(resourceResolver, path) + ".html";
                }
                resultObject.put("path", externalizePath);
                jsonArray.put(resultObject);
            }
        }
        return jsonArray;
    }

    public final Map<String, String> createTextSearchQuery(String searchText) {
        final Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content/catalogs");
        queryMap.put("type", "cq:Page");
        queryMap.put("fulltext", searchText);
        queryMap.put("p.limit", Long.toString(-1));
        return queryMap;
    }

    private void writeResponse(SlingHttpServletResponse resp, JSONObject jsonObj) throws IOException {
        final PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        writer.print(jsonObj);
    }
}
