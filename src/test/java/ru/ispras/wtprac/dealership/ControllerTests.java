package ru.ispras.wtprac.dealership;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

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

        assertNotNull(catalogButton);

        catalogButton.click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        assertEquals("Каталог", driver.getTitle());

        driver.quit();
    }

    void login(WebDriver driver, String username, String password) {
        driver.findElement(By.id("loginEmail")).sendKeys(username);
        driver.findElement(By.id("loginPassword")).sendKeys(password);
        driver.findElement(By.id("loginButton")).click();
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

        login(driver, "alex@keller.net", "password");

        WebElement headerName = driver.findElement(By.id("headerName"));

        assertNotNull(headerName);
        assertEquals("alex@keller.net", headerName.getText());

        driver.quit();
    }

    @Test
    void orderTest() throws InterruptedException {
        ChromeDriver driver = new ChromeDriver();

        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().maximize();
        //driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
        driver.get("http://localhost:8080/");

        WebElement loginButton = driver.findElement(By.id("headerLoginRegister"));

        assertNotNull(loginButton);
        loginButton.click();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        assertEquals("Вход и регистрация", driver.getTitle());

        login(driver, "alex@keller.net", "password");

        Thread.sleep(2000);

        assertEquals("http://localhost:8080/account", driver.getCurrentUrl());

        driver.findElement(By.id("headerCatalog")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("car1")));
        assertNotNull(element);
        driver.executeScript("arguments[0].scrollIntoView(true);", element);
        element.click();

        WebElement itemHeader = driver.findElement(By.className("main-header"));
        assertNotNull(itemHeader);
        assertEquals("Toyota Corolla White", itemHeader.getText());
        assertEquals("http://localhost:8080/car/1", driver.getCurrentUrl());


        WebElement orderButton = driver.findElement(By.className("btn-1"));
        assertNotNull(orderButton);
        orderButton.click();

        assertEquals("http://localhost:8080/order/car/1", driver.getCurrentUrl());
        WebElement orderHeader = driver.findElement(By.className("mb-0"));
        assertNotNull(orderHeader);
        assertEquals("Заказ автомобиля", orderHeader.getText());

        WebElement confirmOrderButton = driver.findElement(By.id("confirmOrderButton"));
        assertNotNull(confirmOrderButton);
        confirmOrderButton.click();

        assertEquals("http://localhost:8080/account", driver.getCurrentUrl());
        WebElement successAlert = driver.findElement(By.id("successAlert"));
        assertNotNull(successAlert);
        assertEquals("Заказ успешно оформлен!", successAlert.getText());

        WebElement txtCar = driver.findElement(By.id("orderCar1"));
        assertNotNull(txtCar);
        assertEquals("Toyota Corolla White", txtCar.getText());

        WebElement btnCancel = driver.findElement(By.id("cancelOrderCar1"));
        assertNotNull(btnCancel);
        btnCancel.click();

        assertEquals("http://localhost:8080/account", driver.getCurrentUrl());

        successAlert = driver.findElement(By.id("successAlert"));
        assertNotNull(successAlert);
        assertTrue(successAlert.getText().matches("Заказ №[0-9]+ успешно отменен\\."));
        driver.quit();
    }


}
