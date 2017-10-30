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
import answer.king.repo.ItemRepository;

@RunWith(SpringRunner.class)
public class ItemServiceTest {
	
	private final Long id = new Long(10);
	
	@TestConfiguration
    static class ItemServiceTestContextConfiguration {
        @Bean
        public ItemService itemService() {
            return new ItemService();
        }
    }
	
	@Autowired
    private ItemService itemService;
 
    @MockBean
    private ItemRepository itemRepository;
    
    @Before
    public void setUp() {
        
    }
    
	@Test
	public void testGetAll() {
		Item foundItem = new Item("A Found Item", BigDecimal.TEN);
		List<Item> itemList = new ArrayList<Item>();
		itemList.add(foundItem);
        Mockito.when(itemRepository.findAll()).thenReturn(itemList);
		
		List<Item> foundItemList = itemService.getAll();
		assertNotNull(foundItemList);
		assertEquals(1, foundItemList.size());
		assertEquals(BigDecimal.TEN, foundItemList.get(0).getPrice());
	}

	@Test
	public void testCreate() {
		Item createdItem = new Item("A Created Item", BigDecimal.TEN);
		Mockito.when(itemRepository.save(createdItem)).thenReturn(createdItem);
		
		Item persisted = itemService.save(createdItem);
		assertNotNull(persisted);
		assertEquals(BigDecimal.TEN, persisted.getPrice());
	}
	
	@Test
	public void testCheckPrice_CorrectId() {
		Item priceItem = new Item("An item to change", BigDecimal.TEN);
		priceItem.setId(id);
		
		Mockito.when(itemRepository.findOne(id)).thenReturn(priceItem);
		Mockito.when(itemRepository.save(priceItem)).thenReturn(priceItem);
	
		Item item = itemService.changePrice(id, BigDecimal.ZERO);
		assertNotNull(item);
		assertEquals(BigDecimal.ZERO, item.getPrice());
	}
	
	@Test
	public void testCheckPrice_IncorrectId() {
		try {
			itemService.changePrice(id, BigDecimal.ZERO);
			fail("Should not save updated price on non-existent item.");
		} catch (NullPointerException e) {
			
		}
	}
	
}
