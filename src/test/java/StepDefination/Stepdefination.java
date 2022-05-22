package StepDefination;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import io.cucumber.java.After;
import io.cucumber.java.en.*;

public class Stepdefination {
	
	WebDriver driver;
	WebDriverWait wait;
    List<String> tweetTexts = new ArrayList<>();
    SoftAssert softAssert;

	@Given("Login to {string} with {string} and {string}")
		public void loginTwitter(String url, String username, String password) throws InterruptedException {
	        String driverPath=System.getProperty("user.dir")+"\\Driver\\chromedriver.exe";
	    	System.out.println("Path is- "+driverPath);
	    	System.setProperty("webdriver.chrome.driver",driverPath);
	        driver = new ChromeDriver();
	        wait=new WebDriverWait(driver, 120);
	        driver.manage().window().maximize();

	        driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);


	        driver.get(url);
	        WebElement signInButton=driver.findElement(By.xpath("//span[text()='Sign in']"));		
	        		
	        signInButton.click();
	      //  wait.until(ExpectedConditions.invisibilityOf(signInButton));
	        WebElement nextButton=driver.findElement(By.xpath("//span[contains(text(),'Next')]"));
	        nextButton.click();
	        Thread.sleep(3000);
	     //   wait.until(ExpectedConditions.visibilityOf(nextButton));
			softAssert = new SoftAssert();

	        	softAssert.assertTrue(driver.getPageSource().contains("Sorry, we could not find your account."));
	        
	        driver.findElement(By.xpath("//input[@autocomplete='username']")).sendKeys(username);

	        driver.findElement(By.xpath("//span[contains(text(),'Next')]")).click();
	        
	        driver.findElement(By.xpath("//input[@autocomplete='current-password']")).click();
	        driver.findElement(By.xpath("//input[@autocomplete='current-password']")).sendKeys(password);

	        driver.findElement(By.xpath("//div[@data-testid='LoginForm_Login_Button']")).click();

	        
	}

	@And("Navigate Profile Page of logged {string}")
	public void upoadProfilePic(String username) throws InterruptedException {
        String profileXpath = "//a[contains(@href,'" + username + "')][@aria-label='Profile']";
        driver.findElement(By.xpath(profileXpath)).click();
        Thread.sleep(2000);
        wait = new WebDriverWait(driver, 120);
        WebElement editProfile = driver.findElement(By.xpath("//a[@data-testid='editProfileButton']"));
        wait.until(ExpectedConditions.visibilityOf(editProfile));

        editProfile.click();
	}

	@And("upload a profile picture from {string}")
	public void uploadAProfilePictureFromLocation(String location) throws InterruptedException {
        Thread.sleep(2000);
        WebElement uploadImage = driver.findElement(By.xpath("//div[@aria-label='Add avatar photo']/following-sibling::input"));
        //wait = new WebDriverWait(driver, 120);
        //wait.until(ExpectedConditions.visibilityOf(uploadImage));
        String imgLocaion=System.getProperty("user.dir")+"//Images//"+location; 
        uploadImage.sendKeys(imgLocaion);
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[@data-testid='applyButton']")).click();
	}

	@When("update {string} as {string}")
	public void updateFieldsToUpdateAsFieldsValue(String field, String value) {
        String[] fields = field.split(",");
        String[] values = value.split(",");
        String fieldXpath = "";
        wait = new WebDriverWait(driver, 120);
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].equalsIgnoreCase("bio")) {
                fieldXpath = "//span[text()='Bio']/ancestor::label//textarea";
            } else {
                fieldXpath = "//span[text()='" + fields[i] + "']/ancestor::label//input";
            }

            WebElement fieldToValidate = driver.findElement(By.xpath(fieldXpath));
            wait.until(ExpectedConditions.visibilityOf(fieldToValidate));
            fieldToValidate.clear();
            fieldToValidate.sendKeys(values[i]);


        }

        driver.findElement(By.xpath("//div[@data-testid='Profile_Save_Button']")).click();
	}

	@Then("Values of {string} should be {string} on profile section")
	public void validateFieldsInProfile(String field, String value) {
        String[] fields = field.split(",");
        String[] values = value.split(",");
     //   driver.findElement(By.xpath("//a[@data-testid='AppTabBar_Profile_Link']")).click();

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].equalsIgnoreCase("bio")) {
                String val = driver.findElement(By.xpath("//div[@data-testid='UserDescription']")).getText();
                System.out.println("Value of field" + fields[i] + "is- " + val);
                Assert.assertTrue(val.equalsIgnoreCase(values[i]));
            } else if (fields[i].equalsIgnoreCase("location")) {
                String val = driver.findElement(By.xpath("//span[contains(@data-testid,'UserLocation')]")).getText();
                System.out.println("Value of field" + fields[i] + "is- " + values[i]);
                Assert.assertTrue(val.equalsIgnoreCase(values[i]));
            } else if (fields[i].equalsIgnoreCase("website")) {
                String val = driver.findElement(By.xpath("//a[contains(@data-testid,'UserUrl')]")).getText();
                System.out.println("Value of field" + fields[i] + "is- " + values[i]);
                Assert.assertTrue(val.equalsIgnoreCase(values[i]));
            }
        }

    }
	@When("Tweets are searched of the page {string}")
	public void searchTwitterPage(String inputPage) throws InterruptedException {
        driver.findElement(By.xpath("//a[@data-testid='AppTabBar_Home_Link']")).click();
        driver.findElement(By.xpath("//input[contains(@data-testid,'SearchBox_Search_Input')]")).sendKeys(inputPage);
        Thread.sleep(3000);
        List<WebElement> searchResults = driver.findElements(By.xpath("//div[@data-testid='typeaheadResult']"));
        System.out.println("Number of search results are " + searchResults.size());
        for (WebElement result : searchResults) {
            if (result.getText().contains(inputPage)) {
                result.click();
                break;
            }

        }

    }
	   @And("Fetch the tweets from last {string} hours")
	    public void fetchTheTweetsFromLastHours(String hours) throws InterruptedException {
	        Thread.sleep(2000);
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.scrollBy(0,15000)", "");

	        List<WebElement> tweetTime = driver.findElements(By.xpath("//article//time/parent::a"));
	        		

	        for (WebElement tweet : tweetTime) {
	            if (tweet.getAttribute("aria-label").contains("minutes ago")) {
	                System.out.println("\n Tweet is done " + tweet.getAttribute("aria-label") + " ");
	                String xpathOfTweet = "//a[@id='" + tweet.getAttribute("id") + "']/ancestor::article//div[@data-testid='tweetText']";
	                tweetTexts.add(driver.findElement(By.xpath(xpathOfTweet)).getText());


	                
	            } else if (tweet.getAttribute("aria-label").contains("hours")||tweet.getAttribute("aria-label").contains("hour")) {
	                //if(Character.getNumericValue(tweet.getAttribute("aria-label").charAt(0)<=Integer.parseInt(hours)))
	                int tweetHour = Character.getNumericValue(tweet.getAttribute("aria-label").charAt(0));
	                System.out.println("\n Tweet is done " + tweetHour + " hours ago");
	                int expectedHour = Integer.parseInt(hours);
	                if (tweetHour <= expectedHour) {
	                    String xpathOfTweet = "//a[@id='" + tweet.getAttribute("id") + "']/ancestor::article//div[@data-testid='tweetText']";
	                    tweetTexts.add(driver.findElement(By.xpath(xpathOfTweet)).getText());

	                }
	            }
	        }
	        System.out.println("Tweets within time range are- \n" + tweetTexts);
	        System.out.println("Number of tweets within time range are- " + tweetTexts.size());


	    }

	    @Then("Split the tweets which are more than {string} characters")
	    public void splitTheTweetsWhichAreMoreThanCharacters(String numberOfChar) {
	        for (String tweet : tweetTexts) {
	            System.out.println("\n Tweet text is- " + tweet);
	            if (tweet.length() > Integer.parseInt(numberOfChar)) {
	                System.out.println("\n 1/3 Tweet text- " + tweet.substring(0, 20));
	                System.out.println("\n 2/3 Tweet text- " + tweet.substring(21, 40));
	                System.out.println("\n 3/3 Tweet text- " + tweet.substring(41, tweet.length()));

	            }


	        }

	    }

	    @After
	    public void cleanUp() {
	    	try {
	    		softAssert.assertAll();
			} catch (AssertionError e) {
				driver.quit();
			}
	        driver.quit();
	    }

}

