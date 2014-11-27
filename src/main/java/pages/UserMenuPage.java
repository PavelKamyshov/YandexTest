package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Pavlo_Kamyshov on 11/24/2014.
 */


public class UserMenuPage {

    public static boolean isElementPresent(By by, WebDriver driver) {
        return !driver.findElements(by).isEmpty();
    }
}
