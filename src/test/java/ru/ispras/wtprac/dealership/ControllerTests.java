package ru.ispras.wtprac.dealership;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ControllerTests {

    @Test
    void IndexTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.get("http://localhost:8080/");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        assertEquals("Домашняя страница", driver.getTitle());

        driver.quit();
    }

    @Test
    void catalogTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.get("http://localhost:8080/");

        WebElement catalogButton = driver.findElement(By.id("gotoCatalog"));
        catalogButton.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        assertEquals("Каталог", driver.getTitle());

        driver.quit();
    }

    @Test
    void loginTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.get("http://localhost:8080/");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        WebElement loginButton = driver.findElement(By.id("headerLoginRegister"));

        assertNotNull(loginButton);
        loginButton.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        assertEquals("Вход и регистрация", driver.getTitle());

        driver.findElement(By.id("loginEmail")).sendKeys("alex@keller.net");
        driver.findElement(By.id("loginPassword")).sendKeys("password");
        driver.findElement(By.id("loginButton")).click();

        WebElement headerName = driver.findElement(By.id("headerName"));

        assertNotNull(headerName);
        assertEquals("alex@keller.net", headerName.getText());

        driver.quit();
    }
}
