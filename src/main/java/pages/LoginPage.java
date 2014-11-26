package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Pavlo_Kamyshov on 11/24/2014.
 */
public class LoginPage {

    public void loginToYandex(WebDriver driver) {
        doLogin(driver, LOGIN, PASSWORD);
        Assert.assertTrue(isElementPresent(By.xpath("//a[contains(@title, 'Inbox (Ctrl + i)')]"))); //тут надо метод этот перенести

    }
    private void doLogin(WebDriver driver, String login, String password) {

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
}
