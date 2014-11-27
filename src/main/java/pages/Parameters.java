package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Pavlo_Kamyshov on 11/24/2014.
 */
public class Parameters {
    public String PASSWORD = "Test1234";
    public String LOGIN = "Login not found";
    public String START_URL = "URL is not found";
    public String LETTER_SUBJECT = "Demo sending via WebDriver";
    public String LETTER_BODY = "New email! You are just obvious!";

    public void initializeParameters() {
        File myFile = new File("src/data.xml");
        if (!myFile.exists()) {
            System.out.println();
            System.out.println("File with parameters is NOT found!!!");

        }
        if (!myFile.canRead() || !myFile.canWrite()) {
            System.out.println("File with parameters can NOT be read/written");

        }

        try {
            //BufferedReader myFileSymbol = new BufferedReader(new FileReader(myFile));
            // String str;

            File fXmlFile = new File("src/data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            LOGIN = doc.getElementsByTagName("LOGIN").item(0).getTextContent();
            PASSWORD = doc.getElementsByTagName("PASSWORD").item(0).getTextContent();
            START_URL = doc.getElementsByTagName("START_URL").item(0).getTextContent();
            LETTER_SUBJECT = doc.getElementsByTagName("LETTER_SUBJECT").item(0).getTextContent();
            LETTER_BODY = doc.getElementsByTagName("LETTER_BODY").item(0).getTextContent();


        } catch (IOException e) {
            System.out.println("Error while working with file!");

        } catch (NullPointerException e) {
            System.out.println("File is not found");

        } catch (NumberFormatException e) {
            System.out.println("Incorrect amount parameter set in the file!");
            System.out.println("Working with file was skipped, default value taken");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
