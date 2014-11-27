package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Created by Pavlo_Kamyshov on 11/24/2014.
 */
public class DraftsPage {

    public void checkDrafts(WebDriver driver) throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(By.xpath("//div[@class='b-folders__i']//a[@title= 'Drafts']")).click();
        //div[@data-action='mail.message.show-or-select']//span[contains(@title, 'Demo sending via WebDriver')]
        Assert.assertTrue(UserMenuPage.isElementPresent(By.xpath("//div[@data-action='mail.message.show-or-select']//span[contains(@title, 'Demo sending via WebDriver')]"),driver));
        driver.findElement((By.xpath(("//div[@class='b-messages']/div[1]//span[@class='b-messages__firstline-wrapper']")))).click();
        Thread.sleep(4000);
    }
}
