package com.project.core.servlets;

import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes = "aem-project/components/custom/country-state-dropdown",
        methods = HttpConstants.METHOD_GET,
        selectors = {"country"},
        extensions = {"json"}
)
public class CountryServlet extends SlingAllMethodsServlet {

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {
        JSONObject mainJSONObject = new JSONObject();
        List<String> stateList = new ArrayList<>();

        try {
            String country = request.getParameter("country");
            Resource resource = request.getResource();
            Node node = resource.adaptTo(Node.class);
            populateStatesForCountry(node, country, stateList);
            PrintWriter writer = response.getWriter();
            mainJSONObject.put("result", stateList);
            writer.print(mainJSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateStatesForCountry(Node node, String country, List<String> stateList) {
        try {
            String COUNTRY_NAME = "countryName";
            Node countryNode = node.getNode("country");
            NodeIterator nodes = countryNode.getNodes();

            while (nodes.hasNext()) {
                Node itemNode = nodes.nextNode();
                if (itemNode.hasProperty(COUNTRY_NAME)) {
                    String countryName = itemNode.getProperty(COUNTRY_NAME).getValue().getString();
                    if (countryName.equals(country)) {
                        NodeIterator stateNode = itemNode.getNodes();
                        Node node1 = stateNode.nextNode();
                        NodeIterator stateNodes = node1.getNodes();
                        while (stateNodes.hasNext()) {
                            Node node2 = stateNodes.nextNode();
                            String stateName = node2.getProperty("stateName").getValue().getString();
                            stateList.add(stateName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("{Error from Country Servlet}" + e);
        }
    }

}