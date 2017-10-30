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
import answer.king.repo.ReceiptRepository;

@RunWith(SpringRunner.class)
public class ReceiptServiceTest {

	private Receipt createdReceipt;
	
	@TestConfiguration
    static class ReceiptServiceTestContextConfiguration {
        @Bean
        public ReceiptService receiptService() {
            return new ReceiptService();
        }
    }
	
	@Autowired
    private ReceiptService receiptService;
 
    @MockBean
    private ReceiptRepository receiptRepository;
    
    @Before
    public void setUp() {
    	Item item = new Item("Item1", BigDecimal.TEN);
    	LineItem lineItem = new LineItem(item.getPrice(), item, 1);
		
		List<LineItem> lineItems = new ArrayList<LineItem>();
    	lineItems.add(lineItem);
    	
    	Order order1 = new Order(false, lineItems);
    	Order order2 = new Order(false, lineItems);
    	
    	Receipt foundReceipt = new Receipt(BigDecimal.TEN, order1);
		
		createdReceipt = new Receipt(BigDecimal.TEN, order2);
     
		List<Receipt> receiptList = new ArrayList<Receipt>();
		receiptList.add(foundReceipt);
		
        Mockito.when(receiptRepository.findAll()).thenReturn(receiptList);
        Mockito.when(receiptRepository.save(createdReceipt)).thenReturn(createdReceipt);
    }
    
	@Test
	public void testGetAll() {
		List<Receipt> receiptList = receiptService.getAll();
		assertNotNull(receiptList);
		assertEquals(1, receiptList.size());
		assertEquals(BigDecimal.TEN, receiptList.get(0).getPayment());
	}

	@Test
	public void testCreate() {
		Receipt persisted = receiptService.save(createdReceipt);
		assertNotNull(persisted);
		assertEquals(BigDecimal.TEN, persisted.getPayment());
	}
	
	@Test
	public void testChangeItemPrice() {
		List<Receipt> receiptList = receiptService.getAll();
		assertNotNull(receiptList);
		
		Receipt receipt = receiptList.get(0);
		BigDecimal change = receipt.getChange();
		Item item = receipt.getOrder().getLineItems().get(0).getItem();
		
		item.setPrice(BigDecimal.ONE);
		assertEquals(change, receipt.getChange());
	}
	
}
