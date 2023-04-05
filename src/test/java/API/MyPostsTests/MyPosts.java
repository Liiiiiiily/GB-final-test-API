package API.MyPostsTests;

import API.AbstractTest;
import API.Posts.Posts;
import io.restassured.http.Method;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MyPosts extends AbstractTest {

    //1. Валидный тест: вход в ленту своих постов с валидной авторизацией

    @Test
    void getMyPostsTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationValid1)
                        .expect()
                        .when()
                        .request(Method.GET, getUrl())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(Posts.class);
        assertThat(posts.meta.nextPage, equalTo(null));
        assertThat(posts.data.size(), equalTo(0));
    }

    //2. Валидный тест: вход в ленту своих постов со своими данными в авторизации

    @Test
    void getSortCreatedAtTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationMe)
                        .queryParam(sort, createdAt)
                        .expect()
                        .when()
                        .request(Method.GET, getUrl())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(Posts.class);
        assertThat(posts.data.get(0).id - posts.data.get(1).id, greaterThan(0));
        assertThat(posts.data.get(1).id - posts.data.get(2).id, greaterThan(0));
        assertThat(posts.data.get(2).id - posts.data.get(3).id, greaterThan(0));
    }

    //3. Валидный тест: лента своих постов с конкретным значением order (ASC)

    @Test
    void getOrderASCTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationMe)
                        .queryParam(order, ASC)
                        .expect()
                        .when()
                        .request(Method.GET, getUrl())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(Posts.class);
        assertThat(posts.data.get(0).id - posts.data.get(1).id, lessThan(0));
        assertThat(posts.data.get(1).id - posts.data.get(2).id, lessThan(0));
        assertThat(posts.data.get(2).id - posts.data.get(3).id, lessThan(0));
    }

    //4. Валидный тест: лента чужих постов с конкретным значением order (DESC)

    @Test
    void getOrderDESCTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationMe)
                        .queryParam(order, DESC)
                        .expect()
                        .when()
                        .request(Method.GET, getUrl())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(Posts.class);
        assertThat(posts.data.get(0).id - posts.data.get(1).id, greaterThan(0));
        assertThat(posts.data.get(1).id - posts.data.get(2).id, greaterThan(0));
        assertThat(posts.data.get(2).id - posts.data.get(3).id, greaterThan(0));
    }

    //5. Невалидный тест: лента чужих постов на несуществующей странице

    @Test
    void getNon_existentPageTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationMe)
                        .queryParam(sort, createdAt)
                        .queryParam(page, "2312410435423543564543263727574")
                        .expect()
                        .when()
                        .request(Method.GET, getUrl())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(Posts.class);
        assertThat(posts.data.size(), equalTo(0));
        assertThat(posts.meta.nextPage, equalTo(null));
    }

    //6. Невалидный тест: лента своих постов без авторизации

    @Test
    void getNoAuthTest() {
        given()
                .spec(requestSpecificationNoAuth)
                .expect()
                .body("message", equalTo("Auth header required X-Auth-Token"))
                .when()
                .request(Method.GET, getUrl())
                .then()
                .spec(responseSpecificationIncorrect)
                .extract()
                .body();
    }
}
