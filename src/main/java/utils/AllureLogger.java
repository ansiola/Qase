package utils;

import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;

public class AllureLogger {

    public static void log(String message) {
        System.out.println(message);
        Allure.addAttachment("Лог", "text/plain", message);
    }

    public static void logStep(String stepName, String details) {
        String message = "[" + stepName + "] " + details;
        System.out.println(message);
        Allure.addAttachment("Шаг: " + stepName, "text/plain", message);
    }

    public static void attachScreenshot(byte[] screenshot, String name) {
        // Правильный метод для добавления скриншота
        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }
}