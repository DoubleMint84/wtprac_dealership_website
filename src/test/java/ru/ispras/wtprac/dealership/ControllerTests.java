package ru.ispras.wtprac.dealership;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerTests {

    @Test
    void IndexTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.get("http://localhost:8080/");

        WebElement catalogButton = driver.findElement(By.id("gotoCatalog"));
        catalogButton.click();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        assertEquals("Каталог", driver.getTitle());
    }
}
