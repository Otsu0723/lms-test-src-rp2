package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		// ナビゲーションバーから勤怠管理画面に遷移
		WebElement attendance = webDriver.findElement(By.linkText("勤怠"));
		attendance.click();

		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		assertEquals("http://localhost:8080/lms/attendance/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {
		// 出勤ボタンを押下
		WebElement punchIn = webDriver.findElement(By.name("punchIn"));
		punchIn.click();

		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		WebElement comp = webDriver.findElement(By.className("alert-dismissible"));
		assertEquals("×\n勤怠情報の登録が完了しました。", comp.getText());

		// 出勤完了メッセージのエビデンス取得
		getEvidence(new Object() {
		}, "01");

		//WebElement tbody = webDriver.findElement(By.xpath("//tbody"));

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {
		// 退勤ボタンを押下
		WebElement punchOut = webDriver.findElement(By.name("punchOut"));
		punchOut.click();

		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		WebElement comp = webDriver.findElement(By.className("alert-dismissible"));
		assertEquals("×\n勤怠情報の登録が完了しました。", comp.getText());

		// 退勤完了メッセージのエビデンス取得
		getEvidence(new Object() {
		}, "01");
	}

}
