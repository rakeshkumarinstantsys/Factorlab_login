import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginFromExcel {

	public static void main(String[] args) throws IOException, InterruptedException {

		File file=new File(System.getProperty("user.dir") + "\\UserData\\" + "User_Login_Data" + ".xlsx");
		FileInputStream inputstream=new FileInputStream(file);
		
		XSSFWorkbook wb=new XSSFWorkbook(inputstream);
		XSSFSheet sheet=wb.getSheet("LoginDetails");
		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
		driver.get("https://staging1.factorlablambdaapis.com/login");
		driver.manage().window().maximize();
		
		XSSFRow row=null;
		XSSFCell cell=null;
		String UserName=null;
		String password=null;
		
		for (int i=1; i<=sheet.getLastRowNum();i++)
		{
			row=sheet.getRow(i);
			for ( int j=0;j<row.getLastCellNum();j++)
			{
				cell=row.getCell(j);
				if(j==0)
				{
					UserName=cell.getStringCellValue();
				}
				if(j==1)
				{
					password=cell.getStringCellValue();
				}
			}
			driver.findElement(By.id("login-username")).sendKeys(UserName);
			Thread.sleep(1000);
			driver.findElement(By.xpath("/html/body/app-root/app-login/div/div[2]/div/div[2]/form/div[2]/button")).click();
			Thread.sleep(1000);
			driver.findElement(By.id("login-password")).sendKeys(password);
			Thread.sleep(1000);
			driver.findElement(By.xpath("/html/body/app-root/app-login/div/div[2]/div/div[2]/form/div[3]/button[1]")).click();
			Thread.sleep(1000);
			
			String result=null;
			try
			{
				Boolean isLoggedIn=driver.findElement(By.xpath("//*[@id=\'navbarHeaderContent\']/ul/li[1]/a")).isDisplayed();
				if(isLoggedIn==true)
				{
					result="PASS";
				}
				System.out.println("User Name : " + UserName + " ----  " + "Password : "  + password + " --- Login success --- " + "Test " + result);
				//System.out.println("Login successful : " + isLoggedIn);
				driver.findElement(By.id("navbarDropdown")).click();
				driver.findElement(By.xpath("//*[@id=\'navbarHeaderContent\']/ul/li[7]/div/a[2]")).click();
			}
			catch(Exception e)
			{
				Boolean isError=driver.findElement(By.xpath("//*[text()='Please enter a valid password.']")).isDisplayed();
				if(isError==true)
				{
					result="FAIL";
				}
				System.out.println("User Name : " + UserName + " ----  " + "Password : "  + password + " --- Login Failed --- " + "Test " + result);
				Thread.sleep(1000);
				driver.findElement(By.xpath("/html/body/app-root/app-login/div/div[2]/div/div[2]/form/div[3]/button[2]")).click();
				Thread.sleep(1000);
				driver.findElement(By.id("login-username")).clear();
				Thread.sleep(1000);
			}
			Thread.sleep(1000);
		
			
			
			
		}
		
	}

}
