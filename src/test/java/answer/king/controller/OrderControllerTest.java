package answer.king.controller;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import answer.king.Application;
import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class OrderControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private OrderRepository orderRepository;
	
	@MockBean 
	private ReceiptRepository receiptRepository;
	
	@MockBean 
	private ItemRepository itemRepository;

	@Before
	public void setup() throws Exception {
		mockMvc = webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
		
		Item item = new Item("Item1", BigDecimal.TEN);
		item.setId(new Long(2));
    	LineItem lineItem = new LineItem(item.getPrice(), item, 1);
		
		List<LineItem> lineItems = new ArrayList<LineItem>();
    	lineItems.add(lineItem);
    	
    	Order createdOrder = new Order(false, lineItems);
		createdOrder.setId(new Long(2));
		
		Receipt createdReceipt = new Receipt(BigDecimal.TEN, createdOrder);
		createdOrder.setReceipt(createdReceipt);
		
		Item item2 = new Item("Item2", BigDecimal.TEN);
		item2.setId(new Long(3));
		
		Mockito.when(orderRepository.findOne(new Long(2))).thenReturn(createdOrder);
        Mockito.when(orderRepository.save(createdOrder)).thenReturn(createdOrder);
		Mockito.when(receiptRepository.save(createdReceipt)).thenReturn(createdReceipt);
		Mockito.when(itemRepository.findOne(new Long(3))).thenReturn(item2);
	}

	@Test
	public void testPay_SufficientFunds() {
		try {
			Long id = new Long(2);
			String jsonInString = mapper.writeValueAsString(BigDecimal.TEN);

			this.mockMvc.perform(put("/order/" + id + "/pay")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonInString))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.payment", is(10)))
			.andExpect(jsonPath("$.change", is(0)));
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testPay_updateOrderPaid() {
		try {
			Long id = new Long(2);
			String jsonInString = mapper.writeValueAsString(BigDecimal.TEN);

			this.mockMvc.perform(put("/order/" + id + "/pay")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonInString))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.order.paid", is(true)));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testPay_InsufficientFunds() {
		try {
			Long id = new Long(2);
			String jsonInString = mapper.writeValueAsString(BigDecimal.ZERO);

			this.mockMvc.perform(put("/order/" + id + "/pay")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonInString));
			
			fail("Payment should not be processed with insufficient funds.");
		} catch (Exception e) {
			assertEquals("Payment can not be less than order total.", e.getCause().getMessage());
		}
	}
	
	@Test
	public void testAddLineItem_NewLineItem() {
		Item item = new Item("Item3", BigDecimal.TEN);
    	item.setId(new Long(3));
    	
    	try {
			this.mockMvc.perform(put("/order/2/addItem/3/3"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.lineItems", hasSize(2)));
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testAddLineItem_ExistingLineItem() {
		Item item = new Item("Item3", BigDecimal.TEN);
    	item.setId(new Long(3));
    	
    	try {
			this.mockMvc.perform(put("/order/2/addItem/2/3"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.lineItems", hasSize(1)))
			.andExpect(jsonPath("$.lineItems.[0].quantity", is(4)));
		} catch (Exception e) {
			fail();
		}
	}
}
