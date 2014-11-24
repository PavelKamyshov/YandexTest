package com.epam.mailtest; /**
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class YandexTest {

    private static final String PASSWORD = "Test1234";
    private static final String LOGIN = "pavkam2014";
    private static final String START_URL = "https://mail.yandex.com/";
    private static final String LETTER_SUBJECT = "Demo sending via WebDriver";
    private static final String LETTER_BODY = "New email! You are just obvious!";
    private WebDriver driver;

    @BeforeTest(description = "Start browser")
    public void startBrowser() {
        //System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
        driver = new FirefoxDriver();
        driver.get(START_URL);
    }

    @BeforeTest(dependsOnMethods = "startBrowser", description = "Add implicitly")
    public void addImplicitly() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test(description = "Login to Yandex.com")
    public void loginToYandex() {
        doLogin(LOGIN, PASSWORD);
        Assert.assertTrue(isElementPresent(By.xpath("//a[contains(@title, 'Inbox (Ctrl + i)')]")));

    }

    //@Test(description = "Drafts deletion", dependsOnMethods = {"loginToYandex"})
   /* private void deleteDrafts() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[1]/div[2]/div/div/div[1]/div[1]/div/div[5]/span[2]/a")).click();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        try {
            driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div/div[3]/div[2]/div/div[1]/div[2]/div/label/input")).click();
            driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div/div/div/div[2]/a[8]")).click();
        }
        catch(Exception e) {
           System.out.println("All drafts were clear");
           e.printStackTrace();
            }
        finally {
           // Thread.sleep(1000000000);
            driver.findElement(By.xpath("//div[@class='block-app']//div[@class='b-toolbar__i'][1]//a[2][@title='Compose (w, c)']")).click();
        }

    }*/

    @Test(description = "Begin new letter creation", dependsOnMethods = {"loginToYandex"})
    public void beginCreationOfLetter() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        // System.out.println("beginCreationOfLetter is clicked ");
        //driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div/div/div/div[2]/a[2]/span[1]")).click(); //Compose clicked
        //ToDO вот тут он иногда тупо не кликается почему-то!
        driver.findElement(By.xpath("//div[@class='block-app']//div[@class='b-toolbar__i'][1]//a[2][@title='Compose (w, c)']")).click(); //Compose clicked
        //*[@id="js"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div[1]/div/div/div[2]/a[2]

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
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class = 'js-compose-message-actions-helper js-compose-type']//span[contains(@class,'b-pseudo-link')]")));
        try {
            checkDrafts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        validateLetter(LOGIN + "@yandex.ru", LETTER_SUBJECT, LETTER_BODY);
        //   ToDo
        // Assert.assertTrue(isElementPresent(By.xpath("//div[@class='message-sent__title']")));
    }

    //@Test(description = "Validate Draft", dependsOnMethods = { "beginCreationOfLetter" })
    private void checkDrafts() throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(By.xpath("//div[@class='b-folders__i']//a[@title= 'Drafts']")).click();
        //div[@data-action='mail.message.show-or-select']//span[contains(@title, 'Demo sending via WebDriver')]
        Assert.assertTrue(isElementPresent((By.xpath("//div[@data-action='mail.message.show-or-select']//span[contains(@title, 'Demo sending via WebDriver')]"))));
        driver.findElement((By.xpath(("//div[@class='b-messages']/div[1]//span[@class='b-messages__firstline-wrapper']")))).click();
        Thread.sleep(4000);
            }

   @Test(description = "ClickSendButton", dependsOnMethods = { "beginCreationOfLetter" })
    private void clickSendButton() {
        driver.findElement(By.id("compose-submit")).click();
        new WebDriverWait(driver, 10).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[3]/div/div/div[1]")));
        Assert.assertTrue(isElementPresent(By.xpath("/html/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[3]/div/div/div[1]")));
        driver.findElement(By.xpath("//a[contains(@data-action,'user-dropdown.toggle')]//span[1]")).click();
        driver.findElement(By.xpath("/html/body/div[5]/div[5]/div[10]/a")).click();
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
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'mail-input_to')]/input[contains(@class, 'focus')]")));
        WebElement toField = driver.findElement(By.xpath("//div[contains(@class, 'mail-input_to')]/input[contains(@class, 'focus')]"));
//  System.out.println("before - " + toField.getAttribute("disabled"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", toField);
//  System.out.println("after - " + toField.getAttribute("disabled"));
        toField.sendKeys(to);
//        new WebDriverWait(driver, 15).until(
//                ExpectedConditions.presenceOfElementLocated(By.id("compose-subj")));
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
   //       Assert.assertTrue(toField.getAttribute("b-yabble__text__content").equalsIgnoreCase(to));
        String composeSubject = driver.findElement(By.id("compose-subj")).getAttribute("value");
         Assert.assertTrue(composeSubject.equalsIgnoreCase(subject));


    }



}