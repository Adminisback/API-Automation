package APIClass;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.restassured.parsing.Parser;
import io.restassured.path.json.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import POJO.Courses;
import POJO.api;
import POJO.webAutomation;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
public class OAuthPractice {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		//GOOGLE DIDNT ALLOW USER TO AUTOMATE LOGIN WITH 3RD PARTY..........//
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("shirishmistari23@gmail.com");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(4000);
//		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("Sonu!12345");
//		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
//		Thread.sleep(4000);
//		String url = driver.getCurrentUrl();
		
		
		//Validating below arraylist with actual arraylist from webAutomation...
		String [] actualArrayOfCourses = {"Selenium Webdriver Java","Cypppress","Protractor"};
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AfJohXnqWuMUqAOUrk7wZdCDkG-jTtemBiCT1dzXaW4-r_FiqaMt5CcFk8X3YT8ZQqLMKQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String partialURL = url.split("code=")[1];
		String code = partialURL.split("&scope")[0];
		System.out.println(code);
		
		
		//We have to get code from end point URL when we hit URL 
		String accessTokenResponse = given().queryParam("code",code).urlEncodingEnabled(false)
		.queryParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParam("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParam("grant_type", "authorization_code").when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		System.out.println(accessToken);
		
		Courses resp = given().queryParam("access_token",accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php").as(Courses.class);
		System.out.println(resp);
		System.out.println(resp.getInstructor());
		System.out.println(resp.getLinkedIn());
		//Get courseTitle from api course..
		resp.getCourses().getApi().get(1).getCourseTitle();
		List<api> getCoursePricefromAPI = resp.getCourses().getApi();
		for(int i=0; i<getCoursePricefromAPI.size(); i++) {
			if(getCoursePricefromAPI.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(getCoursePricefromAPI.get(i).getPrice());
			}
		}
		//Validating webAutomation courses list with given list of course;
		ArrayList<String> expectedList = new ArrayList<String>();
		
		List<webAutomation> aa = resp.getCourses().getWebAutomation();
		
		for(int j = 0; j<aa.size(); j++) {
			expectedList.add(aa.get(j).getCourseTitle());      //List of all courses available in webautomation.
		}
		List<String> expectedL = Arrays.asList(actualArrayOfCourses);  
		Assert.assertTrue(expectedList.equals(expectedL));

	}

}
