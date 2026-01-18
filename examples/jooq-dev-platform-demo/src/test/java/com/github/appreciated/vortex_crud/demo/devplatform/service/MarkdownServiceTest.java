package com.github.appreciated.vortex_crud.demo.devplatform.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MarkdownServiceTest {

    private final MarkdownService markdownService = new MarkdownService();

    @Test
    void testRenderBasic() {
        String markdown = "# Hello";
        String html = markdownService.render(markdown);
        assertTrue(html.contains("<h1>Hello</h1>"));
    }

    @Test
    void testRenderBold() {
        String markdown = "**bold**";
        String html = markdownService.render(markdown);
        assertTrue(html.contains("<strong>bold</strong>"));
    }

    @Test
    void testRenderTable() {
        String markdown = "| Head | \n | --- | \n | Cell |";
        String html = markdownService.render(markdown);
        assertTrue(html.contains("<table>"));
        assertTrue(html.contains("<thead>"));
        assertTrue(html.contains("<tbody>"));
        assertTrue(html.contains("<td>Cell</td>"));
    }

    @Test
    void testRenderHtmlEscaping() {
        String markdown = "<script>alert('xss')</script>";
        String html = markdownService.render(markdown);
        // It should escape the raw HTML
        assertTrue(html.contains("&lt;script&gt;alert('xss')&lt;/script&gt;"));
        assertTrue(!html.contains("<script>"));
    }
}
