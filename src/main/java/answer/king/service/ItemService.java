package answer.king.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import answer.king.model.Item;
import answer.king.repo.ItemRepository;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * Get all Items from the database
	 * @return List<Item>
	 */
	public List<Item> getAll() {
		return itemRepository.findAll();
	}

	/**
	 * Save the given item to the database
	 * @param item
	 * @return Item
	 */
	public Item save(Item item) {
		return itemRepository.save(item);
	}

	/**
	 * Change the price for a given item
	 * @param id
	 * @param price
	 * @return Item
	 */
	public Item changePrice(Long id, BigDecimal price) {
		Item item = itemRepository.findOne(id);

		item.setPrice(price);
		return itemRepository.save(item);
	}
}
