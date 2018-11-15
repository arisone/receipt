package receipt;

import java.math.BigDecimal;

/**
 * This class represents a good that can be purchased
 */
public class Good {

    private String description;

    private BigDecimal unitPrice;

    private Category category;

    private boolean imported;

    private Good() {
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public boolean isExempt() {
        return category.isExempt();
    }

    public boolean isImported() {
        return imported;
    }

    public static class GoodBuilder {

        private String description;

        private BigDecimal unitPrice;

        private Category category;

        private boolean imported;

        public GoodBuilder description(String description) {
            this.description = description;
            return this;
        }

        public GoodBuilder unitPrice(String unitPrice) {
            this.unitPrice = new BigDecimal(unitPrice);
            return this;
        }

        public GoodBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public GoodBuilder imported(boolean imported) {
            this.imported = imported;
            return this;
        }

        public Good create() {
            Good good = new Good();
            good.description = this.description;
            good.unitPrice = this.unitPrice;
            good.category = this.category;
            good.imported = this.imported;
            return good;
        }
    }

}
