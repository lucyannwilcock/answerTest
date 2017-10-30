package answer.king.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "T_RECEIPT")
public class Receipt {

	/**
	 * Constructor
	 */
	public Receipt() {}
	
	/**
	 * Constructor
	 * @param payment
	 * @param order
	 */
	public Receipt(BigDecimal payment, Order order) {
		this.payment = payment;
		this.order = order;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Min(0)
	private BigDecimal payment;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL, CascadeType.PERSIST })
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	/**
	 * Get the change for a given payment
	 * @return
	 */
	public BigDecimal getChange() {
		BigDecimal totalOrderPrice = order.getLineItems()
			.stream()
			.map(LineItem::getCurrentPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		return payment.subtract(totalOrderPrice);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Receipt other = (Receipt) obj;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.getId().equals(other.order.getId()))
			return false;
		if (payment == null) {
			if (other.payment != null)
				return false;
		} else if (!payment.equals(other.payment))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
