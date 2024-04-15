package com.project.core.servlets;

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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Session;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaginationServletTest {

    QueryBuilder queryBuilder = mock(QueryBuilder.class);
    @Mock
    Resource resource;
    @Mock
    private SlingHttpServletRequest request;
    @Mock
    private SlingHttpServletResponse response;
    @Mock
    private Writer writer;
    @Mock
    private Query query;
    @Mock
    private SearchResult searchResult;
    @Mock
    private ResourceResolver resourceResolver;
    @Mock
    private Page page;
    @Mock
    private Hit hit;
    @InjectMocks
    private PaginationServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new PaginationServlet();
    }

    @Test
    void testDoPost() throws Exception {
        String searchText = "sample";
        PrintWriter writer = mock(PrintWriter.class);
        when(request.getParameter("searchText")).thenReturn(searchText);
        when(request.getResourceResolver()).thenReturn(resourceResolver);
        when(resourceResolver.adaptTo(Session.class)).thenReturn(mock(Session.class));
        when(resourceResolver.adaptTo(QueryBuilder.class)).thenReturn(queryBuilder);
        when(queryBuilder.createQuery(any(PredicateGroup.class), any())).thenReturn(query);
        when(query.getResult()).thenReturn(searchResult);
        List<Hit> hits = new ArrayList<>();
        hits.add(hit);
        lenient().when(searchResult.getHits()).thenReturn(hits);
        when(hit.getResource()).thenReturn(resource);
        when(hit.getResource().adaptTo(Page.class)).thenReturn(page);
        when(response.getWriter()).thenReturn(writer);
        servlet.doPost(request, response);
        int listSize = hits.size();
        assertNotNull(writer);
        assertNotNull(listSize);
        assertEquals(1, listSize);
    }

    @Test
    void testCreateTextSearchQuery() {
        PaginationServlet servlet = new PaginationServlet();
        final String searchText = "test";
        Map<String, String> queryMap = servlet.createTextSearchQuery(searchText);
        assertEquals("/content/catalogs", queryMap.get("path"));
        assertEquals("cq:Page", queryMap.get("type"));
        assertEquals(searchText, queryMap.get("fulltext"));
        assertEquals("-1", queryMap.get("p.limit"));
    }
}