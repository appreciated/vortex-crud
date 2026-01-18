package com.github.appreciated.vortex_crud.demo.devplatform.service;

import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkdownService {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService() {
        this.parser = Parser.builder()
                .extensions(List.of(TablesExtension.create()))
                .build();
        this.renderer = HtmlRenderer.builder()
                .extensions(List.of(TablesExtension.create()))
                .escapeHtml(true) // Transform raw HTML in input to escaped text
                .build();
    }

    public String render(String markdown) {
        if (markdown == null) {
            return "";
        }
        return renderer.render(parser.parse(markdown));
    }
}
