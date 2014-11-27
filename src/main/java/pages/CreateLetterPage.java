package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel on 27.11.2014.
 */
public class CreateLetterPage {

    public void createLetter(WebDriver driver) {

        fillLetter(Parameters.LOGIN + "@yandex.ru", Parameters.LETTER_SUBJECT, Parameters.LETTER_BODY, driver);
    }

    private void fillLetter(String to, String subject, String body, WebDriver driver) {
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

    public void validateLetter(String to, String subject, String body, WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        WebElement toField = driver.findElement(By.xpath("//div[contains(@class, 'mail-input_to')]/input[contains(@class, 'focus')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('disabled');", toField);
        //       Assert.assertTrue(toField.getAttribute("b-yabble__text__content").equalsIgnoreCase(to));
        String composeSubject = driver.findElement(By.id("compose-subj")).getAttribute("value");
        Assert.assertTrue(composeSubject.equalsIgnoreCase(subject));


    }

    public void clickSendButton(WebDriver driver) {
        driver.findElement(By.id("compose-submit")).click();
        new WebDriverWait(driver, 10).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[3]/div/div/div[1]")));
        Assert.assertTrue(UserMenuPage.isElementPresent(By.xpath("/html/body/div[2]/div/div[5]/div/div[2]/div/div[3]/div/div/div/div[3]/div/div/div[1]"),driver));
        driver.findElement(By.xpath("//a[contains(@data-action,'user-dropdown.toggle')]//span[1]")).click();
        driver.findElement(By.xpath("/html/body/div[5]/div[5]/div[10]/a")).click();
    }




}
