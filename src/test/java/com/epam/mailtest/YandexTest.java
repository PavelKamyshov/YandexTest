package com.epam.mailtest;
/**
 * Created by Pavlo_Kamyshov on 10/13/2014.
 */

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;


public class YandexTest {

//      added to sources/pages.Parameters class
//    public static final String PASSWORD = "Test1234";
//    public static final String LOGIN = "pavkam2014";
//    public static final String LETTER_SUBJECT = "Demo sending via WebDriver";
//    public static final String LETTER_BODY = "New email! You are just obvious!";

    public static final String START_URL = "https://mail.yandex.com/";
    private WebDriver driver;
    LoginPage loginObject = new LoginPage();
    DraftsPage draftsObject = new DraftsPage();
    InboxPage inboxObject = new InboxPage();
    UserMenuPage userMenuObject = new UserMenuPage();
    CreateLetterPage createLetterObject = new CreateLetterPage();
    Parameters parametersObject = new Parameters();


    @BeforeTest(description = "Start browser")
    public void startBrowser() {
        parametersObject.initializeParameters();
        //System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(START_URL);
        loginToYandexTest();

    }



    public void loginToYandexTest() {
        loginObject.loginToYandex(driver,parametersObject);
    }

    //added to pages.LoginPage
//    public void loginToYandex() {
//        doLogin(LOGIN, PASSWORD);
//        Assert.assertTrue(isElementPresent(By.xpath("//a[contains(@title, 'Inbox (Ctrl + i)')]")));
//
//    }

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

   //@Test(description = "Begin new letter creation")
    public void beginCreationOfLetterTest() {
        //driver.findElement(By.xpath("//div[@class='block-app']//div[@class='b-toolbar__i'][1]//a[2][@title='Compose (w, c)']")).click();
        inboxObject.beginCreationOfLetter(driver);
    }

    /*public void beginCreationOfLetter() {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        // System.out.println("beginCreationOfLetter is clicked ");
        //driver.findElement(By.xpath("/*//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div/div/div/div[2]/a[2]/span[1]")).click(); //Compose clicked
        //ToDO вот тут он иногда тупо не кликается почему-то!
        driver.findElement(By.xpath("//div[@class='block-app']//div[@class='b-toolbar__i'][1]//a[2][@title='Compose (w, c)']")).click(); //Compose clicked
        /*//*[@id="js"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div[1]/div/div/div[2]/a[2]

          driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        //ToDo ассерт криво работает на /#compose - не видит
        Assert.assertTrue(driver.getCurrentUrl().contains("/#"));
    }*/

    @Test(description = "Create new letter")
    public void CreateLetterTest() {
        beginCreationOfLetterTest();
        createLetterObject.createLetter(driver, parametersObject);

        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class = 'js-compose-message-actions-helper js-compose-type']//span[contains(@class,'b-pseudo-link')]")));
        try {
            draftsObject.checkDrafts(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        createLetterObject.validateLetter(parametersObject.LOGIN + "@yandex.ru", parametersObject.LETTER_SUBJECT, parametersObject.LETTER_BODY, driver);
        /* fillLetter(LOGIN + "@yandex.ru", LETTER_SUBJECT, LETTER_BODY);

        WebDriverWait wait = new WebDriverWait(driver, 15);
       // WebElement element = wait.until(
       wait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class = 'js-compose-message-actions-helper js-compose-type']//span[contains(@class,'b-pseudo-link')]")));
        try {
            checkDrafts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        validateLetter(LOGIN + "@yandex.ru", LETTER_SUBJECT, LETTER_BODY);*/
        //   ToDo
        // Assert.assertTrue(isElementPresent(By.xpath("//div[@class='message-sent__title']")));
    }

    //@Test(description = "Validate Draft", dependsOnMethods = { "beginCreationOfLetter" })
    private void checkDraftsTest() throws InterruptedException {
        draftsObject.checkDrafts(driver);

        /*Thread.sleep(3000);
        driver.findElement(By.xpath("//div[@class='b-folders__i']//a[@title= 'Drafts']")).click();
        //div[@data-action='mail.message.show-or-select']//span[contains(@title, 'Demo sending via WebDriver')]
        Assert.assertTrue(isElementPresent((By.xpath("//div[@data-action='mail.message.show-or-select']//span[contains(@title, 'Demo sending via WebDriver')]"))));
        driver.findElement((By.xpath(("//div[@class='b-messages']/div[1]//span[@class='b-messages__firstline-wrapper']")))).click();
        Thread.sleep(4000);*/
    }

    @Test(description = "ClickSendButton")
    public void clickSendButtonTest() {
        createLetterObject.clickSendButton(driver);
    }
   /* private void clickSendButton() {
        driver.findElement(By.id("compose-submit")).click();
        new WebDriverWait(driver, 10).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[3]/div/div/div[1]")));
        Assert.assertTrue(isElementPresent(By.xpath("/html/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[3]/div/div/div[1]")));
        driver.findElement(By.xpath("//a[contains(@data-action,'user-dropdown.toggle')]//span[1]")).click();
        driver.findElement(By.xpath("/html/body/div[5]/div[5]/div[10]/a")).click();
    }*/

    @AfterTest(description = "Stop Browser")
    public void stopBrowser() {
        driver.quit();
    }

    /*private boolean isElementPresent(By by) {
        return !driver.findElements(by).isEmpty();
    }*/


    //added to pages.LoginPage
//    private void doLogin(String login, String password) {
//
//        WebElement loginInput = driver.findElement(By.id("b-mail-domik-username11"));
//        loginInput.clear();
//        loginInput.sendKeys(login);
//
////        Select domainPartSelect = new Select(driver.findElement(By.xpath("//*[@id='mailbox__login__domain']")));
////        domainPartSelect.selectByVisibleText(domainPart);
//
//        WebElement passwordInput = driver.findElement(By.id("b-mail-domik-password11"));
//        passwordInput.clear();
//        passwordInput.sendKeys(password);
//
//        driver.findElement(By.xpath("//input[@value='Log in']")).click();//(By.tagNamid("mailbox__auth__button")).click();
//    }



 /*   private void validateLetter(String to, String subject, String body) {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        WebElement toField = driver.findElement(By.xpath("//div[contains(@class, 'mail-input_to')]/input[contains(@class, 'focus')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", toField);
   //       Assert.assertTrue(toField.getAttribute("b-yabble__text__content").equalsIgnoreCase(to));
        String composeSubject = driver.findElement(By.id("compose-subj")).getAttribute("value");
         Assert.assertTrue(composeSubject.equalsIgnoreCase(subject));


    }
*/


}