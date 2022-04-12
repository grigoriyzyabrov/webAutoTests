package ru.nspk.pages;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {


    Faker faker = new Faker();
    String name = faker.name().firstName();
    String userEmail = faker.internet().emailAddress();
    String comment = faker.gameOfThrones().quote();



    SelenideElement
            findButton = $(".icon.header__search.js-search-open"),
            findString = $("#qGxTkUz"),
            responseBlock = $("#response-block"),
            form = $(".row"),
            nameField = $(".field__input.js-field-input"),
            emailField = $(".field__input.js-field-input", 1),
            commentField = $(".field__input.js-field-input", 2),
            checkbox = $(".control__input"),
            submitButton = $(".button.feedback__footer-button.js-feedback-submit"),
            selectDrop= $(".select__drop"),
            container =$("#scrollContainer");

    @Step("Поиск на странице")
    public MainPage findOnMainPage(String value){
        findButton.click();
        findString.setValue(value);
        responseBlock.shouldHave(text(value));
        return this;
    }

    @Step("Заполнение сортировки")
    public MainPage fillSort(String value, String type){
        $(byText(value)).click();
        selectDrop.shouldBe(visible);
        $("[data-text=\""+type+"\"]").parent().click();
        return this;
    }
    @Step("Проверить соответствие сортировки")
    public MainPage checkSort(String value, String type){
        container.shouldHave(text(String.valueOf(value)),text(type));
        return this;
    }

    @Step("Открыть страницу обратной связи")
    public void openFeedbackPage(){
        open("/about/corporate_governance/trust-line/");
    }
    @Step("Скролл до формы")
    public void scrollToForm(){
        form.scrollTo();
        sleep(1500);
    }
    @Step("Ввод имя")
    public void fillName(){
        nameField.setValue(name);
    }
    @Step("Ввод email")
    public void fillEmail(){
        emailField.setValue(userEmail);
    }
    @Step("Ввод комментария")
    public void fillComment(){
        commentField.setValue(comment);
    }
    @Step("Проверяем чекбокс")
    public void checkCheckbox(){
        checkbox.shouldBe(checked);
    }
    @Step("Проверяем кнопку Отправить")
    public void checkSubmit(){
        submitButton.shouldNotHave(attribute("disabled"));

    }





}
