package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", webDriver.getTitle());
		//		getEvidence(new Object() {
		//		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		WebElement loginId = webDriver.findElement(By.name("loginId"));
		WebElement password = webDriver.findElement(By.name("password"));

		loginId.clear();
		loginId.sendKeys("StudentAA03");
		password.clear();
		password.sendKeys("StudentAA0303");

		WebElement loginButton = webDriver.findElement(By.className("btn-primary"));
		loginButton.click();

		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());
		//		getEvidence(new Object() {
		//		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		WebElement dropdown = webDriver.findElement(By.className("dropdown"));
		dropdown.click();
		WebElement help = webDriver.findElement(By.linkText("ヘルプ"));
		help.click();

		assertEquals("http://localhost:8080/lms/help", webDriver.getCurrentUrl());
		//		getEvidence(new Object() {
		//		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		WebElement faq = webDriver.findElement(By.linkText("よくある質問"));
		faq.click();

		Set<String> windowHandles = webDriver.getWindowHandles();
		assertTrue(windowHandles.size() > 1);

		Iterator<String> iterator = windowHandles.iterator();
		while (iterator.hasNext()) {
			String faqWindow = iterator.next();
			if (!faqWindow.equals(windowHandles)) {
				webDriver.switchTo().window(faqWindow);
			}
		}
		assertEquals("http://localhost:8080/lms/faq", webDriver.getCurrentUrl());
		//		getEvidence(new Object() {
		//		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() throws IOException {
		WebElement category = webDriver.findElement(By.linkText("【研修関係】"));
		category.click();

		scrollBy("window.innerHeight");
		pageLoadTimeout(5);

		// 検索結果の確認
		WebElement result = webDriver.findElement(By.className("mb10"));
		assertEquals("Q.キャンセル料・途中退校について", result.getText());

		// エビデンスを取得する
		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("C:\\work\\ScreenShot\\result.png"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() throws IOException {
		WebElement question = webDriver.findElement(By.className("sorting_1"));
		question.click();

		WebElement answer = webDriver.findElement(By.id("answer-h[${status.index}]"));
		String answerTest = "A. 受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、"
				+ "事情をお伺いした上で、協議という形を取らせて頂きます。 弊社営業担当までご相談下さい。";
		assertEquals(answerTest, answer.getText());

		// エビデンスを取得する
		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File("C:\\work\\ScreenShot\\faq.png"));
	}

}
