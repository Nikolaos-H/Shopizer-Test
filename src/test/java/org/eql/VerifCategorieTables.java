
package org.eql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.FindBy;


public class VerifCategorieTables {
	
	WebDriver driver;
	List <WebElement> produits;
	List <WebElement> tables;
	WebElement image;
    WebElement prix;
    WebElement nom;
    WebElement ajoutPanier;
    String prixText;
    String nomText;
	String URL = "http://176.135.69.207:16500/shopizer/shop";
	
	
	@Before
	public void Initialisation() {
		
		//Choisir le driver parmis les 3 choix disponibles (Internet Explorer KO)
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
//		System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
//		System.setProperty("webdriver.ie.driver", "src/main/resources/IEDriverServer.exe");
		
		// Instanciation du WebDriver. Choisir le driver parmis les 3 choix disponibles (Internet Explorer KO)
		driver = new ChromeDriver();
//		driver = new FirefoxDriver();
//		driver = new InternetExplorerDriver();				
	}
	
	
	@Test		
	public void CategorieTables () throws InterruptedException{
	
		//navigation
		driver.get(URL);
		
		//PdT1
		//Cliquer sur Tables
		driver.findElement(By.xpath("//a[text()='Tables']")).click();
		assertTrue(driver.findElement(By.xpath("//h2[text()='Tables']")).isDisplayed());
		
		//PdT2
		//Vérifier la présence d'une liste de produits
		int i = 1;
		produits = driver.findElements(By.xpath("//div[@id='productsContainer']/div"));
		List <Integer> idProduits = new ArrayList<Integer>();
		for (WebElement produit:produits ) { 
        	System.out.println(i);
        	System.out.println(produit.getAttribute("item-name"));          
        	if (i==1) {
            	assertEquals(produit.getAttribute("item-name"),"Natural root console"); 
            	String idProduitString = produit.getAttribute("data-id");
            	int idProduitInt = Integer.parseInt(idProduitString);
            	idProduits.add(idProduitInt);
            }
        	if (i==2) {
        		assertEquals(produit.getAttribute("item-name"),"Asian rosewood console"); 
            	String idProduitString = produit.getAttribute("data-id");
            	int idProduitInt = Integer.parseInt(idProduitString);
            	idProduits.add(idProduitInt);
          	}
        	if (i==3) {
            	assertEquals(produit.getAttribute("item-name"),"Edge console");        	        
            	String idProduitString = produit.getAttribute("data-id");
            	int idProduitInt = Integer.parseInt(idProduitString);
            	idProduits.add(idProduitInt);
          	}
        	if (i==4) {
        		assertEquals(produit.getAttribute("item-name"),"Coffee table Accacia");
        		String idProduitString = produit.getAttribute("data-id");
        		int idProduitInt = Integer.parseInt(idProduitString);
        		idProduits.add(idProduitInt);
        	}
        	image = driver.findElement(By.xpath("//*[@id='productsContainer']/div["+i+"]/div[1]"));
        	prix = driver.findElement(By.xpath("//*[@id=\"productsContainer\"]/div["+i+"]//span"));
        	nom = driver.findElement(By.xpath("//*[@id=\"productsContainer\"]/div["+i+"]//h3"));
        	ajoutPanier = driver.findElement(By.xpath("//*[@id=\"productsContainer\"]/div["+i+"]//a[@class='addToCart']"));
        	prixText = prix.getText();
        	nomText = nom.getText();
        	assertTrue(image.isDisplayed());
        	assertTrue(prix.isDisplayed());
        	assertTrue(nom.isDisplayed());
        	assertTrue(ajoutPanier.isDisplayed());
        	i++;
			}
		
		//PdT3
		//Selectionner le filtre Asian Wood
		driver.findElement(By.xpath("//*[@id=\"mainContent\"]//li[3]/a")).click();
		
		//Vérifier le nombre d'élément est inférieur au nombre avant l'application du filtre
		tables=driver.findElements(By.xpath("//*[@id=\"productsContainer\"]/div"));
		assert(produits.size()>tables.size());
	
		//Vérifier que les tables affichées étaient bien dans la liste avant application du filtre
		for (WebElement table:tables ) { 
			boolean test = false;
			String idTableString = table.getAttribute("data-id");
			int idTable = Integer.parseInt(idTableString);
			for (int idProduit:idProduits) {
				if (idTable==idProduit) {
					System.out.println("La table "+table.getAttribute("item-name")+" est bien présente après application du filtre");
					test = true;
				}
			}
			assertTrue(test);
		}
	
		//PdT4
		//Cliquer sur le premier produit
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id='productsContainer']//h3")).click();
		
		//PdT5
		//Présence des éléments
		String nomItem = driver.findElement(By.xpath("//h3")).getText();
		assertEquals(nomItem.toLowerCase(),nomText.toLowerCase());
		String prixItem = driver.findElement(By.xpath("//*[@id=\"productPrice\"]/span")).getText();
		assertEquals(prixItem,prixText);
		WebElement etoileItem= driver.findElement(By.xpath("//*[@class='stars']"));
		assertTrue(etoileItem.isDisplayed());
		WebElement ajoutPanierItem = driver.findElement(By.xpath("//button[@class='btn addToCart addToCartButton btn-buy']"));
		assertTrue(ajoutPanierItem.isDisplayed());	
	}
	
	
	@After
	
	public void Final() {
		
		//Quitter le navigateur
		driver.quit();
	}
	
}	