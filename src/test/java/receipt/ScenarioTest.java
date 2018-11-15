package receipt;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;

/**
 * INPUT:
 * <p>
 * Input 1:
 * 1 book at 12.49
 * 1 music CD at 14.99
 * 1 chocolate bar at 0.85
 * <p>
 * Input 2:
 * 1 imported box of chocolates at 10.00
 * 1 imported bottle of perfume at 47.50
 * <p>
 * Input 3:
 * 1 imported bottle of perfume at 27.99
 * 1 bottle of perfume at 18.99
 * 1 packet of headache pills at 9.75
 * 1 box of imported chocolates at 11.25
 * <p>
 * OUTPUT
 * <p>
 * Output 1:
 * 1 book : 12.49
 * 1 music CD: 16.49
 * 1 chocolate bar: 0.85
 * Sales Taxes: 1.50
 * Total: 29.83
 * <p>
 * 1 imported box of chocolates at 10.00
 * 1 imported bottle of perfume at 47.50
 * Output 2:
 * 1 imported box of chocolates: 10.50
 * 1 imported bottle of perfume: 54.65
 * Sales Taxes: 7.65
 * Total: 65.15
 * <p>
 * Output 3:
 * 1 imported bottle of perfume: 32.19
 * 1 bottle of perfume: 20.89
 * 1 packet of headache pills: 9.75
 * 1 imported box of chocolates: 11.85
 * Sales Taxes: 6.70
 * Total: 74.68
 */
public class ScenarioTest {

	private Basket basket;

	@Before
	public void init() {
		// create the basket with the taxes to be applied
		basket = new Basket(EnumSet.of(Tax.BASIC_SALES, Tax.IMPORT_DUTY));
	}

	@Test
	public void firstScenario() {
		Basket.Entry e1 = basket.add(1,
				new Good.GoodBuilder()
						.description("book")
						.unitPrice("12.49")
						.category(Category.BOOKS)
						.imported(false)
						.create());

		Basket.Entry e2 = basket.add(1,
				new Good.GoodBuilder()
						.description("music CD")
						.unitPrice("14.99")
						.category(Category.OTHERS)
						.imported(false)
						.create());

		Basket.Entry e3 = basket.add(1,
				new Good.GoodBuilder()
						.description("chocolate bar")
						.unitPrice("0.85")
						.category(Category.FOOD)
						.imported(false)
						.create());

		assertEquals(new BigDecimal("12.49"), e1.getTotalCost());
		assertEquals(new BigDecimal("16.49"), e2.getTotalCost());
		assertEquals(new BigDecimal("0.85"), e3.getTotalCost());
		assertEquals(new BigDecimal("1.50"), basket.getTaxesAmount());
		assertEquals(new BigDecimal("29.83"), basket.getTotalCost());
	}

	@Test
	public void secondScenario() {
		Basket.Entry e1 = basket.add(1,
				new Good.GoodBuilder()
						.description("imported box of chocolate")
						.unitPrice("10.00")
						.category(Category.FOOD)
						.imported(true)
						.create());

		Basket.Entry e2 = basket.add(1,
				new Good.GoodBuilder()
						.description("imported bottled of perfume")
						.unitPrice("47.50")
						.category(Category.OTHERS)
						.imported(true)
						.create());

		assertEquals(new BigDecimal("10.50"), e1.getTotalCost());
		assertEquals(new BigDecimal("54.65"), e2.getTotalCost());
		assertEquals(new BigDecimal("7.65"), basket.getTaxesAmount());
		assertEquals(new BigDecimal("65.15"), basket.getTotalCost());
	}

	@Test
	public void thirdScenario() {
		Basket.Entry e1 = basket.add(1,
				new Good.GoodBuilder()
						.description("imported bottle of perfume")
						.unitPrice("27.99")
						.category(Category.OTHERS)
						.imported(true)
						.create());

		Basket.Entry e2 = basket.add(1,
				new Good.GoodBuilder()
						.description("bottle of perfume")
						.unitPrice("18.99")
						.category(Category.OTHERS)
						.imported(false)
						.create());

		Basket.Entry e3 = basket.add(1,
				new Good.GoodBuilder()
						.description("packet of headache pills")
						.unitPrice("9.75")
						.category(Category.MEDICAL)
						.imported(false)
						.create());

		Basket.Entry e4 = basket.add(1,
				new Good.GoodBuilder()
						.description("box of imported chocolates")
						.unitPrice("11.25")
						.category(Category.FOOD)
						.imported(true)
						.create());

		assertEquals(new BigDecimal("32.19"), e1.getTotalCost());
		assertEquals(new BigDecimal("20.89"), e2.getTotalCost());
		assertEquals(new BigDecimal("9.75"), e3.getTotalCost());
		assertEquals(new BigDecimal("11.85"), e4.getTotalCost());
		assertEquals(new BigDecimal("6.70"), basket.getTaxesAmount());
		assertEquals(new BigDecimal("74.68"), basket.getTotalCost());
	}

}
