package answer.king.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import answer.king.model.Item;
import answer.king.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * Get all Items saved in the database
	 * @return List<Item>
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Item> getAll() {
		return itemService.getAll();
	}

	/**
	 * Save the given item to the database
	 * @param item
	 * @return Item
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Item create(@RequestBody Item item) {
		return itemService.save(item);
	}
	
	/**
	 * Change the price of the given item
	 * @param id
	 * @param price
	 * @return Item
	 */
	@RequestMapping(value = "/{id}/changePrice/{price}", method = RequestMethod.PUT)
	public Item changePrice(@PathVariable("id") Long id, @PathVariable("price") BigDecimal price) {
		return itemService.changePrice(id, price);
	}
}
