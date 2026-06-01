package qase.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.LoginPage;
import pages.ProjectPage;

import java.io.ByteArrayInputStream;

import static com.codeborne.selenide.Selenide.open;

@Epic("Тестирование Qase.io")
@Feature("Управление проектами")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QaseProjectTest {

    private LoginPage loginPage;
    private ProjectPage projectPage;
    private static String projectName;
    private long startTime;

    @BeforeAll
    @Step("🔧 Настройка тестового окружения")
    @Description("Инициализация браузера Chrome в режиме инкогнито и настройка Allure")
    public static void setUpAll() {
        Configuration.browser = "chrome";
        Configuration.timeout = 20000;
        Configuration.headless = false;

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        Configuration.browserCapabilities = options;
        Configuration.browserSize = "1920x1080";

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
        );

        projectName = generateRandomProjectName();
        Allure.addAttachment("📋 Имя проекта", projectName);
    }

    @Step(" Генерация случайного имени проекта (2 латинские буквы)")
    private static String generateRandomProjectName() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            int index = (int) (Math.random() * letters.length());
            name.append(letters.charAt(index));
        }
        return name.toString();
    }

    @BeforeEach
    @Step("Подготовка к выполнению теста")
    @Description("Открытие страницы логина и авторизация через Google")
    public void setUp() {
        startTime = System.currentTimeMillis();
        loginPage = new LoginPage();
        projectPage = new ProjectPage();

        Allure.step("Открыть страницу логина Qase", () -> {
            open("https://app.qase.io/login");
        });

        Allure.step("Выполнить авторизацию через Google аккаунт", () -> {
            loginPage.loginWithGoogle();
        });
    }

    @Test
    @DisplayName(" Создание и удаление проекта в Qase")
    @Description("Тест проверяет полный жизненный цикл проекта: создание, отображение в списке и удаление")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Управление проектами")
    @Tag("smoke")
    @Tag("regression")
    @Owner("QA Team")
    @Link(name = "Qase.io", url = "https://qase.io")
    public void fullProjectLifecycleTest() {

        // ШАГ 1: Создание проекта
        Allure.step(" Шаг 1 - Создание нового проекта", () -> {
            Allure.step("Нажать кнопку 'Create new project'", () -> {
                projectPage.clickCreateProject();
            });
            Allure.step("Ввести название проекта: " + projectName, () -> {
                projectPage.setProjectName(projectName);
            });
            Allure.step("Пропустить ввод кода (автогенерация)", () -> {
                projectPage.skipProjectCode();
            });
            Allure.step("Нажать кнопку 'Create project'", () -> {
                projectPage.submit();
            });
        });

        // ШАГ 2: Проверка создания проекта
        Allure.step("Шаг 2 - Проверка успешного создания проекта", () -> {
            Allure.step("Убедиться, что открылась страница созданного проекта", () -> {
                projectPage.verifyProjectCreated(projectName);
            });
        });

        // ШАГ 3: Переход к списку проектов
        Allure.step(" Шаг 3 - Переход к списку проектов", () -> {
            Allure.step("Нажать на ссылку 'Projects' в навигации", () -> {
                projectPage.navigateToProjectsPage();
            });
        });

        // ШАГ 4: Проверка наличия проекта в таблице
        Allure.step(" Шаг 4 - Проверка отображения проекта в списке", () -> {
            Allure.step("Найти проект '" + projectName + "' в таблице проектов", () -> {
                projectPage.verifyProjectExistsInTable(projectName);
            });
        });

        // ШАГ 5: Удаление проекта
        Allure.step(" Шаг 5 - Удаление проекта", () -> {
            Allure.step("Открыть меню действий проекта (кнопка с тремя точками)", () -> {
                // Этот шаг внутри метода deleteProject
            });
            Allure.step("Выбрать опцию 'Remove'", () -> {});
            Allure.step("Подтвердить удаление кнопкой 'Delete project'", () -> {});
            projectPage.deleteProject(projectName);
        });

        // ШАГ 6: Проверка удаления
        Allure.step(" Шаг 6 - Проверка успешного удаления проекта", () -> {
            Allure.step("Убедиться, что проект отсутствует в таблице", () -> {
                projectPage.verifyProjectNotExistsInTable(projectName);
            });
        });
    }

    @AfterEach
    @Step("Завершение теста и формирование отчёта")
    public void tearDown() {
        // Добавляем скриншот финального состояния
        try {
            byte[] screenshot = Selenide.screenshot(OutputType.BYTES);
            if (screenshot != null && screenshot.length > 0) {
                Allure.addAttachment(" Финальный скриншот", "image/png",
                        new ByteArrayInputStream(screenshot), "png");
            }
        } catch (Exception e) {
            System.out.println("Не удалось сделать скриншот: " + e.getMessage());
        }

        long duration = System.currentTimeMillis() - startTime;
        Allure.addAttachment("Время выполнения теста", String.format("%.2f секунд", duration / 1000.0));

        Allure.addAttachment(" Статистика теста", "text/plain",
                "Имя проекта: " + projectName + "\n" +
                        "Время выполнения: " + duration + " ms\n" +
                        "Статус:  успех!");
    }
}