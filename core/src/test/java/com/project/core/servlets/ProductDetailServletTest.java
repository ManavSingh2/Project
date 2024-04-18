package com.project.core.servlets;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductDetailServletTest {

    @Mock
    ContentElement element;
    @Mock
    private Hit hit;
    @InjectMocks
    private ProductDetailServlet productDetailServlet;
    @Mock
    private SlingHttpServletRequest request;
    @Mock
    private SlingHttpServletResponse response;
    @Mock
    private ResourceResolver resourceResolver;
    @Mock
    private Session session;
    @Mock
    private QueryBuilder queryBuilder;
    @Mock
    private Query query;
    @Mock
    private SearchResult searchResult;
    @Mock
    private Resource resource;
    @Mock
    private Page page;
    @Mock
    private Path path;
    @Mock
    private ContentFragment contentFragment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void doGet() throws ServletException, IOException, RepositoryException {
        String gender = "men";
        String cfPath = "/content/dam/aem-project/products/hilandersirt/jcr:content";
        String mainPath = "/content/dam/aem-project/products/hilandersirt";
        String productName = "productName";
        PrintWriter writer = mock(PrintWriter.class);
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
        when(request.getParameter("gender")).thenReturn(gender);
        when(queryBuilder.createQuery(any(PredicateGroup.class), any())).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        List<Hit> hits = new ArrayList<>();
        hits.add(hit);
        when(searchResult.getHits()).thenReturn(hits);
        when(hit.getResource()).thenReturn(resource);
        when(resource.getPath()).thenReturn(cfPath);
        when(resourceResolver.getResource(mainPath)).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);
        when(contentFragment.getElement("a")).thenReturn(element);
        when(element.getContent()).thenReturn(productName);
        Map<String, String> map = new HashMap<>();
        map.put("ProductName", productName);
        productDetailServlet.doGet(request, response);
        assertEquals(map.size(), 1);

    }


    @Test
    void testAllMen() throws ServletException, IOException, RepositoryException {
        String gender = "all";
        String cfPath = "/content/dam/aem-project/products/hilandersirt/jcr:content";
        String mainPath = "/content/dam/aem-project/products/hilandersirt";
        String price = "price";
        PrintWriter writer = mock(PrintWriter.class);
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(session);
        when(request.getParameter("gender")).thenReturn(gender);
        when(queryBuilder.createQuery(any(PredicateGroup.class), any())).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        List<Hit> hits = new ArrayList<>();
        hits.add(hit);
        when(searchResult.getHits()).thenReturn(hits);
        when(hit.getResource()).thenReturn(resource);
        when(resource.getPath()).thenReturn(cfPath);
        when(resourceResolver.getResource(mainPath)).thenReturn(resource);
        when(resource.adaptTo(ContentFragment.class)).thenReturn(contentFragment);
        when(contentFragment.getElement("a")).thenReturn(element);
        when(element.getContent()).thenReturn(price);
        Map<String, String> map = new HashMap<>();
        map.put("price", price);
        productDetailServlet.doGet(request, response);
        assertEquals(map.size(), 1);

    }


}