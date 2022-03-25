package edu.nus.iss.POApp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.nus.iss.POApp.model.Quotation;
import edu.nus.iss.POApp.service.QuotationService;

@SpringBootTest
class PoAppApplicationTests {

	@Autowired
	private QuotationService quotationSvc;

	@Test
	void contextLoads() {

		List<String> items = Arrays.asList("durian", "plum", "pear");

		Optional<Quotation> opt = quotationSvc.getQuotations(items);

		Assertions.assertTrue(opt.isEmpty());
	}

}
