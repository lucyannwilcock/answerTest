package answer.king.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;

@RunWith(SpringRunner.class)
public class OrderServiceTest {

	@TestConfiguration
    static class OrderServiceTestContextConfiguration {
        @Bean
        public OrderService orderService() {
            return new OrderService();
        }
    }
	
	@Autowired
    private OrderService orderService;
 
    @MockBean
    private OrderRepository orderRepository;
    
    @MockBean
    private ItemRepository itemRepository;
	
	@MockBean 
	private ReceiptRepository receiptRepository;
    
    @Before
    public void setUp() {
    	Item item = new Item("Item1", BigDecimal.TEN);
    	item.setId(new Long(2));
    	LineItem lineItem = new LineItem(item.getPrice(), item, 1);
		
		List<LineItem> lineItems = new ArrayList<LineItem>();
    	lineItems.add(lineItem);
    	
    	Order createdOrder = new Order(false, lineItems);
		createdOrder.setId(new Long(2));
		
		Receipt createdReceipt = new Receipt(BigDecimal.TEN, createdOrder);
		createdOrder.setReceipt(createdReceipt);
		
		Mockito.when(orderRepository.findOne(new Long(2))).thenReturn(createdOrder);
        Mockito.when(orderRepository.save(createdOrder)).thenReturn(createdOrder);
		Mockito.when(receiptRepository.save(createdReceipt)).thenReturn(createdReceipt); 
    }
    
	@Test
	public void testPay_SufficientFunds() {
		Receipt persisted = orderService.pay(new Long(2), BigDecimal.TEN);
		assertNotNull(persisted);
		assertEquals(new Long(2), persisted.getOrder().getId());
		assertEquals(BigDecimal.TEN, persisted.getPayment());
		assertEquals(BigDecimal.ZERO, persisted.getChange());
	}
	
	@Test
	public void testPay_OrderPaid() {
		Receipt receipt = orderService.pay(new Long(2), BigDecimal.TEN);
		assertNotNull(receipt.getOrder());
		assertEquals(new Long(2), receipt.getOrder().getId());
		assertEquals(true, receipt.getOrder().getPaid());
	}
	
	@Test
	public void testPay_InufficientFunds() {
		try {
			orderService.pay(new Long(2), BigDecimal.ZERO);
			fail("Payment should not be processed with insuffucient funds.");
		}
		catch (IllegalArgumentException e){
			assertEquals("Payment can not be less than order total.", e.getMessage());
		}
		catch (Exception e){
			fail();
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddLineItem_NewLineItem() {
		Item item = new Item("Item3", BigDecimal.TEN);
    	item.setId(new Long(3));
		Mockito.when(itemRepository.findOne(new Long(3))).thenReturn(item);
		Order persisted = orderService.addLineItem(new Long(2), new Long(3), 3);
		assertNotNull(persisted);
		assertEquals(2, persisted.getLineItems().size());
	}
	
	@Test
	public void testAddLineItem_ExistingLineItem() {
		Item item = new Item("Item2", BigDecimal.TEN);
    	item.setId(new Long(2));
		Mockito.when(itemRepository.findOne(new Long(2))).thenReturn(item);
		Order persisted = orderService.addLineItem(new Long(2), new Long(2), 3);
		assertNotNull(persisted);
		assertEquals(1, persisted.getLineItems().size());
		assertEquals(4, persisted.getLineItems().get(0).getQuantity());
	}
	
	@Test
	public void testAddLineItem_NonExistantItem() {
		try {
			orderService.addLineItem(new Long(2), new Long(20), 3);
		} catch (Exception e) {
			assertEquals("Item with id: 20 does not exist.", e.getMessage());
		}
	}

}
