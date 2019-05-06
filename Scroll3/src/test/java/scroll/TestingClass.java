package scroll;


import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;



/*
 * http://confluence.bch.bancodechile.cl:8090/display/FNI/%5BEPI%5D+Consulta+-+Linea+y+Sublineas
 * */

public class TestingClass {
	private WebDriver driver;
	ExtentReports extent;
	ExtentTest logger;
	ExtentHtmlReporter htmlReporter;
	String htmlReportPath = "./report.html";		
	private String Nom_Class;	
	
	@BeforeClass	
	public void begin() {
		htmlReporter = new ExtentHtmlReporter(htmlReportPath);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
	}
		
	@BeforeTest
	public void testing() {
		//REPORT
			try {
				//END REPORT
				System.out.println("prueba testing");
				System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
				driver = new ChromeDriver(); 
				driver.manage().window().maximize();
				driver.get("https://login.qa.labchile.cl/bancochile-web/empresa/login/index.html#/login");
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				
				//WebElement usuario = driver.findElement(By.id("iduserName"));
				//Assert.assertTrue(usuario.isDisplayed());
				//usuario.sendKeys("164234908");
				driver.findElement(By.id("iduserName")).sendKeys("94973333");
				
				//Verifica que existe el textbox de passWord, luego asigna un valor
				//WebElement pass = driver.findElement(By.name("password"));
				//Assert.assertTrue(pass.isDisplayed());
				//pass.sendKeys("Banco001");
				driver.findElement(By.name("password")).sendKeys("Banco001");
				
				//Verifica que existe el boton, luego hace click
				//WebElement Btn = driver.findElement(By.id("idIngresar"));
				//Assert.assertTrue(Btn.isDisplayed());
				//Btn.click();
				driver.findElement(By.id("idIngresar")).click();
				Nom_Class = Nom_Class +' ' +Thread.currentThread().getStackTrace()[1].getMethodName();
				
			} catch (Exception e) {
		         System.out.println("nepe"+"Exception thrown  :" + e);
		      }
	}
	
	/*Ingreso al menu Financiamiento/Factoring/Configurar Factoring/Línea y Sublíneas*/
	@Test
	public void inicio() {
		System.out.println("inicio testing");
		//menu Financiamiento
		WebElement element = driver.findElement(By.id("nivel1-6000"));
		Mouse mouse = ((HasInputDevices) driver).getMouse();
		Locatable hoverItem = (Locatable) element;
		mouse.mouseMove(hoverItem.getCoordinates());
		
		//menu Financiamiento/Factoring/Configuración/Operaciones Pendientes
		WebElement beginner = driver.findElement(By.xpath("//*[@id=\'menu2sb-6400\']/div[1]/ul[3]/li[1]/a"));
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(beginner));
		Locatable clickItem = (Locatable) beginner;
		mouse.mouseDown(clickItem.getCoordinates());
		mouse.mouseUp(clickItem.getCoordinates());
		System.out.println(driver.getTitle());
		System.out.println("Ingreso a la pantalla Línea y Sublíneas");
		Nom_Class = Nom_Class +' ' +Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
	/*test Para confirmar Razon social */
	@Test (priority = 0, enabled = true, description = "Valida Razon Social")
	public void valida_rsocial() {
		System.out.println("Verifica Razon Social");
		logger = extent.createTest("Valida Razon Social");
		WebElement r_social = driver.findElement(By.xpath("//*[@id=\'main-portal\']/section/div/div[1]/p[1]"));
		String txt_r_social = r_social.getText();
		System.out.println("Razon Social: " + txt_r_social);
		System.out.println("Verifica Razon Social: " + txt_r_social);
		Assert.assertNotNull(txt_r_social);
		//Assert.assertNull(txt_r_social);
		logger.log(Status.PASS, MarkupHelper.createLabel("verifica que existe Razon Social", ExtentColor.CYAN));
		Nom_Class = Nom_Class +' ' +Thread.currentThread().getStackTrace()[1].getMethodName();
	}

	/*test Para confirmar Formato del rut */
	@Test (priority = 1, description = "Verifica formato del RUT")
	public void valida_rut() {
		logger = extent.createTest("Valida RUT");
		Pattern pattern = Pattern.compile("^[0-9]+-[0-9kK]{1}$");
		WebElement r_rut = driver.findElement(By.xpath("//*[@id='main-portal']/section/div/div[1]/p[2]"));
		String t_rut = r_rut.getText().substring(5);
		Matcher matcher = pattern.matcher(t_rut);
		String r = matcher.replaceAll(t_rut);
		System.out.println("Rut_Desp:"+ t_rut);
		System.out.println("Rut_Res:"+ r);
		Nom_Class = Nom_Class +' ' +Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertEquals(t_rut, r);
		logger.log(Status.PASS, MarkupHelper.createLabel("verifica Formato RUT", ExtentColor.CYAN));
		
	}

	@AfterMethod
	public void cerrar(ITestResult result) throws Exception {
		
	
		
		if(result.getStatus() == ITestResult.FAILURE) {
			System.out.println("AfterMethod ------ IF");
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + ". ISSUE", ExtentColor.RED));
			File screen = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		    
			System.out.println("Nombre Clase" + Nom_Class);
			
			BufferedImage img = ImageIO.read(screen);
			File filetest = Paths.get(".").toAbsolutePath().normalize().toFile();
			
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

			ImageIO.write(img, "jpg", new File(filetest + "\\Report\\" + Nom_Class + "logo1.jpg"));
		    logger.log(Status.FAIL, "Title verified");
		    logger.fail("foto1").addScreenCaptureFromPath("logo1.jpg");
		    logger.fail("foto1", MediaEntityBuilder.createScreenCaptureFromPath(System.getProperty("user.dir") + "\\Report\\" + Nom_Class +  "logo1.jpg").build());
	        
		    ImageIO.write(img, "jpg", new File(filetest + "\\Report\\" + "logo2.jpg"));
		    logger.fail("foto2").addScreenCaptureFromPath("logo2.jpg");
		    logger.info("foto2", MediaEntityBuilder.createScreenCaptureFromPath(System.getProperty("user.dir") + "\\Report\\" + Nom_Class +  "logo2.jpg").build());
		}
	}
	
	@AfterTest
	public void end() {
		System.out.println("Cerrar");
		extent.flush();
		driver.close();
	}
}
