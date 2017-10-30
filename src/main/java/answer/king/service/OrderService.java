package answer.king.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ReceiptRepository receiptRepository;

	/**
	 * Get all orders from the database
	 * @return List<Order>
	 */
	public List<Order> getAll() {
		return orderRepository.findAll();
	}

	/**
	 * Save the given order to the database
	 * @param order
	 * @return Order
	 */
	public Order save(Order order) {
		return orderRepository.save(order);
	}

	/**
	 * Add a line item to the order. If the line item already exists then increase the quantity
	 * @param id
	 * @param itemId
	 * @param quantity
	 * @return Order
	 */
	public Order addLineItem(Long id, Long itemId, int quantity) {
		Order order = orderRepository.findOne(id);
		
		//Find any line item on the order linked to a given item
		Optional<LineItem> matchingLineItems = order.getLineItems().stream().
			    filter(i -> i.getItem().getId().compareTo(itemId) == 0).
			    findFirst();
		LineItem lineItem = matchingLineItems.orElse(null);
		
		if (lineItem != null) {
			//If the item already exists on the order then increase the order
			lineItem.setQuantity(lineItem.getQuantity() + quantity);
		} else {
			//Otherwise just add a new line item
			lineItem = createNewLineItem(itemId, order, quantity);
			order.getLineItems().add(lineItem);
		}
		
		return orderRepository.save(order);
	}

	private LineItem createNewLineItem(Long itemId, Order order, int quantity) {
		Item item = itemRepository.findOne(itemId);
		
		if (item == null) {
			throw new IllegalArgumentException("Item with id: " + itemId + " does not exist.");
		}
		
		return new LineItem(item.getPrice(), item, 1);
	}

	/**
	 * Pay the order and return a receipt for the payment
	 * @param id
	 * @param payment
	 * @return Receipt
	 * @throws IllegalArgumentException
	 */
	public Receipt pay(Long id, BigDecimal payment) throws IllegalArgumentException {
		Order order = orderRepository.findOne(id);
		
		if(checkSufficientFunds(order.getLineItems(), payment)) {
			throw new IllegalArgumentException("Payment can not be less than order total.");
		}
		
		order.setPaid(true);
		order = save(order);

		return createAndSaveReceipt(payment, order);
	}

	private Receipt createAndSaveReceipt(BigDecimal payment, Order order) {
		Receipt receipt = new Receipt(payment, order);
		order.setReceipt(receipt);
		receipt = receiptRepository.save(receipt);
		return receipt;
	}
	
	private boolean checkSufficientFunds(List<LineItem> items, BigDecimal payment) {
		//Get the total price for each line item, depending on quantity
		Function<LineItem, BigDecimal> totalMapper = lineItem -> lineItem.getCurrentPrice().multiply(new BigDecimal(lineItem.getQuantity()));
		
		//Sum all total prices for all line items
		BigDecimal totalPrice = items.stream()
		        .map(totalMapper)
		        .reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return payment.compareTo(totalPrice) < 0;
	}
}
