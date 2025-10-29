package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
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
 * 結合テスト 試験実施機能
 * ケース14
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果50点")
public class Case14 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// 「試験有」の研修日を取得
		WebElement table = webDriver.findElement(By.className("sctionList"));
		List<WebElement> tableList = table.findElements(By.tagName("td"));
		WebElement exam = tableList.get(8);
		String examStatus = exam.getText();

		if ("試験有".equals(examStatus)) {
			// 取得した研修日の「詳細」ボタンを押下
			tableList.get(9).click();
			pageLoadTimeout(5);

			assertEquals("http://localhost:8080/lms/section/detail", webDriver.getCurrentUrl());
			getEvidence(new Object() {
			});
		}
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {
		WebElement button = webDriver.findElement(By.xpath("//input[@value='詳細']"));
		button.click();

		pageLoadTimeout(5);
		assertEquals("http://localhost:8080/lms/exam/start", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {
		WebElement button = webDriver.findElement(By.xpath("//input[@value='試験を開始する']"));
		button.click();

		pageLoadTimeout(5);
		assertEquals("http://localhost:8080/lms/exam/question", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 正答と誤答が半々で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() {
		// ラジオボタンクリック処理
		// 誤答
		WebElement radio1 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-0-0']"));
		radio1.click();
		scrollBy("200");
		WebElement radio2 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-1-0']"));
		radio2.click();
		scrollBy("450");
		// 正答
		WebElement radio3 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-2-0']"));
		radio3.click();
		scrollBy("500");
		WebElement radio4 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-3-0']"));
		radio4.click();
		scrollBy("550");

		WebElement radio5 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-4-1']"));
		radio5.click();
		scrollBy("200");
		WebElement radio6 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-5-1']"));
		radio6.click();
		scrollBy("150");

		// 誤答
		WebElement radio7 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-6-0']"));
		radio7.click();
		scrollBy("450");
		WebElement radio8 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-7-0']"));
		radio8.click();
		scrollBy("500");

		// 正答
		WebElement radio9 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-8-1']"));
		radio9.click();
		scrollBy("200");
		WebElement radio10 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-9-1']"));
		radio10.click();
		scrollBy("300");

		// 誤答
		WebElement radio11 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-10-0']"));
		radio11.click();
		scrollBy("400");
		WebElement radio12 = webDriver.findElement(By.xpath("//div/ul/li/input[@id='answer-11-0']"));
		radio12.click();
		scrollHeight();

		WebElement div = webDriver.findElement(By.className("container"));
		WebElement button = div.findElement(By.xpath("//input[@value='確認画面へ進む']"));

		pageLoadTimeout(10);
		visibilityTimeout((By.xpath("//input[@value='確認画面へ進む']")), 20);
		button.click();

		pageLoadTimeout(5);
		assertEquals("http://localhost:8080/lms/exam/answerCheck", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {
		scrollHeight();
		pageLoadTimeout(20);
		WebElement button = webDriver.findElement(By.id("sendButton"));
		button.click();
		alertAccept();

		pageLoadTimeout(5);
		WebElement myScore = webDriver.findElement(By.xpath("//h2/small[text()='あなたのスコア：50.0点']"));
		System.out.println(myScore.getText());
		assertEquals("あなたのスコア：50.0点", myScore.getText());
		assertEquals("http://localhost:8080/lms/exam/result", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() {
		scrollHeight();
		WebElement button = webDriver.findElement(By.xpath("//input[@value='戻る']"));
		button.click();

		pageLoadTimeout(5);
		assertEquals("http://localhost:8080/lms/exam/start", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

}
