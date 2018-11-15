package receipt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A basket of purchased items
 */
public class Basket {

	/** Entries in the basket, each entry is related to a good */
	private List<Entry> entries = new ArrayList<>();

	/** Taxes to apply to the good in the basket */
	private EnumSet<Tax> taxes;

	/**
	 * Creates a new instance of Basket with the specified taxes to apply.
	 *
	 * @param taxes the set of taxes to be applied
	 */
	public Basket(EnumSet<Tax> taxes) {
		this.taxes = taxes;
	}

	/**
	 * Adds the entry to the basket and calculate tax and total amounts for the entry
	 *
	 * @param quantity of item purchased
	 * @param item     item purchased
	 * @return the entry corresponding to the quantity and item specified with tax and total amounts set
	 */
	public Entry add(int quantity, Good item) {
		Entry entry = new Entry(item, quantity).calculate(taxes);
		entries.add(entry);
		return entry;
	}

	public BigDecimal getTaxesAmount() {
		return entries.stream().map(Entry::getTaxesAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getTotalCost() {
		return entries.stream().map(Entry::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public Iterator<Entry> entries() {
		return entries.iterator();
	}

	/**
	 * This class represents an entry of a basket. Each entry is related to a specific item.
	 */
	public static class Entry {

		/**
		 * the item related to this line
		 */
		private Good item;

		/**
		 * the number of items
		 */
		private int quantity;

		/**
		 * the taxable amount whose value is (item unit price * quantity)
		 */
		private BigDecimal itemsAmount;

		/**
		 * the amount of taxes whose value is the sum of taxes calculated on taxableAmount
		 */
		private BigDecimal taxesAmount;

		/**
		 * the total amount whose value is the sum of taxableAmount and taxes
		 */
		private BigDecimal totalCost;

		private CalculateTax calculateTax;

		private Entry(Good item, int quantity) {
			this.item = item;
			this.quantity = quantity;
			this.itemsAmount = item.getUnitPrice().multiply(new BigDecimal(quantity));
			this.calculateTax = new CalculateTax(this);
		}

		/**
		 * Apply the specified set of taxes if the tax is mandatory or it is not mandatory but the item is not exempt
		 */
		private Entry calculate(EnumSet<Tax> taxes) {
			taxesAmount = taxes.stream()
					.filter(calculateTax)
					.map(calculateTax)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			totalCost = itemsAmount.add(taxesAmount);
			return this;
		}

		public int getQuantity() {
			return quantity;
		}

		public BigDecimal getTaxesAmount() {
			return taxesAmount;
		}

		public BigDecimal getTotalCost() {
			return totalCost;
		}

	}

	private static class CalculateTax implements Predicate<Tax>, Function<Tax, BigDecimal>  {

		private static final BigDecimal ROUNDING = new BigDecimal("0.05");

		private Entry entry;

		public CalculateTax(Entry entry) {
			this.entry = entry;
		}

		@Override
		public boolean test(Tax tax) {
			switch (tax) {
			case BASIC_SALES:
				return !entry.item.isExempt();
			case IMPORT_DUTY:
				return entry.item.isImported();
			default:
				return false;
			}
		}

		@Override
		public BigDecimal apply(Tax tax) {
			return round(entry.itemsAmount.multiply(tax.getRate()));
		}

		/**
		 * Thanks to
		 * https://stackoverflow.com/questions/2106615/round-bigdecimal-to-nearest-5-cents#answer-16665378
		 */
		private BigDecimal round(BigDecimal value) {
			return value.divide(ROUNDING, 0, RoundingMode.UP).multiply(ROUNDING);
		}

	}

}
