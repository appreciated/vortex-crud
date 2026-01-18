package com.github.appreciated.vortex_crud.demo.devplatform;

import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.component.page.Inline;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.github.appreciated.vortex_crud.demo.devplatform.service.GitService;
import com.vaadin.flow.theme.aura.Aura;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@ColorScheme(ColorScheme.Value.DARK)
@Push
@StyleSheet("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism-tomorrow.min.css")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/prism.min.js")
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner init(GitService gitService) {
        return args -> {
            gitService.initRepository("demo-repo");
        };
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addInlineWithContents("html{--aura-app-layout-inset: 0.9rem;}", Inline.Wrapping.STYLESHEET);
    }
}
