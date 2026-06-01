package pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class LoginPage {

    private static final String CONTINUE_WITH_GOOGLE_BUTTON = "a[href*='/oauth/link/google']";
    private GoogleLoginPage googleLoginPage;

    public LoginPage() {
        this.googleLoginPage = new GoogleLoginPage();
    }

    @Step("Нажать кнопку 'Continue with Google'")
    private void clickGoogleButton() {
        $(By.cssSelector(CONTINUE_WITH_GOOGLE_BUTTON))
                .shouldBe(Condition.visible, Condition.enabled)
                .click();
        sleep(2000);
    }

    @Step("Выполнить вход через Google аккаунт")
    public void loginWithGoogle() {
        String email = utils.ConfigReader.getGoogleLogin();
        String password = utils.ConfigReader.getGooglePassword();

        clickGoogleButton();
        googleLoginPage.loginToGoogle(email, password);
        sleep(5000);
    }
}