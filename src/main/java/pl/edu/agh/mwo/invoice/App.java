package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;

public class App {
    public static void main(String[] args) {
        Invoice invoice = new Invoice();
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);

        System.out.println(invoice.print());
        System.out.println("Subtotal: " + invoice.getSubtotal());
        System.out.println("Tax: " + invoice.getTax());
        System.out.println("Total: " + invoice.getTotal());
    }
}

