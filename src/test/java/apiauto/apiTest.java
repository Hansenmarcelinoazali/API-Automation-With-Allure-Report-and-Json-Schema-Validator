package apiauto;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

public class apiTest {

@Test

    public void getUsers(){

    File jsonSchema =new File("src/test/resources/jsonSchema/getlistUserSchema.json");
    RestAssured.given().when().get("https://reqres.in/api/users?page=2").then().log().all()
            .assertThat().statusCode(200)
            .assertThat().body("per_page", Matchers.equalTo(6))
            .assertThat().body("page", Matchers.equalTo(2))
            .assertThat().body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));
}

@Test
    public void postCreateUsers(){
    String name ="hansen";
    String job = "QA";

    JSONObject jsonObject = new JSONObject();

    jsonObject.put("name",name).put("job", job);

    RestAssured.given().header("Content-Type", "application/json")
            .header("Accept","application/json")
            .body(jsonObject.toString())
            .when()
            .post("https://reqres.in/api/users").then().log().all()
            .assertThat().statusCode(201)
            .assertThat().body("name", Matchers.equalTo(name))
            .assertThat().body("job", Matchers.equalTo(job));

}

@Test
    public void putUsers(){
    RestAssured.baseURI ="https://reqres.in/";

    int userId = 2;
    String newName = "updateUser";

    //get data user dengan user id
    String fname = RestAssured.given().when().get("api/users/"+userId).getBody().jsonPath().get("data.first_name");
    String Lname = RestAssured.given().when().get("api/users/"+userId).getBody().jsonPath().get("data.last_name");
    String avatar = RestAssured.given().when().get("api/users/"+userId).getBody().jsonPath().get("data.avatar");
    String email = RestAssured.given().when().get("api/users/"+userId).getBody().jsonPath().get("data.email");

    System.out.println("nama depan sebelum diubah " +fname);

    //membuat body request dengan hash map
    HashMap<String, Object> bodyMap =new HashMap<>();
    bodyMap.put("id",userId);
    bodyMap.put("email",email);
    bodyMap.put("first_name",newName);
    bodyMap.put("last_name",Lname);
    bodyMap.put("avatar",avatar);

    JSONObject jsonObject = new JSONObject(bodyMap);

    RestAssured.given().log().all()
            .header("Content-Type","application/json")
            .body(jsonObject.toString()) //conversion jsonobject dalam bentuk string
            .put("api/users/"+userId)//memanggil request put
            .then().log().all()
            .assertThat().statusCode(200)
            .assertThat().body("first_name", Matchers.equalTo(newName));
}

@Test
    public void patchUsers(){

    RestAssured.baseURI = "https://reqres.in/";

    int userId = 3;
    String newNamed = "Hansen";

    String firstName = RestAssured.given().when().get("api/users/"+userId).getBody().jsonPath().get("data.first_name");
    System.out.println(firstName);



    HashMap<String, String> bodyMap = new HashMap<>();
    bodyMap.put("first_name",newNamed);

    JSONObject jsonObject = new JSONObject(bodyMap);

    RestAssured.given().log().all()
            .header("Content-Type","application/json")
            .body(jsonObject.toString())
            .patch("https://reqres.in/api/users/"+userId)
            .then().log().all()
            .assertThat().statusCode(200)
            .assertThat().body("first_name", Matchers.equalTo(newNamed));
}

@Test
    public void deleteUser(){

    RestAssured.baseURI = "https://reqres.in/";

    int userIdToDelete = 4;

    RestAssured.given().log().all()
            .delete("api/users"+userIdToDelete)
            .then().log().all()
            .assertThat().statusCode(204);
}
}

