package com.github.appreciated.vortex_crud.uitest;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * JUnit 5 extension that captures a screenshot whenever a test fails and
 * ensures the WebDriver is closed after each test.
 */
public class ScreenshotExtension implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof BaseUITest base) {
            WebDriver driver = base.getDriver();
            if (driver instanceof TakesScreenshot shotProvider) {
                try {
                    Path directory = Paths.get("target", "screenshots");
                    Files.createDirectories(directory);
                    String safeName = context.getDisplayName().replaceAll("[()\\s]", "_");
                    Path screenshot = directory.resolve(safeName + ".png");
                    byte[] bytes = shotProvider.getScreenshotAs(OutputType.BYTES);
                    Files.write(screenshot, bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        Object instance = context.getRequiredTestInstance();
        if (instance instanceof BaseUITest base) {
            WebDriver driver = base.getDriver();
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
