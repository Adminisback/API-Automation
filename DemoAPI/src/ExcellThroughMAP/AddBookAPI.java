package ExcellThroughMAP;
import org.testng.annotations.*;

import POJO.CallExcellData;
import POJO.Demo1ExcellData;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.restassured.response.*;
public class AddBookAPI {

	@Test
	public void addBookDataMap() throws IOException {
		Demo1ExcellData dd = new Demo1ExcellData();
		 List<String> a =dd.getData("Data11","TestCaseData1");  //Retriving data from excell through excell utility method..
		 
		//Adding Data Through HashMap through key value pairs....
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name",a.get(1));
		map.put("isbn",a.get(2));
		map.put("aisle",a.get(3));
		map.put("author",a.get(4));
		/*
		 * HashMap<String, Object> map1 = new HashMap<String, Object>();
		 * map1.put("lat",-38.383494); map1.put("lng",33.427362); map.put("location",
		 * map1);
		 */
		
		
		
		
	     RequestSpecification request = new RequestSpecBuilder()
	    		 .setContentType(ContentType.JSON).setBaseUri("http://216.10.245.166")
	    		 .build();
	     RequestSpecification passBody = given().log().all().spec(request).body(map);
	     ResponseSpecification provideResponse =new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON)
	    		 .build();
	     Response respons = passBody.when().post("/Library/Addbook.php").then().log().all()
	    		 .spec(provideResponse).extract().response();
	     String convertResToString = respons.asString();
	     JsonPath js = new JsonPath(convertResToString);
	     String responseID = js.get("ID");
	     System.out.println(responseID);
	     
	     
	     
	}
}
