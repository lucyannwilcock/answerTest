package answer.king.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import answer.king.Application;
import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ReceiptControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
	}

	@Test
	public void testGetAll() {
		try {
			this.mockMvc.perform(get("/receipt"))
			.andExpect(status().isOk());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	public void testCreate() {
		try {
			Receipt newReceipt = new Receipt(BigDecimal.TEN, getOrderForNewReceipt());
			String jsonInString = mapper.writeValueAsString(newReceipt);

			this.mockMvc.perform(post("/receipt")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonInString))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.payment", is(10)));
		} catch (IOException e) {
			fail("IOException: ");
			e.printStackTrace();
		} 
		catch (Exception e) {
			fail("Exception: ");
			e.getMessage();
			e.getStackTrace();
		}
	}

	@Test
	public void testCreate_NoPayment() {
		try {
			Receipt newReceipt = new Receipt(null, getOrderForNewReceipt());
			String jsonInString = mapper.writeValueAsString(newReceipt);

			this.mockMvc.perform(post("/receipt")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonInString));
			fail("Receipt should not be created without payment");
		}
		catch (Exception e) {}
	}
	
	@Test
	public void testCreate_NegativePayment() {
		try {
			BigDecimal negative = new BigDecimal(-10);
			Receipt newReceipt = new Receipt(negative, getOrderForNewReceipt());
			String jsonInString = mapper.writeValueAsString(newReceipt);

			this.mockMvc.perform(post("/receipt")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonInString));
			fail("Receipt should not be created with negative payment");
		}
		catch (Exception e) {
			assertTrue(e.getCause().getMessage().contains("must be greater than or equal to 0', propertyPath=payment"));
		}
	}
	
	private Order getOrderForNewReceipt() throws JsonProcessingException {
		Item item = new Item("Item1", BigDecimal.TEN);
		LineItem lineItem = new LineItem(item.getPrice(), item, 1);
		
    	List<LineItem> lineItems = new ArrayList<LineItem>();
    	lineItems.add(lineItem);

		return new Order(false, lineItems);
	}
	
}
