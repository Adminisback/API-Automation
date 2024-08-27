package APIClass;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.restassured.path.json.*;

import Files.JsonFile;
public class Demo1 {

	public static void main(String[] args) throws IOException {
		//Add place ->Update place with new address->Get place to validate whether new address is present in response or not 
		
		//Validate if add place API is working as expected..
		// STEPS  1] Given- All inputs details
		// 2] When- Submit the api
		// 3] Then- Validate response
		
		//Content of files to string -> convert to Bytes -> Convert Byte Data into Json
		//JsonFile.addPlace() ---- For adding data from eclipse
	    RestAssured.baseURI = "https://rahulshettyacademy.com";
		 String response = given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
		.body((new String (Files.readAllBytes(Paths.get("F:\\AddBookData.Json.txt"))))).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope",equalTo("APP"))
	    .header("Server","Apache/2.4.52 (Ubuntu)").extract().response().asString();
		System.out.println(response);
		JsonPath js = new JsonPath(response);
		String placeID = js.getString("place_id");
		System.out.println(placeID);
		
		
		//Update the Address
		String newAddress = "Summer adress walk, Africa";
		given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeID+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get place to varify that address changed successfully or not
		String Response = given().log().all().queryParam("key","qaclick123").queryParam("place_id",placeID)
				.when().get("maps/api/place/get/json")
				.then().assertThat().log().all().statusCode(200).extract().response().asString();
		JsonPath js1 = new JsonPath(Response);
		String actualadd = js1.getString("address");
		System.out.println(actualadd);
		//Cucumber JUnit & TestNG  -- Testing framework
	}

}
