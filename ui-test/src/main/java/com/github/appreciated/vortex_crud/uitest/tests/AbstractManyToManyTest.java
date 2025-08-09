package com.github.appreciated.vortex_crud.uitest.tests;

import com.github.appreciated.vortex_crud.uitest.BaseUITest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for many-to-many relationship tests.
 * Implementation-specific tests should extend this class and provide the necessary configuration.
 */
public class AbstractManyToManyTest extends BaseUITest {

    private String getStudentsPath() {
        return "students";
    }

    private String getCoursesPath() {
        return "courses";
    }

    @Test
    void testStudentsListingVisible() {
        navigateTo(getStudentsPath());
        WebElement webElement = waitForAnyElementContainingText("Alex Johnson");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testStudentNavigationPossible() {
        navigateTo(getStudentsPath());
        waitForAnyElementContainingText("Alex Johnson").click();
        waitForUrlToBe(getStudentsPath() + "/1");
        
        // Verify student details
        waitForElementWithTagAndValue("vaadin-text-field", "Alex Johnson");
        waitForElementWithTagAndValue("vaadin-email-field", "alex@university.edu");
        
        // Verify courses are displayed
        waitForAnyElementContainingText("Database Systems");
        waitForAnyElementContainingText("Web Development");
        waitForAnyElementContainingText("Data Structures");
    }

    @Test
    void testCoursesListingVisible() {
        navigateTo(getCoursesPath());
        WebElement webElement = waitForAnyElementContainingText("Database Systems");
        assertEquals(webElement.getTagName(), "vaadin-grid-cell-content");
    }

    @Test
    void testCourseNavigationPossible() {
        navigateTo(getCoursesPath());
        waitForAnyElementContainingText("Database Systems").click();
        waitForUrlToBe(getCoursesPath() + "/1");
        
        // Verify course details
        waitForElementWithTagAndValue("vaadin-text-field", "Database Systems");
        waitForElementWithTagAndValue("vaadin-text-area", "Introduction to database design and SQL");
        waitForElementWithTagAndValue("vaadin-number-field", "3");
        
        // Verify students are displayed
        waitForAnyElementContainingText("Alex Johnson");
        waitForAnyElementContainingText("Maria Garcia");
        waitForAnyElementContainingText("Olivia Brown");
    }

    @Test
    void testStudentEnrollment() {
        navigateTo(getStudentsPath());
        waitForAnyElementContainingText("Alex Johnson").click();
        waitForUrlToBe(getStudentsPath() + "/1");
        
        // Click enroll button
        waitForAnyElementContainingText("Enroll in Course").click();
        
        // Select a course that Alex is not enrolled in (Machine Learning)
        waitForAnyElementContainingText("Machine Learning").click();
        
        // Save the enrollment
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the student view
        waitForUrlToBe(getStudentsPath() + "/1");
        
        // Verify the new course appears
        WebElement newCourse = waitForAnyElementContainingText("Machine Learning");
        assertTrue(newCourse.isDisplayed());
    }

    @Test
    void testCourseAddingStudent() {
        navigateTo(getCoursesPath());
        waitForAnyElementContainingText("Machine Learning").click();
        waitForUrlToBe(getCoursesPath() + "/4");
        
        // Click add student button
        waitForAnyElementContainingText("Add Student").click();
        
        // Select a student not enrolled in this course (Sam Taylor)
        waitForAnyElementContainingText("Sam Taylor").click();
        
        // Save the enrollment
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the course view
        waitForUrlToBe(getCoursesPath() + "/4");
        
        // Verify the new student appears
        WebElement newStudent = waitForAnyElementContainingText("Sam Taylor");
        assertTrue(newStudent.isDisplayed());
    }

    @Test
    void testStudentCreationWithCourses() {
        navigateTo(getStudentsPath() + "/new");
        
        // Fill student details
        WebElement nameField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        nameField.sendKeys("New Student");
        
        WebElement emailField = driver.findElement(By.cssSelector("vaadin-email-field"));
        emailField.sendKeys("newstudent@university.edu");
        
        // Save the student
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the students list
        waitForUrlToBe(getStudentsPath());
        
        // Navigate to the new student
        waitForAnyElementContainingText("New Student").click();
        
        // Enroll in a course
        waitForAnyElementContainingText("Enroll in Course").click();
        
        // Select a course
        waitForAnyElementContainingText("Database Systems").click();
        
        // Save the enrollment
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the student view
        // Note: We can't hardcode the ID here as it might vary
        // Instead, we'll check that we're back to a student URL
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains(getStudentsPath() + "/"));
        
        // Verify the course appears
        WebElement course = waitForAnyElementContainingText("Database Systems");
        assertTrue(course.isDisplayed());
    }

    @Test
    void testCourseCreationWithStudents() {
        navigateTo(getCoursesPath() + "/new");
        
        // Fill course details
        WebElement titleField = driver.findElement(By.cssSelector("vaadin-text-field[required]"));
        titleField.sendKeys("New Course");
        
        WebElement descriptionField = driver.findElement(By.cssSelector("vaadin-text-area"));
        descriptionField.sendKeys("This is a new test course");
        
        WebElement creditsField = driver.findElement(By.cssSelector("vaadin-number-field"));
        creditsField.sendKeys("4");
        
        // Save the course
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the courses list
        waitForUrlToBe(getCoursesPath());
        
        // Navigate to the new course
        waitForAnyElementContainingText("New Course").click();
        
        // Add a student
        waitForAnyElementContainingText("Add Student").click();
        
        // Select a student
        waitForAnyElementContainingText("Alex Johnson").click();
        
        // Save the enrollment
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the course view
        // Note: We can't hardcode the ID here as it might vary
        // Instead, we'll check that we're back to a course URL
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains(getCoursesPath() + "/"));
        
        // Verify the student appears
        WebElement student = waitForAnyElementContainingText("Alex Johnson");
        assertTrue(student.isDisplayed());
    }

    @Test
    void testEnrollmentGradeEditing() {
        navigateTo(getStudentsPath());
        waitForAnyElementContainingText("Alex Johnson").click();
        waitForUrlToBe(getStudentsPath() + "/1");
        
        // Click on a course enrollment
        waitForAnyElementContainingText("Database Systems").click();
        
        // Verify enrollment details
        waitForElementWithTagAndValue("vaadin-date-picker", "2023-09-01");
        
        // Edit the grade
        WebElement gradeField = driver.findElement(By.cssSelector("vaadin-text-field"));
        gradeField.clear();
        gradeField.sendKeys("A+");
        
        // Save the changes
        waitForAnyElementContainingText("Save").click();
        
        // Verify we're back at the student view
        waitForUrlToBe(getStudentsPath() + "/1");
        
        // Verify the grade was updated (this might require additional UI elements to display the grade)
        waitForAnyElementContainingText("A+");
    }
}