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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

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
		WebElement attendance = webDriver.findElement(By.linkText("勤怠"));
		attendance.click();
		alertAccept();

		assertEquals("http://localhost:8080/lms/attendance/detail", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		WebElement container = webDriver.findElement(By.id("contents"));
		WebElement update = container.findElement(By.tagName("a"));
		update.click();

		assertEquals("http://localhost:8080/lms/attendance/update", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {
		// 出勤時間 9時のみ入力
		Select startMin = new Select(webDriver.findElement(By.name("attendanceList[0].trainingStartTimeMinute")));
		startMin.selectByIndex(0);
		// 退勤時間 00分のみ入力
		Select endHour = new Select(webDriver.findElement(By.name("attendanceList[0].trainingEndTimeHour")));
		endHour.selectByIndex(0);

		getEvidence(new Object() {
		}, "01");

		scrollBy("window.innerHeight");
		attendanceUpdate();
		alertAccept();
		pageLoadTimeout(5);

		WebElement error = webDriver.findElement(By.className("error"));
		assertEquals("* 出勤時間が正しく入力されていません。", error.getText());
		assertEquals("http://localhost:8080/lms/attendance/update", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {
		// 出勤時間 入力なし
		Select startHour = new Select(webDriver.findElement(By.name("attendanceList[0].trainingStartTimeHour")));
		Select startMin = new Select(webDriver.findElement(By.name("attendanceList[0].trainingStartTimeMinute")));
		startHour.selectByIndex(0);
		startMin.selectByIndex(0);
		// 退勤時間 18:00
		Select endHour = new Select(webDriver.findElement(By.name("attendanceList[0].trainingEndTimeHour")));
		Select endMin = new Select(webDriver.findElement(By.name("attendanceList[0].trainingEndTimeMinute")));
		endHour.selectByIndex(19);
		endMin.selectByIndex(1);

		getEvidence(new Object() {
		}, "01");

		scrollBy("window.innerHeight");
		attendanceUpdate();
		alertAccept();
		pageLoadTimeout(5);

		WebElement error = webDriver.findElement(By.className("error"));
		assertEquals("* 出勤情報がないため退勤情報を入力出来ません。", error.getText());
		assertEquals("http://localhost:8080/lms/attendance/update", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		// 出勤時間 14:00
		Select startHour = new Select(webDriver.findElement(By.name("attendanceList[0].trainingStartTimeHour")));
		Select startMin = new Select(webDriver.findElement(By.name("attendanceList[0].trainingStartTimeMinute")));
		startHour.selectByIndex(15);
		startMin.selectByIndex(1);
		// 退勤時間 10:00
		Select endHour = new Select(webDriver.findElement(By.name("attendanceList[0].trainingEndTimeHour")));
		Select endMin = new Select(webDriver.findElement(By.name("attendanceList[0].trainingEndTimeMinute")));
		endHour.selectByIndex(11);
		endMin.selectByIndex(1);

		getEvidence(new Object() {
		}, "01");

		scrollBy("window.innerHeight");
		attendanceUpdate();
		alertAccept();
		pageLoadTimeout(5);

		WebElement error = webDriver.findElement(By.className("error"));
		assertEquals("* 退勤時刻[0]は出勤時刻[0]より後でなければいけません。", error.getText());
		assertEquals("http://localhost:8080/lms/attendance/update", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		// 出勤時間 9:00
		Select startHour = new Select(webDriver.findElement(By.name("attendanceList[0].trainingStartTimeHour")));
		Select startMin = new Select(webDriver.findElement(By.name("attendanceList[0].trainingStartTimeMinute")));
		startHour.selectByIndex(10);
		startMin.selectByIndex(1);
		// 退勤時間 14:00
		Select endHour = new Select(webDriver.findElement(By.name("attendanceList[0].trainingEndTimeHour")));
		Select endMin = new Select(webDriver.findElement(By.name("attendanceList[0].trainingEndTimeMinute")));
		endHour.selectByIndex(15);
		endMin.selectByIndex(1);
		// 中抜け時間 7時間
		Select blankTime = new Select(webDriver.findElement(By.name("attendanceList[0].blankTime")));
		blankTime.selectByValue("420");

		getEvidence(new Object() {
		}, "01");

		scrollBy("window.innerHeight");
		attendanceUpdate();
		alertAccept();
		pageLoadTimeout(5);

		WebElement error = webDriver.findElement(By.className("error"));
		assertEquals("* 中抜け時間が勤務時間を超えています。", error.getText());
		assertEquals("http://localhost:8080/lms/attendance/update", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		WebElement note = webDriver.findElement(By.name("attendanceList[0].note"));
		note.sendKeys("あいうえお　あいうえお　あいうえお　あいうえお　あいうえお　あいうえお　あいうえお　あいうえお　"
				+ "あいうえお　あいうえお　あいうえお　あいうえお　あいうえお　あいうえお　あいうえお　あいうえお　あいうえお");
		getEvidence(new Object() {
		}, "01");

		scrollBy("window.innerHeight");
		attendanceUpdate();
		alertAccept();
		pageLoadTimeout(5);

		WebElement error = webDriver.findElement(By.className("error"));
		assertEquals("* 備考の長さが最大値(100)を超えています。", error.getText());
		assertEquals("http://localhost:8080/lms/attendance/update", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "02");
	}

}
