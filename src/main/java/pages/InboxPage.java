package pages;

import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Created by Pavlo_Kamyshov on 11/24/2014.
 */
public class InboxPage {
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
}
