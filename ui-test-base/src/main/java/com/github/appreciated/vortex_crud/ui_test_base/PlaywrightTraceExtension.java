package com.github.appreciated.vortex_crud.ui_test_base;

import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JUnit 5 extension that captures a Playwright trace whenever a test fails.
 */
public class PlaywrightTraceExtension implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof BaseUITest base) {
            try {
                Path directory = Paths.get("target", "traces");
                Files.createDirectories(directory);
                String safeName = context.getDisplayName().replaceAll("[()\\s]", "_");
                Path tracePath = directory.resolve(safeName + ".zip");

                if (base.getContext() != null) {
                    base.getContext().tracing().stop(new Tracing.StopOptions()
                            .setPath(tracePath));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof BaseUITest base) {
            try {
                if (base.getContext() != null) {
                    base.getContext().tracing().stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
