package com.project.core.servlets;

import com.day.cq.search.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/ProductServlet")
@Slf4j
public class ProductServlet extends SlingAllMethodsServlet {
    @Reference
    private QueryBuilder queryBuilder;


    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {

        try {
            String sqlQueryForGender = "SELECT * FROM [nt:unstructured] AS node WHERE ISDESCENDANTNODE(node,'/content/dam/aem-project/products')AND CONTAINS([gender],'men')";
            Session session = request.getResourceResolver().adaptTo(Session.class);
            Query query = session.getWorkspace().getQueryManager().createQuery(sqlQueryForGender, Query.JCR_SQL2);
            QueryResult execute = query.execute();
            NodeIterator nodes = execute.getNodes();
            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                String productName = node.getProperty("productName").getValue().toString();
                String price = node.getProperty("price").getValue().toString();
                String image = node.getProperty("image").getValue().toString();
                String gender = node.getProperty("gender").getValue().toString();

                System.out.println("Product Name "+ productName + "Price "+ price + " Image "+ image + "Gender" + gender );
            }
        } catch (Exception e) {
            log.error("{ Error from Product Servlet }", e);
        }
    }
}
