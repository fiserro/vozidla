package org.nocorp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class AcabProxy {

	public String url = "http://aplikace.policie.cz/patrani-vozidla/default.aspx";
	public String formName = "aspnetForm";
	public String buttonName = "ctl00$Application$cmdHledej";
	public String spzFieldNam = "ctl00$Application$txtSPZ";
	public String vinFieldName = "ctl00$Application$txtVIN";
	public String pocetZaznamuId = "ctl00_Application_lblPocetZaznamu";
	public String tableId = "celacr";

	public List<Zaznam> hledej(String spz, String vin)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		try (WebClient webClient = new WebClient()) {
			HtmlPage page = webClient.getPage(url);
			HtmlForm form = page.getFormByName(formName);
			HtmlSubmitInput button = form.getInputByName(buttonName);
			HtmlTextInput spzField = form.getInputByName(spzFieldNam);
			HtmlTextInput vinField = form.getInputByName(vinFieldName);
			if (spz != null)
				spzField.setValueAttribute(spz);
			if (vin != null)
				vinField.setValueAttribute(vin);
			page = button.click();
			HtmlTable table = page.getHtmlElementById(tableId);
			List<Zaznam> result = new ArrayList<>();
			table.getRows().stream().skip(1).forEach((row) -> {
				List<HtmlTableCell> cells = row.getCells();
				Zaznam z = new Zaznam();
				z.poradi = getString(cells, 0);
				z.rz = getString(cells, 1);
				z.mpz = getString(cells, 2);
				z.vin = getString(cells, 3);
				z.vyrobce = getString(cells, 4);
				z.typ = getString(cells, 5);
				z.druh = getString(cells, 6);
				result.add(z);
			});

			try {
				System.out.println(page.getElementById(pocetZaznamuId).asText());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	private String getString(List<HtmlTableCell> cells, int index) {
		try {
			return cells.get(index).asText();
		} catch (Exception e) {
			return null;
		}
	}

	public static class Zaznam {
		public String poradi;
		public String rz;
		public String mpz;
		public String vin;
		public String vyrobce;
		public String typ;
		public String druh;
		@Override
		public String toString() {
			return "Zaznam [poradi=" + poradi + ", rz=" + rz + ", mpz=" + mpz + ", vin=" + vin + ", vyrobce=" + vyrobce
					+ ", typ=" + typ + ", druh=" + druh + "]";
		}
	}
}
