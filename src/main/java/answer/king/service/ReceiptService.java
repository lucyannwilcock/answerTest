package answer.king.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import answer.king.model.Receipt;
import answer.king.repo.ReceiptRepository;

@Service
@Transactional
public class ReceiptService {

	@Autowired
	private ReceiptRepository receiptRepository;

	/**
	 * Get all the receipts in the database
	 * @return List<Receipt>
	 */
	public List<Receipt> getAll() {
		return receiptRepository.findAll();
	}

	/**
	 * Save the given receipt to the database
	 * @param receipt
	 * @return Receipt
	 */
	public Receipt save(Receipt receipt) {
		return receiptRepository.save(receipt);
	}
}
