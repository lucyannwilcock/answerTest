package answer.king.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "T_LINE_ITEM")
public class LineItem {

	/**
	 * Constructor
	 */
	public LineItem() {}
	
	/**
	 * Constructor
	 * @param currentPrice
	 * @param item
	 * @param quantity
	 */
	public LineItem(BigDecimal currentPrice, Item item, int quantity) {
		this.currentPrice = currentPrice;
		this.item = item;
		this.quantity = quantity;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL, CascadeType.PERSIST })
	@JoinColumn(name = "ITEM_ID")
	private Item item;

	private int quantity = 1;
	
	private BigDecimal currentPrice;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}
}
