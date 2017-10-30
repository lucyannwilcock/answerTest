package answer.king.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * Get all the orders saved in the database
	 * @return List<Order>
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Order> getAll() {
		return orderService.getAll();
	}

	/**
	 * Create a new order
	 * @return Order
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Order create() {
		return orderService.save(new Order());
	}

	/**
	 * Add a new line item to an existing order
	 * @param id
	 * @param itemId
	 * @param quantity
	 * @return Order
	 */
	@RequestMapping(value = "/{id}/addItem/{itemId}/{quantity}", method = RequestMethod.PUT)
	public Order addItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId, @PathVariable("quantity") int quantity) {
		return orderService.addLineItem(id, itemId, quantity);
	}

	/**
	 * Pay the order and return a receipt for the payment
	 * @param id
	 * @param payment
	 * @return Receipt
	 */
	@RequestMapping(value = "/{id}/pay", method = RequestMethod.PUT)
	public Receipt pay(@PathVariable("id") Long id, @RequestBody BigDecimal payment) {
		return orderService.pay(id, payment);
	}
}
