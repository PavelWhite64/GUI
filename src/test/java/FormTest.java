import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class FormTest {
    private static WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
       driver.quit();
    }

    @Test
    void happyPathTest() {
        driver.get("http://localhost:7777/");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Павел Цыганков");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void errorEmptyTest() {
        driver.get("http://localhost:7777/");
        driver.findElement(By.tagName("button")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElements(By.className("input__sub")).get(0).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void errorNameTest() {
        driver.get("http://localhost:7777/");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Steve Master");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElements(By.className("input__sub")).get(0).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void errorPhoneTest() {
        driver.get("http://localhost:7777/");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Павел Цыганков");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("81234567890");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();

        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElements(By.className("input__sub")).get(1).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void errorCheckBoxTest() {
        driver.get("http://localhost:7777/");
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Павел Цыганков");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+71234567890");
        driver.findElement(By.tagName("button")).click();

        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        String actual = driver.findElement(By.className("checkbox__text")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }
}
