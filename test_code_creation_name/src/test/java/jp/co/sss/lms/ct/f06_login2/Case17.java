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
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

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
		loginId.sendKeys("StudentAA08");
		password.clear();
		password.sendKeys("StudentAA08");

		WebElement loginButton = webDriver.findElement(By.className("btn-primary"));
		loginButton.click();

		assertEquals("http://localhost:8080/lms/user/agreeSecurity", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		// 利用規約に同意処理
		WebElement div = webDriver.findElement(By.className("checkbox"));
		WebElement check = div.findElement(By.xpath("//input[@value='1']"));
		check.click();

		pageLoadTimeout(5);
		getEvidence(new Object() {
		}, "01");

		WebElement next = webDriver.findElement(By.xpath("//button[text()='次へ']"));
		next.click();

		assertEquals("パスワード変更 | LMS", webDriver.getTitle());
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() {
		// 各項目に値を設定
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA08");

		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("Testtest08");

		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("Testtest08");

		getEvidence(new Object() {
		}, "01");

		WebElement update = webDriver.findElement(By.xpath("//button[text()='変更']"));
		update.click();
		pageLoadTimeout(60);

		// モーダル「変更」ボタンを押下
		visibilityTimeout(By.className("modal-footer"), 60);
		WebElement modalUpdate = webDriver.findElement(By.className("modal-footer"));
		WebElement modalButton = modalUpdate.findElement(By.xpath("//button[@id='upd-btn']"));
		modalButton.click();

		pageLoadTimeout(30);
		assertEquals("http://localhost:8080/lms/course/detail", webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "02");
	}

}
