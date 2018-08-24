package htmlunit;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class TestMain {

	@Test
	public void test() throws IOException {
		final WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		final HtmlPage page = webClient.getPage("https://blog.csdn.net/qqhjqs?viewmode=contents");
		List<Object> byXPath = page.getByXPath("//*[@id=\"mainBox\"]/main/div[2]/div/h4/a/text()");
		for (Object s : byXPath) {
			System.out.println(s);
		}
	}

	@Test
	public void login() throws IOException {
		// https://www.itjuzi.com/user/login
		final WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);
		final HtmlPage page = webClient.getPage("https://www.itjuzi.com/user/login?flag=radar&redirect=/company");
		HtmlForm loginForm = page.getFormByName("login_form");
		HtmlTextInput inputByName = loginForm.getInputByName("identity");
		HtmlTextInput password = loginForm.("password");
		HtmlSubmitInput submit = loginForm.getInputByName("submit");

		inputByName.type("18255062124");
		password.type("wxc772704457@qq");
		HtmlPage loginPage = submit.click();

		System.out.println(loginPage.getTextContent());

	}
}
