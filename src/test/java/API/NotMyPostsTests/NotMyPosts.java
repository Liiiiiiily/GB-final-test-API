package API.NotMyPostsTests;

import API.AbstractTest;
import API.Posts.Posts;
import io.restassured.http.Method;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class NotMyPosts extends AbstractTest {

    //1. Валидный тест: просмотр стартовой страницы ленты чужих постов

    @Test
    void getOwnerNotMeTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationNotMeOwner)
                        .expect()
                        .body(containsString("data"))
                        .body(containsString("id"))
                        .body(containsString("title"))
                        .body(containsString("description"))
                        .body(containsString("content"))
                        .body(containsString("authorId"))
                        .body(containsString("prevPage"))
                        .body(containsString("nextPage"))
                        .when()
                        .request(Method.GET, getUrl())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(Posts.class);
        assertThat(posts.meta.prevPage, equalTo(1));
    }

    //2. Валидный тест: лента чужих постов с конкретным типом сортировки и страницей (100)

    @Test
    void getSortCreatedAtTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationNotMeOwner)
                        .queryParam(sort, createdAt)
                        .queryParam(page, "100")
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

    //3. Валидный тест: лента чужих постов с конкретным значением order (ASC)

    @Test
    void getOrderASCTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationNotMeOwner)
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
                        .spec(requestSpecificationNotMeOwner)
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

    //5. Валидный тест: лента чужих постов с конкретным значением order (ALL)

    @Test
    void getOrderAllTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationNotMeOwner)
                        .queryParam(order, ALL)
                        .expect()
                        .when()
                        .request(Method.GET, getUrl())
                        .then()
                        .spec(responseSpecification)
                        .extract()
                        .body()
                        .as(Posts.class);
    }

    //6. Невалидный тест: лента чужих постов на несуществующей странице

    @Test
    void getNon_existentPageTest() {
        Posts posts =
                given()
                        .spec(requestSpecificationNotMeOwner)
                        .queryParam(sort, createdAt)
                        .queryParam(page, "2134523568713896137658936278762")
                        .expect()
                        .when()
                        .request(Method.GET, getUrl())
                        .then()
                        .spec(responseSpecificationIncorrect)
                        .extract()
                        .body()
                        .as(Posts.class);
        assertThat(posts.data.size(), equalTo(0));
        assertThat(posts.meta.nextPage, equalTo(null));
    }

    //7. Невалидный тест: лента чужих постов без авторизации

    @Test
    void getNoAuthTest() {
        given()
                .spec(requestSpecificationNoAuth)
                .queryParam(owner, notMe)
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
