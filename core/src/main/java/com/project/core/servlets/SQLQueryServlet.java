package com.project.core.servlets;

import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/SQLQuery")
@Slf4j
public class SQLQueryServlet extends SlingAllMethodsServlet {

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {

        String query = "SELECT * FROM [nt:unstructured] AS node WHERE ISDESCENDANTNODE(node,'/content/aem-project') AND node.[redirectURL] IS NOT NULL";
        ResourceResolver resourceResolver = request.getResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        try {

            Query result = session.getWorkspace().getQueryManager().createQuery(query, Query.JCR_SQL2);
            QueryResult execute = result.execute();
            NodeIterator nodes = execute.getNodes();
            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                System.out.println(node);
                if (node.hasProperty("redirectURL")) {
                    Property property = node.setProperty("redirectURL", "demo");
                    System.out.println(property);
Hiii
                }
            }
            session.save();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }
}
