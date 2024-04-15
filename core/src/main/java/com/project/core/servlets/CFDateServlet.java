package com.project.core.servlets;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;


@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/Date")
public class CFDateServlet extends SlingAllMethodsServlet {
     String CF_PATH = "/content/dam/aem-project/date";

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {

        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = resourceResolver.getResource(CF_PATH);
        ContentFragment contentFragment = resource.adaptTo(ContentFragment.class);
        ContentElement date = contentFragment.getElement("date");
        String content = date.getContent();
        PrintWriter writer = response.getWriter();
        response.setContentType("text/plain");
        writer.print(content);

    }
}
