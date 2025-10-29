package jp.co.sss.lms.ct.f06_login2;

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

/**
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		WebElement loginId = webDriver.findElement(By.name("loginId"));
		WebElement password = webDriver.findElement(By.name("password"));

		loginId.clear();
		loginId.sendKeys("StudentAA05");
		password.clear();
		password.sendKeys("StudentAA05");

		WebElement loginButton = webDriver.findElement(By.className("btn-primary"));
		loginButton.click();

		//		assertEquals("http://localhost:8080/lms/user/agreeSecurity", webDriver.getCurrentUrl());
		//		getEvidence(new Object() {
		//		});
	}

	//	@Test
	//	@Order(3)
	//	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	//	void test03() {
	//		WebElement div = webDriver.findElement(By.className("checkbox"));
	//		WebElement check = div.findElement(By.xpath("//input[@value='1']"));
	//		check.click();
	//
	//		pageLoadTimeout(5);
	//		getEvidence(new Object() {
	//		}, "01");
	//		WebElement next = webDriver.findElement(By.xpath("//button[text()='次へ']"));
	//		next.click();
	//
	//		assertEquals("パスワード変更 | LMS", webDriver.getTitle());
	//		getEvidence(new Object() {
	//		}, "02");
	//	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {
		// 現在のパスワードを未入力に設定
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		currentPassword.clear();
		WebElement password = webDriver.findElement(By.id("password"));
		password.sendKeys("StudentAA0505");
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		passwordConfirm.sendKeys("StudentAA0505");

		getEvidence(new Object() {
		}, "01_01");

		WebElement update = webDriver.findElement(By.xpath("//button[text()='変更']"));
		update.click();
		pageLoadTimeout(60);

		// モーダル「変更」ボタンを押下
		visibilityTimeout(By.className("modal-footer"), 60);
		WebElement modalUpdate = webDriver.findElement(By.className("modal-footer"));
		WebElement modalButton = modalUpdate.findElement(By.xpath("//button[@id='upd-btn']"));
		modalButton.click();

		pageLoadTimeout(30);
		assertEquals("http://localhost:8080/lms/password/changePassword/change", webDriver.getCurrentUrl());

		WebElement error1 = webDriver.findElement(By.xpath("//span[text()='現在のパスワードは必須です。']"));
		assertEquals("現在のパスワードは必須です。", error1.getText());

		getEvidence(new Object() {
		}, "01_02");

		WebElement back = webDriver.findElement(By.xpath("//button[text()='戻る']"));
		back.click();
		// モーダル「キャンセル」ボタンを押下
		visibilityTimeout(By.className("modal-footer"), 60);
		WebElement modalCancelButton = modalUpdate.findElement(By.xpath("//button[text()='キャンセル']"));
		modalCancelButton.click();
		pageLoadTimeout(30);

		// 新しいパスワードを未入力に設定
		currentPassword.sendKeys("StudentAA05");
		password.clear();
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA0505");

		getEvidence(new Object() {
		}, "02_01");

		update.click();
		pageLoadTimeout(60);
		assertEquals("http://localhost:8080/lms/password/changePassword/change", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "02_02");

		scrollBy("200");
		WebElement reback = webDriver.findElement(By.xpath("//button[text()='戻る']"));
		reback.click();
		pageLoadTimeout(30);

		// 確認パスワードを未入力に設定
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA05");
		password.clear();
		password.sendKeys("StudentAA0505");
		passwordConfirm.clear();

		getEvidence(new Object() {
		}, "03_01");

		update.click();
		pageLoadTimeout(60);
		assertEquals("http://localhost:8080/lms/password/changePassword/change", webDriver.getCurrentUrl());

		WebElement error3 = webDriver.findElement(By.xpath("//span[text()='確認パスワードは必須です。']"));
		assertEquals("確認パスワードは必須です。", error3.getText());
		getEvidence(new Object() {
		}, "03_02");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {
		// TODO ここに追加
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {
		// TODO ここに追加 
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() {
		// TODO ここに追加
	}

}
