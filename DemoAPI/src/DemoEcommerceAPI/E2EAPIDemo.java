package DemoEcommerceAPI;

import POJO.GetLoginDetails;
import POJO.GetLoginResponse;
import POJO.OrderDeatils;
import POJO.Orders;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;;

public class E2EAPIDemo {

	public static void main(String[] args) {
		E2Etesting();
	}
	public static void E2Etesting() {
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		GetLoginDetails loginDetails = new GetLoginDetails();
		loginDetails.setUserEmail("dhiraj123@gmail.com");
		loginDetails.setUserPassword("King!@12345");

		RequestSpecification login = given().log().all().spec(req).body(loginDetails);

		GetLoginResponse loginResponse = login.when().post("/api/ecom/auth/login").then().extract()
				.as(GetLoginResponse.class);
		System.out.println(loginResponse.getToken());
		String token = loginResponse.getToken();
		String userID = loginResponse.getUserId();
		System.out.println(loginResponse.getUserId());

		// Add Product
		RequestSpecification addProduct = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();
		RequestSpecification addPrresponse = given().log().all().spec(addProduct).param("productName", "qwerty")
				.param("productAddedBy", userID).param("productCategory", "fashion")
				.param("productSubCategory", "shirts").param("productPrice", "11500")
				.param("productDescription", "Addias Originals").param("productFor", "women")
				.multiPart("productImage", new File("C://Users//Shree//Downloads//Key.jpg"));
		String response = addPrresponse.when().post("/api/ecom/product/add-product").then().log().all().extract()
				.response().asString();
		JsonPath js = new JsonPath(response);
		String productID = js.get("productId");

		// Create Order...
		RequestSpecification OrderProdeuctReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).addHeader("Authorization", token).build();
		// Importing POJO for passing data in body
		OrderDeatils orderdetails = new OrderDeatils();
		orderdetails.setCountry("India");
		orderdetails.setProductOrderedId(productID);

		List<OrderDeatils> orderDetailsList = new ArrayList<OrderDeatils>();
		orderDetailsList.add(orderdetails);

		Orders ord = new Orders();
		ord.setOrders(orderDetailsList);

		RequestSpecification createOrderDetailsReq = given().log().all().spec(OrderProdeuctReq).body(ord);
		String orderDetailsResponse = createOrderDetailsReq.when().post("/api/ecom/order/create-order").then().log()
				.all().extract().response().asString();
		System.out.println(orderDetailsResponse);
		// Extracting Product order ID...
		JsonPath js1 = new JsonPath(orderDetailsResponse);
		List getOrderID =js1.get("orders");
		Object getOrderIDfromList = getOrderID.get(0);
		System.out.println(getOrderIDfromList);
		
		//GetOrder Details...
		RequestSpecification getDetails = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).addHeader("Authorization", token).build();
		RequestSpecification viewDetails = given().log().all().spec(getDetails).queryParam("orders", getOrderIDfromList);
		String getOrderDetails = viewDetails.when().get("/api/ecom/order/get-orders-details?"+"id"+"="+getOrderIDfromList).then().log()
				.all().extract().asString();
		System.out.println("**************Order Details******");
		System.out.println(getOrderDetails);
		System.out.println("*********************************");

		// Delete Product
		RequestSpecification deleteRequest = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).addHeader("Authorization", token).build();
		RequestSpecification deletePrRequest = given().log().all().spec(deleteRequest).pathParam("productId",
				productID);
		String deleteResponse = deletePrRequest.when().delete("/api/ecom/product/delete-product/{productId}").then()
				.log().all().extract().asString();
		
	

		// Delete Product Order....

		RequestSpecification deletePrOrdRe = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).addHeader("Authorization", token).build();
		RequestSpecification deleteOrderR = given().log().all().spec(deletePrOrdRe).pathParam("orders", getOrderIDfromList);
		String deleteOrderResponse = deleteOrderR.when().delete("/api/ecom/order/delete-order/{orders}").then()
				.log().all().extract().response().asString();
	}


}
