import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseSetup{

    public WebDriverWait wait;
    public AndroidDriver driver;

    public void WriteExcel (String username, String email, String pwd, String name) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("RegisterPersonInfo");
        sheet.setColumnWidth(0,3000);
        sheet.setColumnWidth(1,3000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("User Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("E-Mail");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Password");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(1);
        Cell cell = row.createCell(0);
        cell.setCellValue(username);
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(email);
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue(pwd);
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue(name);
        cell.setCellStyle(style);

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0,path.length()-1)+"info.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();

    }

    public Map<Integer, List<String>> ReadExcel () throws IOException {
        try {
            FileInputStream file = new FileInputStream(new File("/Users/soneronbasi/Downloads/project/info.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            Integer integer = Integer.valueOf(i);
            for(Row row : sheet){
                data.put(i, new ArrayList<String>());
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            data.get((i)).add(cell.getRichStringCellValue().getString());
                            break;
                        default: data.get((i)).add(" ");
                    }
                }
                i++;
            }
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @BeforeTest
    public void setup () throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", "Pixel 4");
        caps.setCapability("udid", "emulator-5554"); //DeviceId from "adb devices" command
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "11.0");
        caps.setCapability("appPackage", "io.selendroid.testapp");
        caps.setCapability("appActivity","io.selendroid.testapp.HomeScreenActivity");
        caps.setCapability("noReset","false");
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"),caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(100));
    }

    @BeforeMethod
    public void enterMainPage() throws InterruptedException {

        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable
                (By.id("com.android.permissioncontroller:id/continue_button")));
        continueButton.click();
        WebElement versionButton = wait.until(ExpectedConditions.elementToBeClickable
                (By.id("android:id/button1")));
        versionButton.click();
    }

    @Test
    public void Test1 () throws InterruptedException, IOException {

        String username = "soneronbasi";
        String name = "Soner Onbasi";
        String pwd = "Soner1234";
        String email = "soner@soner.com";


        WebElement registerButtonOnMainPage = wait.until(ExpectedConditions.elementToBeClickable(By.id("io.selendroid.testapp:id/startUserRegistration")));
        registerButtonOnMainPage.click();

        Thread.sleep(1000);
        driver.hideKeyboard();

        WebElement usernameTxt = driver.findElement(By.id("io.selendroid.testapp:id/inputUsername"));
        usernameTxt.sendKeys(username);

        WebElement emailTxt = driver.findElement(By.id("io.selendroid.testapp:id/inputEmail"));
        emailTxt.sendKeys(email);

        WebElement passwordTxt = driver.findElement(By.id("io.selendroid.testapp:id/inputPassword"));
        passwordTxt.sendKeys(pwd);

        WebElement nameTxt =driver.findElement(By.id("io.selendroid.testapp:id/inputName"));
        nameTxt.clear();
        nameTxt.sendKeys(name);

        WebElement adButton = driver.findElement(By.id("io.selendroid.testapp:id/input_adds"));
        driver.hideKeyboard();
        adButton.click();

        WebElement registerBtnOnRegisterPage =
                wait.until(ExpectedConditions.elementToBeClickable(By.id("io.selendroid.testapp:id/btnRegisterUser")));
        registerBtnOnRegisterPage.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("io.selendroid.testapp:id/label_name")));

        String  nameLabel = driver.findElement(By.id("io.selendroid.testapp:id/label_name_data")).getText();
        String usernameLabel = driver.findElement(By.id("io.selendroid.testapp:id/label_username_data")).getText();
        String pwdLabel = driver.findElement(By.id("io.selendroid.testapp:id/label_password_data")).getText();
        String emailLabel = driver.findElement(By.id("io.selendroid.testapp:id/label_email_data")).getText();


        Assert.assertEquals(nameLabel,name,"Name is not match");
        Assert.assertEquals(usernameLabel,username,"Username is not match");
        Assert.assertEquals(pwdLabel,pwd,"Password is not match");
        Assert.assertEquals(emailLabel,email,"Email is not match");


        WebElement registerBtnOnAssertPage = driver.findElement(By.id("io.selendroid" +
                ".testapp:id/buttonRegisterUser"));
        registerBtnOnAssertPage.click();

    }

    @Test
    public void Test2 () throws InterruptedException {

        String visibleText = "Text is sometimes displayed";

        WebElement visibleButton = driver.findElement(By.id("io.selendroid" +
                ".testapp:id/visibleButtonTest"));
        visibleButton.click();

        String openedText =
                driver.findElement(By.id("io.selendroid.testapp:id/visibleTextView")).getText();
        Assert.assertEquals(openedText,visibleText,
                "Text " +
                "is not correct");
    }

    @Test
    public void Test3 () throws InterruptedException,IOException{

            WriteExcel("sonbasi","soner@soner.com","soner1234","soner onbasi");

            String username = ReadExcel().get(1).get(0);
            String email = ReadExcel().get(1).get(1);
            String pwd = ReadExcel().get(1).get(2);
            String name = ReadExcel().get(1).get(3);

        WebElement registerButtonOnMainPage = wait.until(ExpectedConditions.elementToBeClickable(By.id("io.selendroid.testapp:id/startUserRegistration")));
        registerButtonOnMainPage.click();

        Thread.sleep(1000);
        driver.hideKeyboard();

        WebElement usernameTxt = driver.findElement(By.id("io.selendroid.testapp:id/inputUsername"));
        usernameTxt.sendKeys(username);

        WebElement emailTxt = driver.findElement(By.id("io.selendroid.testapp:id/inputEmail"));
        emailTxt.sendKeys(email);

        WebElement passwordTxt = driver.findElement(By.id("io.selendroid.testapp:id/inputPassword"));
        passwordTxt.sendKeys(pwd);

        WebElement nameTxt =driver.findElement(By.id("io.selendroid.testapp:id/inputName"));
        nameTxt.clear();
        nameTxt.sendKeys(name);

        WebElement adButton = driver.findElement(By.id("io.selendroid.testapp:id/input_adds"));
        driver.hideKeyboard();
        adButton.click();

        WebElement registerBtnOnRegisterPage =
                wait.until(ExpectedConditions.elementToBeClickable(By.id("io.selendroid.testapp:id/btnRegisterUser")));
        registerBtnOnRegisterPage.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("io.selendroid.testapp:id/label_name")));

        String  nameLabel = driver.findElement(By.id("io.selendroid.testapp:id/label_name_data")).getText();
        String usernameLabel = driver.findElement(By.id("io.selendroid.testapp:id/label_username_data")).getText();
        String pwdLabel = driver.findElement(By.id("io.selendroid.testapp:id/label_password_data")).getText();
        String emailLabel = driver.findElement(By.id("io.selendroid.testapp:id/label_email_data")).getText();


        Assert.assertEquals(nameLabel,name,"Name is not match");
        Assert.assertEquals(usernameLabel,username,"Username is not match");
        Assert.assertEquals(pwdLabel,pwd,"Password is not match");
        Assert.assertEquals(emailLabel,email,"Email is not match");


        WebElement registerBtnOnAssertPage = driver.findElement(By.id("io.selendroid" +
                ".testapp:id/buttonRegisterUser"));
        registerBtnOnAssertPage.click();

    }

    @Test
    public void Test4 () throws InterruptedException {

        WebElement popupButton = driver.findElement(By.id("io.selendroid" +
                ".testapp:id/showPopupWindowButton"));
        popupButton.click();

        TouchAction touchAction = new TouchAction(driver);
        touchAction.tap(PointOption.point(538,1015)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(10))).perform();


    }

    @AfterMethod
    public void resetApp(){
        driver.resetApp();
    }

    @AfterTest
    public void teardown(){
        driver.quit();
    }
}
