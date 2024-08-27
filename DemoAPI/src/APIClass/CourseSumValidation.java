package APIClass;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.path.json.*;

import Files.JsonFile;

public class CourseSumValidation {
 //Verify if Sum of all Course prices matches with Purchase Amount
	@Test
	public void SumOfCourseMatching() {
		int sum =0;
		JsonPath jss = new JsonPath(JsonFile.courseJson());
        int counts = jss.getInt("courses.size()");
        for(int i=0; i<counts; i++) {
        	int numberOfCopies = jss.get("courses["+i+"].copies");
        	int Prices = jss.get("courses["+i+"].price");
        	int sumAmount = Prices*numberOfCopies;
        	System.out.println(sumAmount); //Requires JCommander jar from maven repos
        	sum = sum+sumAmount;
        }
        System.out.println(sum);
        int PurchaseValue = jss.getInt("dashboard.purchaseAmount");
        Assert.assertEquals(sum,PurchaseValue);
    	
        
		
	}
}
