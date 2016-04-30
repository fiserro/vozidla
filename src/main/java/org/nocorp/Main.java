package org.nocorp;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

/**
 * Created by robert on 4/30/16.
 */
public class Main {
	public static void main(String[] args) throws IOException {
		try (WebClient webClient = new WebClient()) {
			HtmlPage page = webClient.getPage("http://aplikace.policie.cz/patrani-vozidla/default.aspx");

			HtmlForm form = page.getFormByName("aspnetForm");

			HtmlSubmitInput button = form.getInputByName("ctl00$Application$cmdHledej");
			HtmlTextInput spzField = form.getInputByName("ctl00$Application$txtSPZ");
			HtmlTextInput vinField = form.getInputByName("ctl00$Application$txtVIN");

			spzField.setValueAttribute("xxx");

			page = button.click();
			DomElement pocetZaznamu = page.getElementById("ctl00_Application_lblPocetZaznamu");
			HtmlDivision div = (HtmlDivision) page.getByXPath("//div[@class='vypisZaznamu']").get(0);
			HtmlTable table = page.getHtmlElementById("celacr");
			for (HtmlTableRow row : table.getRows()) {
				System.out.println("Found row");
				for (HtmlTableCell cell : row.getCells()) {
					System.out.println("   Found cell: " + cell.asText());
				}
			}
			System.out.println(pocetZaznamu.asText());
		}
	}
}
