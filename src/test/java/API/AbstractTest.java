package API;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AbstractTest {

    static Properties properties = new Properties();
    private static InputStream configFile;
    private static String url;
    private static String urlLogin;
    private static String header;
    private static String token_me;
    private static String token_valid1;
    private static String id_me;

    protected static String owner = "owner";
    protected static String notMe = "notMe";
    protected static String sort = "sort";
    protected static String createdAt = "createdAt";
    protected static String order = "order";
    protected static String ASC = "ASC";
    protected static String DESC = "DESC";
    protected static String ALL = "ALL";
    protected static String page = "page";
    protected static String username = "username";
    protected static String password = "password";

    protected static ResponseSpecification responseSpecification;
    protected static RequestSpecification requestSpecificationNotMeOwner;
    protected static RequestSpecification requestSpecificationNoAuth;
    protected static ResponseSpecification responseSpecificationIncorrect;
    protected static RequestSpecification requestSpecificationMe;
    protected static RequestSpecification requestSpecificationValid1;

    @BeforeAll
    static void initTest() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        configFile = new FileInputStream("src/main/resources/api.properties");
        properties.load(configFile);


        url = properties.getProperty("url");
        urlLogin = properties.getProperty("url_login");
        header = properties.getProperty("header");
        token_me = properties.getProperty("token_me");
        token_valid1 = properties.getProperty("token_valid1");
        id_me = properties.getProperty("id_me");

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                //    .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();
        

        responseSpecificationIncorrect = new ResponseSpecBuilder()
                .expectStatusCode(401)
                .expectStatusLine("HTTP/1.1 401 Unauthorized")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();

        requestSpecificationMe = new RequestSpecBuilder()
                .addHeader(header, token_me)
                .setContentType(ContentType.JSON)
                .build();

        requestSpecificationValid1 = new RequestSpecBuilder()
                .addHeader(header, token_valid1)
                .setContentType(ContentType.JSON)
                .build();

        requestSpecificationNotMeOwner = new RequestSpecBuilder()
                .addHeader(header, token_me)
                .addQueryParam(owner, notMe)
                .setContentType(ContentType.JSON)
                .build();

        requestSpecificationNoAuth = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }


    public static String getUrl() {
        return url;
    }
    public static String getUrlLogin() {
        return urlLogin;
    }
    public static String getHeader() {
        return header;
    }
    public static int getId_me() {
        return Integer.parseInt(id_me);
    }
}
