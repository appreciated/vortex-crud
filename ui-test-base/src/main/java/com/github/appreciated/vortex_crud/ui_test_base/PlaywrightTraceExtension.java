package com.github.appreciated.vortex_crud.ui_test_base;

import com.microsoft.playwright.Tracing;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JUnit 5 extension that captures a Playwright trace whenever a test fails,
 * and ensures the BrowserContext is closed.
 */
public class PlaywrightTraceExtension implements TestWatcher {

    private final Logger logger = LoggerFactory.getLogger(PlaywrightTraceExtension.class);

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof BaseUITest base) {
            try {
                String className = context.getRequiredTestClass().getSimpleName();
                String methodName = context.getDisplayName().replaceAll("[()\\s]", "_");
                logger.error("Test failed: " + className + "#" + methodName);
                if (base.getContext() != null) {
                    Path directory = Paths.get("target", "traces");
                    Files.createDirectories(directory);
                    String safeName = className + "#" + methodName;
                    Path tracePath = directory.resolve(safeName + ".zip");

                    try {
                        base.getContext().tracing().stop(new Tracing.StopOptions()
                                .setPath(tracePath));
                        logger.error("Trace saved to " + tracePath.toAbsolutePath());
                    } catch (Exception e) {
                        logger.error("Failed to stop tracing/save trace: ", e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeContext(base);
            }

            if (base.browser != null) {
                base.browser.close();
                base.browser = null;
            }

            if (base.playwright != null) {
                base.playwright.close();
                base.playwright = null;
            }
        }

    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof BaseUITest base) {
            try {
                if (base.getContext() != null) {
                    try {
                        base.getContext().tracing().stop();
                    } catch (Exception e) {
                        // ignore
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeContext(base);
            }

            // Clean up browser and playwright instances for successful tests too
            if (base.browser != null) {
                base.browser.close();
                base.browser = null;
            }

            if (base.playwright != null) {
                base.playwright.close();
                base.playwright = null;
            }
        }
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof BaseUITest base) {
            closeContext(base);

            // Clean up browser and playwright instances for aborted tests too
            if (base.browser != null) {
                base.browser.close();
                base.browser = null;
            }

            if (base.playwright != null) {
                base.playwright.close();
                base.playwright = null;
            }
        }
    }

    private void closeContext(BaseUITest base) {
        try {
            if (base.getContext() != null) {
                base.getContext().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
