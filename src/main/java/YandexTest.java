/**
 * Created by Pavlo_Kamyshov on 10/13/2014.
 */
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
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
        driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[1]/div[2]/div/div/div[1]/div[1]/div/div[5]/span[2]/a")).click(); //попадаем в Drafts
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        // driver.findElement(By.xpath("div.block-messages:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > label:nth-child(1) > input:nth-child(1)")).click(); //чекаем чекбокс Drafts
        // driver.findElement(By.xpath("a.b-messages-head__infoline__link:nth-child(2)")).click(); //удаляем все драфтс

    }

    @Test(description = "Begin new letter creation", dependsOnMethods = { "loginToYandex" })
    public void beginCreationOfLetter() {
       driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
       // System.out.println("beginCreationOfLetter is clicked ");
        driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[2]/div/div/div/div[2]/a[2]/span[1]")).click();
        //<input type="submit" tabindex="4" class="b-mail-button__button" value="Log in">
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        //ToDo
        Assert.assertTrue(driver.getCurrentUrl().contains("/#"));
    }

    @Test(description = "Create new letter", dependsOnMethods = { "beginCreationOfLetter" })
    public void CreateNewLetter() {
        //System.out.println("beginCreationOfLetter is tried method sendLetter");
        sendLetter(LOGIN + "@yandex.ru", LETTER_SUBJECT, LETTER_BODY);
        Assert.assertTrue(isElementPresent(By.xpath("//div[@class='message-sent__title']")));
    }

    @Test(description = "Validate Draft", dependsOnMethods = { "beginCreationOfLetter" })
    private void checkDrafts () throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[1]/div[2]/div/div/div[1]/div[1]/div/div[5]/span[2]/a")).click();
        Assert.assertTrue(isElementPresent((By.xpath("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[2]/div[3]/div/div/div[2]/div[2]/div[2]/span[2]/span/a/span[1]/span/span"))));
        driver.findElement((By.xpath(("//*[@id=\"js\"]/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[2]/div[3]/div/div/div[2]/div[2]/div[2]/span[2]/span/a/span[1]/span/span")))).click();

    }

    //@Test(description = "ClickSendButton", dependsOnMethods = { "beginCreationOfLetter" })
    private void clickSendButton(){
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

    private void sendLetter(String to, String subject, String body) {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        WebElement toField = driver.findElement(By.xpath("//div[contains(@class, 'mail-input_to')]/input[contains(@class, 'focus')]"));
//  System.out.println("before - " + toField.getAttribute("disabled"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", toField);
//  System.out.println("after - " + toField.getAttribute("disabled"));
        toField.sendKeys(to);
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bodyInput.sendKeys(" P.S. Bla bla");
        bodyInput.sendKeys("   ");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



}