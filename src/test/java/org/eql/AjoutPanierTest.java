package org.eql;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;

import static org.junit.Assert.*;

public class AjoutPanierTest {

	String URL = "http://176.135.69.207:16500/shopizer/shop";
	WebDriver driver;
	
	
	@Before
	public void InitialisationTest() {		
			
		//Choisir le driver parmis les 3 choix disponibles (Internet Explorer KO)
//		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
//		System.setProperty("webdriver.ie.driver", "src/main/resources/IEDriverServer.exe");
		
		
		// Instanciation du WebDriver. Choisir le driver parmis les 3 choix disponibles (Internet Explorer KO)
//		driver = new ChromeDriver();
		driver = new FirefoxDriver();
//		driver = new InternetExplorerDriver();	
	}
		
	
	@Test
	public void utilisationDuPanierTest() throws InterruptedException {

		// instanciation de de WebDriver
		Actions action = new Actions(driver);

		// CONFIGURATION D'UN IMPLICIT WAIT
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// PdT 1
		// Accès à Shopizer
		driver.get(URL);

		// PdT2
		// Cliquer sur un objet pour l'ajouter au panier
		driver.findElement(By.xpath("//a[@class='addToCart']")).click();
		// Vérifier que le panier a été incrémenté
		assertEquals(driver.findElement(By.xpath("//div[@id='miniCartSummary']//strong")).getText(), "(1)");

		// Récupérer le nom du produit ajouter
		// a[@class='addToCart']/preceding::h3
		String produitAjoute = driver.findElement(By.xpath("//a[@class='addToCart']/preceding::h3")).getText()
				.toLowerCase();

		Thread.sleep(1000);

		// PdT3
		// Selectionner le panier
		action.moveToElement(driver.findElement(By.xpath("//div[@id='miniCartSummary']"))).build().perform();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//li[@class='checkout-bg']/a")).click();
		// Vérifier qu'on est sur la page récapitulatif du panier
		assertEquals(driver.findElement(By.xpath("//h1")).getText(), "Revoir votre commande");

		// PdT4
		// Vérifier présence du produit précédemment ajouté
		assertEquals(driver.findElement(By.xpath("//td[@data-th='Article']//strong")).getText().toLowerCase(),
				produitAjoute);
		// Vérification de la présence d'un tableau comprenant une image, un nom, une
		// quantité, un prix et un sous-total par article
		// Image
		assertTrue(driver.findElement(By.xpath("//tr//img")).isDisplayed());
		// Nom
		assertTrue(driver.findElement(By.xpath("//tr//span[@class='nomargin']")).isDisplayed());
		// Quantité
		assertTrue(driver.findElement(By.xpath("//tr/td[@data-th='Quantité']")).isDisplayed());
		// Prix
		assertTrue(driver.findElement(By.xpath("//tr/td[@data-th='Prix']")).isDisplayed());
		// Sous-Total
		assertTrue(driver.findElement(By.xpath("//tr/td[@data-th='Total']")).isDisplayed());
		// Récupérer le sous-total
		String sousTotal1String = driver.findElement(By.xpath("//tr/td[@data-th='Total']/strong")).getText();
		String sousTotal1StringOnlyNumber = sousTotal1String.substring(3);
		double sousTotal1 = Double.parseDouble(sousTotal1StringOnlyNumber);

		// PdT5
		// Doubler la quantité du produit
		driver.findElement(By.xpath("//tr/td[@data-th='Quantité']/input")).clear();
		driver.findElement(By.xpath("//tr/td[@data-th='Quantité']/input")).sendKeys("2");
		// Vérifier que seul la quantité change, pas le total.
		String sousTotal2String = driver.findElement(By.xpath("//tr/td[@data-th='Total']/strong")).getText();
		String sousTotal2StringOnlyNumber = sousTotal2String.substring(3);
		double sousTotal2 = Double.parseDouble(sousTotal2StringOnlyNumber);
		assertEquals(sousTotal2, sousTotal1, 0);

		// PdT6
		// Cliquer sur Recalculer
		driver.findElement(By.xpath("//a[text()='Recalculer']")).click();
		Thread.sleep(1000);
		// Vérifier que sous total est est égale aux prix*2
		String sousTotal3String = driver.findElement(By.xpath("//tr/td[@data-th='Total']/strong")).getText();
		String sousTotal3StringOnlyNumber = sousTotal3String.substring(3);
		double sousTotal3 = Double.parseDouble(sousTotal3StringOnlyNumber);
		assertEquals(sousTotal3, sousTotal2 * 2, 0);

		// PdT7
		// Cliquer sur Proceed to checkout
		driver.findElement(By.xpath("//div[@class='wc-proceed-to-checkout']/a")).click();
		// Vérifier qu'on est sur la page paiement.
		assertEquals(driver.findElement(By.xpath("//h1")).getText(), "Paiement");
		
	}
	
	@After
	public void FinalTest() {
		
		//Quitter le navigateur
		driver.quit();
		
	}

}