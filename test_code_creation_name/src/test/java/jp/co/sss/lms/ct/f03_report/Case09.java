package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
		getEvidence(new Object() {
		});
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
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		WebElement navbar = webDriver.findElement(By.className("navbar-right"));
		WebElement myPage = navbar.findElement(By.partialLinkText("ようこそ"));
		myPage.click();

		pageLoadTimeout(5);
		assertEquals("ユーザー詳細", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		scrollBy("200");
		WebElement detail = webDriver.findElement(By.xpath("//input[@value='修正する']"));
		detail.click();

		pageLoadTimeout(5);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		WebElement genre = webDriver.findElement(By.id("intFieldName_0"));
		genre.clear();

		pageLoadTimeout(5);
		assertEquals("", genre.getAttribute("value"));
		getEvidence(new Object() {
		}, "01");

		scrollHeight();
		// 「提出する」ボタンを押下
		WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		WebElement error = webDriver.findElement(By.className("error"));
		WebElement span = error.findElement(By.xpath("//span[text()='* 理解度を入力した場合は、学習項目は必須です。']"));
		// エラーメッセージのアサーションメソッドを打鍵する
		assertEquals("* 理解度を入力した場合は、学習項目は必須です。", span.getText());
		assertEquals("http://localhost:8080/lms/report/complete", webDriver.getCurrentUrl());
		pageLoadTimeout(5);
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		// 学習項目を再入力後、理解度を未入力に
		WebElement genre = webDriver.findElement(By.id("intFieldName_0"));
		genre.sendKeys("自動テスト入力チェック");
		Select feildValue = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		feildValue.selectByIndex(0);

		getEvidence(new Object() {
		}, "01");

		scrollHeight();
		WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		WebElement error = webDriver.findElement(By.className("error"));
		WebElement span = error.findElement(By.xpath("//span[text()='* 学習項目を入力した場合は、理解度は必須です。']"));

		assertEquals("* 学習項目を入力した場合は、理解度は必須です。", span.getText());
		assertEquals("http://localhost:8080/lms/report/complete", webDriver.getCurrentUrl());
		pageLoadTimeout(5);
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		Select feildValue = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		feildValue.selectByIndex(3);

		// 「目標の達成度」を「理解できた。」に設定
		WebElement goal = webDriver.findElement(By.id("content_0"));
		goal.clear();
		goal.sendKeys("理解できた。");

		getEvidence(new Object() {
		}, "01");

		scrollHeight();
		WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		WebElement error = webDriver.findElement(By.className("error"));
		WebElement span = error.findElement(By.xpath("//span[text()='* 目標の達成度は半角数字で入力してください。']"));

		assertEquals("* 目標の達成度は半角数字で入力してください。", span.getText());
		assertEquals("http://localhost:8080/lms/report/complete", webDriver.getCurrentUrl());
		pageLoadTimeout(5);
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		// 「目標の達成度」を「11」に設定
		WebElement goal = webDriver.findElement(By.id("content_0"));
		goal.clear();
		goal.sendKeys("11");

		getEvidence(new Object() {
		}, "01");

		scrollHeight();
		WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		WebElement error = webDriver.findElement(By.className("error"));
		WebElement span = error.findElement(By.xpath("//span[text()='* 目標の達成度は、半角数字で、1～10の範囲内で入力してください。']"));

		assertEquals("* 目標の達成度は、半角数字で、1～10の範囲内で入力してください。", span.getText());
		assertEquals("http://localhost:8080/lms/report/complete", webDriver.getCurrentUrl());
		pageLoadTimeout(5);
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		// 「目標の達成度」を未入力に設定
		WebElement goal = webDriver.findElement(By.id("content_0"));
		goal.clear();
		// 「所感」を未入力に設定
		WebElement feeling = webDriver.findElement(By.id("content_1"));
		feeling.clear();

		pageLoadTimeout(5);
		assertEquals("", goal.getAttribute("value"));
		assertEquals("", feeling.getAttribute("value"));
		getEvidence(new Object() {
		}, "01");

		scrollHeight();
		WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		assertEquals("http://localhost:8080/lms/report/complete", webDriver.getCurrentUrl());
		scrollBy("200");
		pageLoadTimeout(5);
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		scrollHeight();
		// 「目標の達成度」を「10」に設定
		WebElement goal = webDriver.findElement(By.id("content_0"));
		goal.clear();
		goal.sendKeys("10");

		// 「所感」と「一週間の振り返り」に2010字を設定
		WebElement feeling = webDriver.findElement(By.id("content_1"));
		WebElement weekReview = webDriver.findElement(By.id("content_2"));
		weekReview.clear();

		// 入力値セット
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 2010; i++) {
			sb.append("あいうえお");
		}

		pageLoadTimeout(60);
		feeling.sendKeys(sb);
		weekReview.sendKeys(sb);
		getEvidence(new Object() {
		}, "01");

		WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		assertEquals("http://localhost:8080/lms/report/complete", webDriver.getCurrentUrl());
		scrollHeight();
		pageLoadTimeout(5);
		getEvidence(new Object() {
		}, "02");
	}

}
