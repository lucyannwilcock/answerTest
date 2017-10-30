package answer.king.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import answer.king.model.Receipt;
import answer.king.service.ReceiptService;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

	@Autowired
	private ReceiptService receiptService;

	/**
	 * Get all receipts saved in the database
	 * @return List<Receipt>
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Receipt> getAll() {
		return receiptService.getAll();
	}

	/**
	 * Create a given receipt
	 * @param receipt
	 * @return Receipt
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Receipt create(@RequestBody Receipt receipt) {
		return receiptService.save(receipt);
	}
}
