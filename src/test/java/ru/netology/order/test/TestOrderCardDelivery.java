package ru.netology.order.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.order.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestOrderCardDelivery {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $(".notification__title").
                shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(exactText("Успешно!"));
        $(".notification__content").
                shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id=date] input").sendKeys(Keys.SHIFT, Keys.HOME, Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $("button.button").click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(Condition.visible, Duration.ofSeconds(10));
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification] .notification__content").
                shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
