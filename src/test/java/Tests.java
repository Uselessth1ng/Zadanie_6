import com.codeborne.selenide.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Selenide.*;


public class Tests {

    @BeforeEach
    void setup(){
        Configuration.browser = CHROME;
        open("https://the-internet.herokuapp.com");
    }

    @Test
    void testDragNDrop(){
        $x("//a[@href='/drag_and_drop']").click();
        SelenideElement a = $x("//div[@id='column-a']");
        SelenideElement b = $x("//div[@id='column-b']");
        actions().clickAndHold(a).moveToElement(b).release().perform();
        a.should(Condition.text("B"));
    }

    @Test
    void testContextMenu(){
        $x("//a[@href='/context_menu']").click();
        $x("//div[@id='hot-spot']").contextClick();
        Alert alert = switchTo().alert();
        assert alert.getText().contains("You selected a context menu");
    }

    @Test
    void testInfiniteScroll(){
        $x("//a[@href='/infinite_scroll']").click();
        int counter = 0;
        SelenideElement textChunk;
        do{
            counter++;
            textChunk = $x("//div[@class='jscroll-added'][" + counter + "]");
            textChunk.shouldNot(Condition.text("Loading..."));
            $x("//div[@id='page-footer']").scrollIntoView(true);
        }
        while (!textChunk.getText().contains("Eius"));
        textChunk.scrollIntoView(true);
        textChunk.should(Condition.visible);
    }

    @Test
    void testKeyPresses(){
        $x("//a[@href='/key_presses']").click();
        Keys[] keys = {Keys.ENTER, Keys.TAB, Keys.ALT, Keys.CONTROL};
        String[] strKeys = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "ENTER", "TAB", "ALT", "CONTROL"};
        for (int i = 0; i < strKeys.length; i++) {
            actions().sendKeys(i >= 10 ? keys[i - 10] : strKeys[i]).build().perform();
            $x("//p[@id='result']").should(Condition.text("You entered: " + strKeys[i]));
        }
    }

    @AfterEach
    void teardown(){
        closeWebDriver();
    }
}
