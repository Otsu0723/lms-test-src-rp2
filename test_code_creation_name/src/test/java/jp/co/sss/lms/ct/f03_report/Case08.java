package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// 週報提出済みの研修日を取得
		WebElement table = webDriver.findElement(By.className("sctionList"));
		List<WebElement> tableList = table.findElements(By.tagName("td"));
		WebElement report = tableList.get(7);
		String reportStatus = report.getText();

		if (reportStatus.equals("提出済み")) {
			// 取得した研修日の「詳細」ボタンを押下
			tableList.get(9).click();
			scrollBy("window.innerHeight");
			pageLoadTimeout(5);

			assertEquals("http://localhost:8080/lms/section/detail", webDriver.getCurrentUrl());
			WebElement weekReport = webDriver.findElement(By.xpath("//input[@value='提出済み週報【デモ】を確認する']"));
			assertEquals("提出済み週報【デモ】を確認する", weekReport.getAttribute("value"));
			getEvidence(new Object() {
			});
		}
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		WebElement weekReport = webDriver.findElement(By.xpath("//input[@value='提出済み週報【デモ】を確認する']"));
		weekReport.click();
		assertEquals("http://localhost:8080/lms/report/regist", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		scrollBy("200");
		List<WebElement> formList = webDriver.findElements(By.className("form-control"));

		getEvidence(new Object() {
		}, "01");
		// (1):所感記入欄 (2):振り返り記入欄
		WebElement feeling = formList.get(3);
		WebElement review = formList.get(4);

		// 修正内容を記入
		feeling.clear();
		review.clear();
		getEvidence(new Object() {
		}, "02");

		feeling.sendKeys("テストケースNo.08週報編集テスト");
		review.sendKeys("テストケースNo.08週報一週間の振り返り編集テスト");

		pageLoadTimeout(5);
		getEvidence(new Object() {
		}, "03");

		scrollBy("200");
		// 「提出する」ボタンを押下後、セクション詳細画面に遷移
		WebElement button = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		button.click();

		pageLoadTimeout(5);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		}, "04");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		WebElement navbar = webDriver.findElement(By.className("navbar-right"));
		WebElement myPage = navbar.findElement(By.partialLinkText("ようこそ"));
		myPage.click();

		scrollBy("200");
		pageLoadTimeout(5);
		assertEquals("ユーザー詳細", webDriver.getTitle());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		WebElement detail = webDriver.findElement(By.xpath("//input[@value='詳細']"));
		detail.click();
		pageLoadTimeout(5);
		assertEquals("レポート詳細 | LMS", webDriver.getTitle());

		// テーブルのリストを取得
		List<WebElement> tableList = webDriver.findElements(By.tagName("table"));
		WebElement reportDetail = tableList.get(2);

		// tableList(2)の中のtdタグをリスト化
		List<WebElement> tdList = reportDetail.findElements(By.tagName("td"));
		// 所感記入欄
		WebElement tdFeeling = tdList.get(1);
		// 振り返り記入欄
		WebElement tdReview = tdList.get(2);

		assertEquals("テストケースNo.08週報編集テスト", tdFeeling.getText());
		assertEquals("テストケースNo.08週報一週間の振り返り編集テスト", tdReview.getText());
		getEvidence(new Object() {
		});
	}

}
