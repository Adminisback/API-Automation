package APIClass;

import Files.JsonFile;
import io.restassured.path.json.*;

public class ComplexJson {

	public static void main(String []args) {
		
		         //Make object of JsonPath...
		JsonPath js = new JsonPath(JsonFile.courseJson());
		int count = js.getInt("courses.size()");
		         //Print number of courses available...
		System.out.println(count);
		         //Print Purchase Amount...
		int purchaseAmountt = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmountt);
		         //Print title of the first first course...
		String firstCourseTitle = js.getString("courses[0].title");
		System.out.println(firstCourseTitle);
		         //Print all course titles and their respective prices..
		
		for(int i=0; i<count; i++) {
			String allCourcesTitles = js.getString("courses["+i+"].title"); //Important
			System.out.println(js.get("courses["+i+"].price").toString());  //Important
			System.out.println(allCourcesTitles);
		}
		System.out.println("Print number of copies sold by RPA course from the list");
		for(int i=0; i<count; i++) {
			String titleOfRPA = js.getString("courses["+i+"].title");
			if(titleOfRPA.equalsIgnoreCase("RPA")) {
				int numberOfCopies = js.get("courses["+i+"].copies");
				System.out.println(numberOfCopies);
				break;
			}
		}
		
	}
}
