package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) {
        if (name == null) {
            throw new java.lang.IllegalArgumentException("name cannot be null");
        }
        if (name.isBlank()) {
            throw new java.lang.IllegalArgumentException("name cannot be empty");
        }
        this.name = name;
        if (price == null) {
            throw new java.lang.IllegalArgumentException("price cannot be null");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new java.lang.IllegalArgumentException("price cannot be negative");
        }
        this.price = price;
        this.taxPercent = tax;
    }

    public String getName() { return name; }


    public BigDecimal getPrice() {return price;}

    public BigDecimal getTaxPercent() {return  taxPercent;}

    public BigDecimal getPriceWithTax() {return price.multiply(BigDecimal.ONE.add(taxPercent));}

}


