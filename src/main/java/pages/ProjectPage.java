package pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.url;

public class ProjectPage {

    private static final String CREATE_PROJECT_BUTTON = "//button//span[text()='Create new project']";
    private static final String PROJECT_NAME_INPUT = "#project-name";
    private static final String CREATE_PROJECT_SUBMIT = "//span[text()='Create project']";
    private static final String PROJECTS_PAGE_URL = "https://app.qase.io/projects";
    private static final String PROJECT_IN_TABLE = "//tbody//a[contains(text(),'%s')]";
    private static final String ACTION_MENU_BUTTON = "//button[@aria-label='Open action menu']";
    private static final String REMOVE_OPTION = "//div[@data-testid='remove']";
    private static final String DELETE_PROJECT_CONFIRM = "//span[text()='Delete project']";
    private static final String PROJECT_ROW = "//tbody//tr[.//a[contains(text(),'%s')]]";

    @Step("Нажать кнопку 'Create new project'")
    public void clickCreateProject() {
        sleep(3000);
        $(By.xpath(CREATE_PROJECT_BUTTON))
                .shouldBe(Condition.visible, Condition.enabled)
                .click();
        sleep(2000);
    }

    @Step("Ввести название проекта: {name}")
    public void setProjectName(String name) {
        $(By.cssSelector(PROJECT_NAME_INPUT))
                .shouldBe(Condition.visible)
                .setValue(name);
        sleep(500);
    }

    @Step("Пропустить ввод кода проекта (будет сгенерирован автоматически)")
    public void skipProjectCode() {
        sleep(500);
    }

    @Step("Нажать кнопку 'Create project' для сохранения")
    public void submit() {
        $(By.xpath(CREATE_PROJECT_SUBMIT))
                .shouldBe(Condition.visible, Condition.enabled)
                .click();
        sleep(3000);
    }

    @Step("Перейти на страницу 'Projects'")
    public void navigateToProjectsPage() {
        open(PROJECTS_PAGE_URL);
        sleep(3000);
    }

    @Step("Проверить, что проект '{projectName}' отображается в таблице")
    public void verifyProjectExistsInTable(String projectName) {
        String projectXpath = String.format(PROJECT_IN_TABLE, projectName);
        $(By.xpath(projectXpath)).shouldBe(Condition.visible);
    }

    @Step("Проверить, что проект '{projectName}' удалён из таблицы")
    public void verifyProjectNotExistsInTable(String projectName) {
        String projectXpath = String.format(PROJECT_IN_TABLE, projectName);
        $(By.xpath(projectXpath)).shouldNotBe(Condition.visible);
    }

    @Step("Удалить проект '{projectName}'")
    public void deleteProject(String projectName) {
        String projectRowXpath = String.format(PROJECT_ROW, projectName);

        $(By.xpath(projectRowXpath + ACTION_MENU_BUTTON)).click();
        sleep(1000);

        $(By.xpath(REMOVE_OPTION)).click();
        sleep(1000);

        $(By.xpath(DELETE_PROJECT_CONFIRM)).click();
        sleep(3000);
    }

    @Step("Проверить, что проект '{expectedTitle}' успешно создан")
    public void verifyProjectCreated(String expectedTitle) {
        $(By.xpath("//h1[contains(text(),'" + expectedTitle + "')]"))
                .shouldBe(Condition.visible);
    }

    @Step("Создать новый проект с именем '{projectName}'")
    public void createProject(String projectName) {
        clickCreateProject();
        setProjectName(projectName);
        skipProjectCode();
        submit();
    }
}