/**
 * Created by Pavlo_Kamyshov on 10/13/2014.
 */

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class YandexTest {

    private static final String PASSWORD = "Test1234";
    private static final String LOGIN = "pavkam2014";
    private static final String START_URL = "https://mail.yandex.com/";

    private static final String LETTER_SUBJECT = "Demo sending via WebDriver";
    private static final String LETTER_BODY = "New email! You are just obvious!";
    private WebDriver driver;

    @BeforeClass(description = "Start browser")
    public void startBrowser() {
        //System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
        driver = new FirefoxDriver();
        driver.get(START_URL);
    }

    @BeforeClass(dependsOnMethods = "startBrowser", description = "Add implicitly")
    public void addImplicitly() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test(description = "Login to Yandex.com")
    public void loginToYandex() {
        doLogin(LOGIN, PASSWORD);
        Assert.assertTrue(isElementPresent(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[1]/div[2]/div/div/div[1]/div[1]/div/div[1]/span[2]/a/span")));

    }

    //@Test(description = "Drafts deletion", dependsOnMethods = {"loginToYandex"})
    private void deleteDrafts() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[1]/div[2]/div/div/div[1]/div[1]/div/div[5]/span[2]/a")).click(); //кликаем в Drafts
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        try {
            driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div/div[3]/div[2]/div/div[1]/div[2]/div/label/input")).click(); //чекаем чекбокс Drafts
            driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div/div/div/div[2]/a[8]")).click(); //удаляем все драфтс
        }
        catch(Exception e) {
           System.out.println("All drafts were clear");
           e.printStackTrace();
            }
        finally {
           // Thread.sleep(1000000000);
            driver.findElement(By.xpath("//div[@class='block-app']//div[@class='b-toolbar__i'][1]//a[2][@title='Compose (w, c)']")).click(); //кликаем по Compose кнопке
        }

    }

    @Test(description = "Begin new letter creation", dependsOnMethods = {"loginToYandex"})
    public void beginCreationOfLetter() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        // System.out.println("beginCreationOfLetter is clicked ");
        //driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div/div/div/div[2]/a[2]/span[1]")).click(); //Compose clicked
        //ToDO вот тут он иногда тупо не кликается почему-то!
        driver.findElement(By.xpath("//div[@class='block-app']//div[@class='b-toolbar__i'][1]//a[2][@title='Compose (w, c)']")).click(); //Compose clicked
        //*[@id="js"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div[1]/div/div/div[2]/a[2] А НЕ КЛИКАЕТСЯ ПОТОМУ ЧТО МЕНЯЕТСЯ XPATH!!! /span

          driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        //ToDo ассерт криво работает на /#compose - не видит
        Assert.assertTrue(driver.getCurrentUrl().contains("/#"));
    }

    @Test(description = "Create new letter", dependsOnMethods = {"beginCreationOfLetter"})
    public void CreateNewLetter() {

         fillLetter(LOGIN + "@yandex.ru", LETTER_SUBJECT, LETTER_BODY);

        WebDriverWait wait = new WebDriverWait(driver, 15);
       // WebElement element = wait.until(
       wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[2]/div/div/form/div/div[2]/div[6]/div[2]/span/span/span/span[1]"))); //Ждем пока появится надпись Saved As Draft
        try {
            checkDrafts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        validateLetter(LOGIN + "@yandex.ru", LETTER_SUBJECT, LETTER_BODY);  //Проверяем валидность письма
        //   ToDo
        // Assert.assertTrue(isElementPresent(By.xpath("//div[@class='message-sent__title']"))); //Проверяем, что письмо отправилось.
    }

    //@Test(description = "Validate Draft", dependsOnMethods = { "beginCreationOfLetter" })
    private void checkDrafts() throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[1]/div[2]/div/div/div[1]/div[1]/div/div[5]/span[2]/a")).click(); //Кликаем на Драфтс??
        Assert.assertTrue(isElementPresent((By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[1]/div[3]/div[2]/div/div[2]/div[2]/div/span[2]/span/a/span[1]/span/span[1]")))); //Проверяем, что есть элементы
        driver.findElement((By.xpath(("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[1]/div[3]/div[2]/div/div[2]/div[2]/div/span[2]/span/a/span[1]/span/span[1]")))).click();   //Заходим в наш Драфт

        Thread.sleep(4000);
    }

    //@Test(description = "ClickSendButton", dependsOnMethods = { "beginCreationOfLetter" })
    private void clickSendButton() {
        driver.findElement(By.id("compose-submit")).click();
    }

    @AfterClass(description = "Stop Browser")
    public void stopBrowser() {
        driver.quit();
    }

    private boolean isElementPresent(By by) {
        return !driver.findElements(by).isEmpty();
    }

    private void doLogin(String login, String password) {

        WebElement loginInput = driver.findElement(By.id("b-mail-domik-username11"));
        loginInput.clear();
        loginInput.sendKeys(login);

//        Select domainPartSelect = new Select(driver.findElement(By.xpath("//*[@id='mailbox__login__domain']")));
//        domainPartSelect.selectByVisibleText(domainPart);

        WebElement passwordInput = driver.findElement(By.id("b-mail-domik-password11"));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        driver.findElement(By.xpath("//input[@value='Log in']")).click();//(By.tagNamid("mailbox__auth__button")).click();
    }

    private void fillLetter(String to, String subject, String body) {
       // driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        new WebDriverWait(driver, 15).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'mail-input_to')]/input[contains(@class, 'focus')]"))); //Ждем пока появится To field
        WebElement toField = driver.findElement(By.xpath("//div[contains(@class, 'mail-input_to')]/input[contains(@class, 'focus')]")); // локейтим поле To
//  System.out.println("before - " + toField.getAttribute("disabled"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", toField);
//  System.out.println("after - " + toField.getAttribute("disabled"));
        toField.sendKeys(to);
//        new WebDriverWait(driver, 15).until(
//                ExpectedConditions.presenceOfElementLocated(By.id("compose-subj"))); //Ждем пока появится Subject field
        driver.findElement(By.id("compose-subj")).sendKeys(subject);


        // doesn't work ???
        // driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='compose_330_composeEditor_ifr']")));
        //!!! driver.switchTo().frame(driver.findElement(By.id("compose-send")));
        // driver.switchTo().frame(3); // WTF Magic!?

        WebElement bodyInput = driver.findElement(By.id("compose-send"));
        bodyInput.clear();
        bodyInput.sendKeys(body);
        bodyInput.sendKeys(" Sincerely yours - me.");
        try {
          //  Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bodyInput.sendKeys(" P.S. Bla bla");


        try {
          //  Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void validateLetter(String to, String subject, String body) {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        WebElement toField = driver.findElement(By.xpath("//div[contains(@class, 'mail-input_to')]/input[contains(@class, 'focus')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", toField);
//        Assert.assertTrue(toField.getText() == to);
        Assert.assertTrue(driver.findElement(By.id("compose-subj")).getText() == subject);


       /* WebElement bodyInput = driver.findElement(By.id("compose-send"));
        bodyInput.clear();
        bodyInput.sendKeys(body);
        bodyInput.sendKeys(" Sincerely yours - me.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bodyInput.sendKeys(" P.S. Bla bla");


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            */

    }


}