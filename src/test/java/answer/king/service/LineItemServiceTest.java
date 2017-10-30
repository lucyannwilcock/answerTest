package answer.king.service;

import static org.junit.Assert.*;
import java.math.BigDecimal;

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
import answer.king.repo.LineItemRepository;

@RunWith(SpringRunner.class)
public class LineItemServiceTest {
	
	@TestConfiguration
    static class LineItemServiceTestContextConfiguration {
        @Bean
        public LineItemService lineLineItemService() {
            return new LineItemService();
        }
    }
	
	@Autowired
    private LineItemService lineItemService;
 
    @MockBean
    private LineItemRepository lineItemRepository;
    
    @Before
    public void setUp() {
        
    }

	@Test
	public void testCreate() {
		Order order = new Order();
		order.setId(new Long(1));
		
		Item item = new Item();
		item.setId(new Long(1));
		
		LineItem createdLineItem = new LineItem(BigDecimal.ONE, item, 10);
		
		Mockito.when(lineItemService.save(createdLineItem)).thenReturn(createdLineItem);
		
		LineItem persisted = lineItemService.save(createdLineItem);
		assertNotNull(persisted);
	}
	
}
