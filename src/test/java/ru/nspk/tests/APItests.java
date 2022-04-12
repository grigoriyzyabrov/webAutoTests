package ru.nspk.tests;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class APItests extends Spec {


    private static Cookies cookie;

    @Tag("API")
    @Step("Получение токена")
//    @BeforeEach
    public void getAuthToken(){
        open("/Themes/DefaultClean/Content/images/logo.png");
        cookie =
                RestAssured.given(Spec.request).
                contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("Email", "user12345@gmail.com")
                .formParam("Password", "2eN2Naz!C3JS9@!")
                .when()
                .post("/login")
                .then().statusCode(302)
                        .extract()
                        .response()
                        .getDetailedCookies();
    }

    @Tag("API")
    @DisplayName("Добавляем товар в корзину")
    @Test
    public void addProductsToCart(){

        given(Spec.request)
                .cookie(String.valueOf(cookie))
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_72_5_18=53" +
                "&product_attribute_72_6_19=54" +
                "&product_attribute_72_3_20=57" +
                "&addtocart_72.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true));
    }

    @Tag("API")
    @DisplayName("Добавляем товар в Wishlist")
    @Test
    void addToWishlist(){

        given(Spec.request)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(String.valueOf(cookie))
                .body("addtocart_18.EnteredQuantity=1&addtocart_19.EnteredQuantity=1")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/18/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"));

    }

    @Tag("API")
    @DisplayName("Добавляем товар в лист сравнения")
    @Test
    void addToCompareListTest() {
        given(Spec.request)
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie(String.valueOf(cookie))
                .when()
                .get("http://demowebshop.tricentis.com/compareproducts/add/24")
                .then()
                .statusCode(200);
    }

}
