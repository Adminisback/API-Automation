package APIClass;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import Files.JsonFile;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class DynamicJson {

	// private static  ReUsableMethods = null;

	@Test(dataProvider="getBookData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String respoanse = given().log().all().header("Content-Type","application/json")
		.body(JsonFile.addBook(isbn, aisle)).when().post("/Library/Addbook.php")
		.then().log().all().statusCode(200)
		.extract().response().asString();
		// JsonPath jsss = ReUsableMethods.rawToJson(respoanse);  //...ReusableMethods Not coming 
		JsonPath jsss = new JsonPath(respoanse);
		String id = jsss.get("ID");
		System.out.println(id);
	}
	//Delete Book From Json
    @DataProvider(name="getBookData")   //Get Data from dataprovider arrays
	public Object[][] getData() {
    	//Arrays--Collection of elements
    	//MultiDiamensional Arrays-- Collection Of multiple arrays 
		return new Object[][]  {
				{"Shirish","12332"},
				{"Mona","43444"},
				{"Sona","0909"},
				{"Gola","79788"}
		};
	}
}
