package API.AuthorizationTests;

import API.AbstractTest;
import com.google.gson.Gson;
import io.restassured.http.Method;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class Auth extends AbstractTest {

    Gson g = new Gson();

    //1. Валидный тест: собственные валидные данные

    @Test
    void postAuthMeTest() {
        String s =
                given()
                        .formParam(username, "+79277371622")
                        .formParam(password, "e27a7bac9b")
                        .request(Method.POST, getUrlLogin())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .asString();
        Authorization authorization = g.fromJson(s, Authorization.class);
        System.out.println(authorization.toString());
        assertThat(authorization.username , equalTo("+79277371622"));
    }

    //2. Валидный тест: валидные данные

    @Test
    void postAuthValidTest() {
        String s =
                given()
                        .formParam(username, "ww")
                        .formParam(password, "ad57484016")
                        .request(Method.POST, getUrlLogin())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .asString();
        Authorization authorization = g.fromJson(s, Authorization.class);
        System.out.println(authorization.toString());
        assertThat(authorization.username, equalTo("ww"));
    }

    //3. Невалидный тест: логин менее 3 знаков

    @Test
    void postInvalidAuth2SymbolsTest() {
        String s =
                given()
                        .formParam(username, "qq")
                        .formParam(password, "6753")
                        .request(Method.POST, getUrlLogin())
                        .then()
                        .spec(responseSpecificationIncorrect)
                        .extract()
                        .body()
                        .asString();
    }

    //4. Невалидный тест: логин более 21 знака

    @Test
    void postInvalidAuth21SymbolsTest() {
        String s =
                given()
                        .formParam(username, "qwertyuioplkjhgfdsazs")
                        .formParam(password, "0493815307856238")
                        .request(Method.POST, getUrlLogin())
                        .then()
                        .spec(responseSpecificationIncorrect)
                        .extract()
                        .body()
                        .asString();
    }

    //5. Невалидный тест: логин и пароль пустые

    @Test
    void postInvalidAuthNullLoginAndPasswordTest() {
        given()
                .formParam(username, "")
                .formParam(password, "")
                .request(Method.POST, getUrlLogin())
                .then()
                .spec(responseSpecificationIncorrect)
                .extract()
                .body();
    }

    //6. Невалидный тест: логин - кириллицей

    @Test
    void postInvalidAuthRUTest() {
        given()
                .formParam(username, "юзернейм")
                .formParam(password, "3009")
                .request(Method.POST, getUrlLogin())
                .then()
                .spec(responseSpecificationIncorrect)
                .extract()
                .body();
    }

}
