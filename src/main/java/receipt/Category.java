package receipt;

/**
 * A representation of the category of the goods.
 */
public enum Category {
    BOOKS(true),
    FOOD(true),
    MEDICAL(true),
    OTHERS(false);

    /** Specifies if the category is exempt */
    private boolean exempt;

    Category(boolean exempt) {
        this.exempt = exempt;
    }

    boolean isExempt(){
        return exempt;
    }

}
