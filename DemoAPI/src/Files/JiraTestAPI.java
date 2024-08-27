package Files;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.filter.session.SessionFilter;
import java.io.File;

import org.testng.Assert;

import io.restassured.path.json.*;

public class JiraTestAPI {

	public static void main(String[] args) {
		RestAssured.baseURI = "http://localhost:8080";
		//Login to Jira app.
		SessionFilter session = new SessionFilter();  //Common Session class
		//Create session
		
		String ress = given().header("Content-Type","application/json").body("{ \"username\": \"mistarishirish\", \"password\": \"Shirish@1234567890\" }")
		.log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		
		String messageComment = "Hey I am trying to validate added coment with assert";
		String getAddedIssueID = "";
		
		// Creating issue
		String reee = given().header("Content-Type","application/json").body("{\r\n"
				+ "\"fields\": {\r\n"
				+ "        \"project\": {\r\n"
				+ "            \"key\": \"SHIR\"\r\n"
				+ "        },\r\n"
				+ "        \"summary\": \"Adding one meore issue to show swapnil\",\r\n"
				+ "        \"description\": \"Practice to crate issue through Backend selenium\",\r\n"
				+ "        \"issuetype\": {\r\n"
				+ "            \"name\": \"Bug\"\r\n"
				+ "        }\r\n"
				+ "}\r\n"
				+ "}").log().all().filter(session).when().post("/rest/api/2/issue").then().log().all().extract().response().asString();
		   System.out.println(reee);
		   JsonPath jj = new JsonPath(reee);
		   String getIDFromResponse = jj.get("id");
		   getAddedIssueID = getIDFromResponse;
		
		//Create Issue/Bug in Jira...
		
		String getCommentresponse = given().pathParam("key",getAddedIssueID).log().all().header("Content-Type","application/json").body("{\r\n"
				+ "    \"body\": \""+messageComment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}\r\n"
				+ "S").filter(session).when().post("/rest/api/2/issue/{key}/comment").then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		JsonPath js = new JsonPath(getCommentresponse);
		String commnetID = js.get("id").toString();
		
		
		
		//Attach file in created issue automatically
		given().header("X-Atlassian-Token"," no-check").filter(session).pathParam("key",getAddedIssueID)
		.header("Content-Type","multipart/form-data").multiPart("file",new File("F:\\Java_Automation\\DemoAPI\\src\\Jira.txt"))
		.when().post("/rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
		
		//Get issue Details automatically
		String getIssueDetails = given().filter(session).pathParam("key",getAddedIssueID).queryParam("fields","comment").when().get("/rest/api/2/issue/{key}").then().log().all()
	   .extract().response().asString();
		System.out.println(getIssueDetails);
		JsonPath jss = new JsonPath(getIssueDetails);
		int numberOfComments = jss.getInt("fields.comment.comments.size()");
		
		for(int i = 0; i<numberOfComments; i++) {
			String commentsss = jss.get("fields.comment.comments["+i+"].id").toString();
			System.out.println(commentsss);
			if(commentsss.equalsIgnoreCase(messageComment)) {
				Assert.assertEquals(commentsss,messageComment);
			}
		}
	}
	

}
