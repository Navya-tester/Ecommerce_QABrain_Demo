package testcase;



	
	

	import org.testng.Assert;
	import org.testng.annotations.DataProvider;
	import org.testng.annotations.Test;
	import base.BaseUtilis;
	import pageobjects.LoginPage;

	public class LoginTest extends BaseUtilis {

	    // ── Valid credentials confirmed from site screenshot ───────────
	    // test@qabrains.com     / Password123
	    // practice@qabrains.com / Password123
	    // student@qabrains.com  / Password123

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_01 — Valid user 1: test@qabrains.com
	    // ═══════════════════════════════════════════════════════════════
	    @Test(priority = 1, description = "Valid login with test@qabrains.com")
	    public void validLogin_TestUser() {
	        LoginPage lp = new LoginPage(driver);
	        lp.login("test@qabrains.com", "Password123");

	        Assert.assertTrue(lp.isLoginSuccessful(),
	            "TC_LOGIN_01 FAILED — valid user test@qabrains.com could not login"
	        );
	        System.out.println(" TC_LOGIN_01 PASSED — test@qabrains.com login successful");
	    }

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_02 — Valid user 2: practice@qabrains.com
	    // ═══════════════════════════════════════════════════════════════
	    @Test(priority = 2, description = "Valid login with practice@qabrains.com")
	    public void validLogin_PracticeUser() {
	        LoginPage lp = new LoginPage(driver);
	        lp.login("practice@qabrains.com", "Password123");

	        Assert.assertTrue(lp.isLoginSuccessful(),
	            "TC_LOGIN_02 FAILED — valid user practice@qabrains.com could not login"
	        );
	        System.out.println(" TC_LOGIN_02 PASSED — practice@qabrains.com login successful");
	    }

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_03 — Valid user 3: student@qabrains.com
	    // ═══════════════════════════════════════════════════════════════
	    @Test(priority = 3, description = "Valid login with student@qabrains.com")
	    public void validLogin_StudentUser() {
	        LoginPage lp = new LoginPage(driver);
	        lp.login("student@qabrains.com", "Password123");

	        Assert.assertTrue(lp.isLoginSuccessful(),
	            "TC_LOGIN_03 FAILED — valid user student@qabrains.com could not login"
	        );
	        System.out.println(" TC_LOGIN_03 PASSED — student@qabrains.com login successful");
	    }

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_04 — Invalid email, valid password
	    // ═══════════════════════════════════════════════════════════════
	    @Test(priority = 4, description = "Invalid email with valid password should fail")
	    public void invalidEmail_ValidPassword() {
	        LoginPage lp = new LoginPage(driver);
	        lp.login("invalid@qabrains.com", "Password123");

	        Assert.assertTrue(lp.isErrorDisplayed(),
	            "TC_LOGIN_04 FAILED — invalid email should not be allowed to login"
	        );
	        Assert.assertFalse(lp.isLoginSuccessful(),
	            "TC_LOGIN_04 FAILED — invalid email should not reach products page"
	        );
	        System.out.println(" TC_LOGIN_04 PASSED — invalid email correctly rejected");
	    }

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_05 — Valid email, invalid password
	    // ═══════════════════════════════════════════════════════════════
	    @Test(priority = 5, description = "Valid email with wrong password should fail")
	    public void validEmail_InvalidPassword() {
	        LoginPage lp = new LoginPage(driver);
	        lp.login("test@qabrains.com", "WrongPassword999");

	        Assert.assertTrue(lp.isErrorDisplayed(),
	            "TC_LOGIN_05 FAILED — wrong password should not be allowed to login"
	        );
	        Assert.assertFalse(lp.isLoginSuccessful(),
	            "TC_LOGIN_05 FAILED — wrong password should not reach products page"
	        );
	        System.out.println(" TC_LOGIN_05 PASSED — invalid password correctly rejected");
	    }

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_06 — Both email and password invalid
	    // ═══════════════════════════════════════════════════════════════
	    @Test(priority = 6, description = "Invalid email and invalid password should fail")
	    public void invalidEmail_InvalidPassword() {
	        LoginPage lp = new LoginPage(driver);
	        lp.login("fake@fake.com", "FakePassword123");

	        Assert.assertTrue(lp.isErrorDisplayed(),
	            "TC_LOGIN_06 FAILED — invalid credentials should not be allowed to login"
	        );
	        Assert.assertFalse(lp.isLoginSuccessful(),
	            "TC_LOGIN_06 FAILED — invalid credentials should not reach products page"
	        );
	        System.out.println(" TC_LOGIN_06 PASSED — invalid credentials correctly rejected");
	    }

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_07 — Empty email and password
	    // ═══════════════════════════════════════════════════════════════
	    @Test(priority = 7, description = "Empty fields should not allow login")
	    public void emptyFields() {
	        LoginPage lp = new LoginPage(driver);
	        lp.login("", "");

	        boolean stillOnLogin = driver.getCurrentUrl().contains("/login");
	        Assert.assertTrue(stillOnLogin,
	            "TC_LOGIN_07 FAILED — empty fields should keep user on login page"
	        );
	        System.out.println(" TC_LOGIN_07 PASSED — empty fields correctly blocked");
	    }

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_08 — Valid email, empty password
	    // ═══════════════════════════════════════════════════════════════
	    @Test(priority = 8, description = "Valid email with empty password should fail")
	    public void validEmail_EmptyPassword() {
	        LoginPage lp = new LoginPage(driver);
	        lp.login("test@qabrains.com", "");

	        boolean stillOnLogin = driver.getCurrentUrl().contains("/login");
	        Assert.assertTrue(stillOnLogin,
	            "TC_LOGIN_08 FAILED — empty password should keep user on login page"
	        );
	        System.out.println(" TC_LOGIN_08 PASSED — empty password correctly blocked");
	    }

	    // ═══════════════════════════════════════════════════════════════
	    // TC_LOGIN_09 — Data-driven: multiple credentials at once
	    // ═══════════════════════════════════════════════════════════════
	    @DataProvider(name = "loginData")
	    public Object[][] loginData() {
	        return new Object[][] {
	            // { email, password, shouldPass, testCaseName }
	            { "test@qabrains.com",     "Password123",     true,  "Valid user 1"         },
	            { "practice@qabrains.com", "Password123",     true,  "Valid user 2"         },
	            { "student@qabrains.com",  "Password123",     true,  "Valid user 3"         },
	            { "invalid@qabrains.com",  "Password123",     false, "Invalid email"        },
	            { "test@qabrains.com",     "WrongPass999",    false, "Wrong password"       },
	            { "fake@fake.com",         "FakePass123",     false, "Both invalid"         },
	            { "",                      "",                false, "Empty fields"         },
	            { "test@qabrains.com",     "",                false, "Empty password"       },
	            { "notanemail",            "Password123",     false, "Invalid email format" },
	        };
	    }

	    @Test(priority = 9,
	          dataProvider = "loginData",
	          description = "Data-driven login test for all credential combinations")
	    public void loginDataDriven(String email, String password,
	                                 boolean shouldPass, String testName) {
	        LoginPage lp = new LoginPage(driver);
	        lp.login(email, password);

	        if (shouldPass) {
	            Assert.assertTrue(lp.isLoginSuccessful(),
	                "FAILED [" + testName + "] — expected login success but failed"
	            );
	            System.out.println("PASSED [" + testName + "] — login successful");
	        } else {
	            boolean stillOnLogin = driver.getCurrentUrl().contains("/login");
	            Assert.assertTrue(stillOnLogin,
	                "FAILED [" + testName + "] — expected login failure but succeeded"
	            );
	            System.out.println(" PASSED [" + testName + "] — login correctly rejected");
	        }
	    }
	
}
