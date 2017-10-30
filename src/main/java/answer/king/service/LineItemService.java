package answer.king.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import answer.king.model.LineItem;
import answer.king.repo.LineItemRepository;

@Service
@Transactional
public class LineItemService {

	@Autowired
	private LineItemRepository lineItemRepository;

	/**
	 * Get all the line items from the database
	 * @return List<LineItem>
	 */
	public List<LineItem> getAll() {
		return lineItemRepository.findAll();
	}

	/**
	 * Save the given line item
	 * @param lineItem
	 * @return LineItem
	 */
	public LineItem save(LineItem lineItem) {
		return lineItemRepository.save(lineItem);
	}
}
