package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private static long nextNumber = 1;

    private final long number;
    private final Map<String, InvoiceItem> items = new LinkedHashMap<>();

    public Invoice() {
        this.number = nextNumber++;
    }

    public long getNumber() {
        return number;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        String key = product.getName();
        InvoiceItem existing = items.get(key);
        if (existing != null) {
            existing.increaseQuantity(quantity);
        } else {
            items.put(key, new InvoiceItem(product, quantity));
        }
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (InvoiceItem item : items.values()) {
            BigDecimal line = item.product.getPrice().multiply(BigDecimal.valueOf(item.quantity));
            subtotal = subtotal.add(line);
        }
        return subtotal.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getTax() {
        BigDecimal tax = BigDecimal.ZERO;
        for (InvoiceItem item : items.values()) {
            BigDecimal perUnitTax = item.product.getPriceWithTax().subtract(item.product.getPrice());
            BigDecimal lineTax = perUnitTax.multiply(BigDecimal.valueOf(item.quantity));
            tax = tax.add(lineTax);
        }
        return tax.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (InvoiceItem item : items.values()) {
            BigDecimal perUnitWithTax = item.product.getPriceWithTax();
            BigDecimal line = perUnitWithTax.multiply(BigDecimal.valueOf(item.quantity));
            total = total.add(line);
        }
        return total.setScale(2, RoundingMode.HALF_EVEN);
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append("Numer faktury: ").append(number).append("\n");
        for (InvoiceItem item : items.values()) {
            sb.append(item.product.getName())
                    .append(", ")
                    .append(item.quantity)
                    .append(" szt., ")
                    .append(item.product.getPriceWithTax())
                    .append("\n");
        }
        sb.append("Liczba pozycji: ").append(items.size());
        return sb.toString();
    }

    private static class InvoiceItem {
        private final Product product;
        private int quantity;

        InvoiceItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        void increaseQuantity(int delta) {
            this.quantity += delta;
        }
    }
}
