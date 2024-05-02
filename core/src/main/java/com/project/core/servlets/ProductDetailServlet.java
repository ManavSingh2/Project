package com.project.core.servlets;

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
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/productDetailServlet")
@Slf4j
public class ProductDetailServlet extends SlingAllMethodsServlet {

    @Reference
    QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try {
            final String GENDER = request.getParameter("gender");
            // To Get The Products Details
            List<Map<String, String>> listOfMap = getProducts(GENDER, request.getResourceResolver());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("List", listOfMap);
            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            writer.write(jsonObject.toString());
            writer.flush();
        } catch (Exception e) {
            log.error("Error in ProductDetailServlet", e);
        }
    }

    private List<Map<String, String>> getProducts(String gender, ResourceResolver resourceResolver) {
        List<Map<String, String>> listOfMap = new ArrayList<>();

        try {
            Session session = resourceResolver.adaptTo(Session.class);
            Query query = Optional.ofNullable(queryBuilder).map(qb -> queryBuilder.createQuery(PredicateGroup.create(getPredicates(gender)), session)).orElse(null);
            SearchResult result = query.getResult();
            List<Hit> hits = result.getHits();
            for (Hit hit : hits) {
                try {
                    String path = hit.getResource().getPath();
                    String mainPath = StringUtils.substringBeforeLast(path, "/jcr:content");
                    Resource resource = resourceResolver.getResource(mainPath);
                    ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);
                    getProductNamePriceAndImage(contentFragment, listOfMap);
                } catch (RepositoryException e) {
                    log.error("Error from retrieving products Data from CF", e);
                }
            }
        } catch (Exception e) {
            log.error("Error while retrieving products Data", e);
        }
        return listOfMap;
    }

    private void getProductNamePriceAndImage(ContentFragment contentFragment, List<Map<String, String>> listOfMap) {

        String productName = Optional.ofNullable(contentFragment)
                .map(cf -> cf.getElement("productName").getContent())
                .orElse(null);


        String price = Optional.ofNullable(contentFragment)
                .map(cf -> cf.getElement("price").getContent())
                .orElse(null);

        String image = Optional.ofNullable(contentFragment)
                .map(cf -> cf.getElement("image").getContent())
                .orElse(null);

        Map<String, String> map = new HashMap<>();
        map.put("ProductName", productName);
        map.put("Price", price);
        map.put("Image", image);
        listOfMap.add(map);
    }


    private final Map<String, String> getPredicates(String gender) {
        final String PROPERTY = "property";
        Map<String, String> predicates = new HashMap<>();
        predicates.put("path", "/content/dam/aem-project/products");
        predicates.put(PROPERTY, "gender");
        predicates.put("p.limit", "-1");
        if (gender.equals("all")) {
            predicates.put("property.1_value", "men");
            predicates.put("property.2_value", "women");
            predicates.put("property.operation", "OR");
        } else {
            predicates.put("property.value", gender);
        }
        return predicates;
    }
}
