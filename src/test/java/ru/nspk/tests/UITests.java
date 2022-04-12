package ru.nspk.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.nspk.pages.MainPage;


import java.io.File;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UITests extends TestBase {

    private final ClassLoader cl = UITests.class.getClassLoader();

    TestData data = new TestData();
    MainPage steps = new MainPage();
    Faker faker = new Faker();
    String name = faker.name().firstName();
    String userEmail = faker.internet().emailAddress();
    String comment = faker.gameOfThrones().quote();

//  1.Простой автотест на поиск на сайте
    @Tag("UI")
    @Test
    @DisplayName("Find value")
    void menuContentOpen() {
        open("/");
        mainPage.findOnMainPage(data.aboutCompany);
    }
//  2.Автотест на выкачивание PDF-файла и проверку содержимого
    @Tag("UI")
    @Test
    @DisplayName("PDFtest")
    void checkPDF() throws Exception {
        open("/cards-mir/terms-and-tariffs/");
        File pdfDownload = Selenide.$("[target='_blank']").download();
        PDF parsed = new PDF(pdfDownload);
        assertThat(parsed.text).contains("Мир");
    }
//  3. Автотест на форму проверки обратной связи
    @Tag("UI")
    @Test
    @DisplayName("FillFormTest")
    void fillFormTest(){
        step("Открыть страницу обратной связи",() -> {
            open("/about/corporate_governance/trust-line/");
        });
        step("Скролл до формы",()->{
            $(".row").scrollTo();
            sleep(1500);
        });
        step("Ввод имя" + name,()->{
            $(".field__input.js-field-input").setValue(name);
        });
        step("Ввод email" + userEmail,()->{
            $(".field__input.js-field-input", 1).setValue(userEmail);
        });
        step("Ввод комментария"+ comment,()->{
            $(".field__input.js-field-input", 2).setValue(comment);
        });
        step("Проверяем чекбокс",()->{
            $(".control__input").shouldBe(checked);
        });
        step("Проверяем кнопку Отправить",()->{
            $(".button.feedback__footer-button.js-feedback-submit").shouldNotHave(attribute("disabled"));
        });
    }

//  4. Автотест на ту же форму с использованием степового подхода
    @Tag("UI")
    @Test
    @DisplayName("FillFormTest")
    void fillFormTestSteps(){
        steps.openFeedbackPage();
        steps.scrollToForm();
        steps.fillName();
        steps.fillEmail();
        steps.fillComment();
        steps.checkCheckbox();
        steps.checkSubmit();
    }

//  5. Параметризированный автотест на сортировку
    static Stream<Arguments> commonSearchTestCsvSource() {
        return Stream.of(
                Arguments.of("2014", "pdf"),
                Arguments.of("2015", "doc"),
                Arguments.of("2016", "doc")

        );
    }

    @Tag("UI")
    @DisplayName("SortByTest")
    @MethodSource("commonSearchTestCsvSource")
    @ParameterizedTest(name = "SortedBy:{0}")

    void sortByTests(String year, String format){

        open("/about/investor-relations/disclosure/rcbu_reports/");
        mainPage.fillSort(data.allYears,year)
                .fillSort(data.allTypes,format)
                .checkSort(year, format);
        }
    }








