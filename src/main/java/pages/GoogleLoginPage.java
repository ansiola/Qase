package pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.url;

public class GoogleLoginPage {

    @Step("Ожидание загрузки страницы входа Google")
    public void waitForGooglePage() {
        sleep(3000);
        System.out.println("Страница Google загружена. URL: " + url());
    }

    @Step("Ввод email: {email}")
    public void enterEmail(String email) {
        System.out.println("Ввод email: " + email);
        $(By.id("identifierId"))
                .shouldBe(Condition.visible, Condition.enabled)
                .setValue(email);
        sleep(1000);
    }

    @Step("Нажатие кнопки 'Далее' (после ввода email)")
    public void clickNextButtonAfterEmail() {
        System.out.println("Нажатие кнопки 'Далее'");
        $(By.xpath("//span[text()='Далее']"))
                .shouldBe(Condition.visible, Condition.enabled)
                .click();
        sleep(2000);
    }

    @Step("Ввод пароля")
    public void enterPassword() {
        System.out.println("Ввод пароля");
        $(By.xpath("//input[@type='password']"))
                .shouldBe(Condition.visible, Condition.enabled)
                .setValue(utils.ConfigReader.getGooglePassword());
        sleep(1000);
    }

    @Step("Нажатие кнопки 'Далее' (после ввода пароля)")
    public void clickNextButtonAfterPassword() {
        System.out.println("Нажатие кнопки 'Далее' для подтверждения входа");
        $(By.xpath("//span[text()='Далее']"))
                .shouldBe(Condition.visible, Condition.enabled)
                .click();
        sleep(3000);
    }

    @Step("Ожидание редиректа обратно в Qase")
    public void waitForRedirectToQase() {
        int attempts = 0;
        while (attempts < 10 && !url().contains("qase.io")) {
            sleep(1000);
            attempts++;
        }
        System.out.println("Редирект в Qase выполнен. URL: " + url());
    }

    @Step("Полный процесс входа в Google")
    public void loginToGoogle(String email, String password) {
        waitForGooglePage();
        enterEmail(email);
        clickNextButtonAfterEmail();
        enterPassword();
        clickNextButtonAfterPassword();
        waitForRedirectToQase();
    }
}