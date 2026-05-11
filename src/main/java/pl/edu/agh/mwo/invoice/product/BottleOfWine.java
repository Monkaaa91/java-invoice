package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BottleOfWine extends Product {
    private static final BigDecimal EXCISE = new BigDecimal("5.56");

    public BottleOfWine(String name, BigDecimal price) {
        super(name, price, new BigDecimal("0.08"));
    }

    @Override
    public BigDecimal getPriceWithTax() {
        BigDecimal baseWithTax = super.getPriceWithTax();
        BigDecimal result = baseWithTax.add(EXCISE);
        return result.setScale(2, RoundingMode.HALF_EVEN);
    }
}

