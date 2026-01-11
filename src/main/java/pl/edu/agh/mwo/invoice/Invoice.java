package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
//     private Collection<Product> products = new ArrayList<>();

     private Map<Product, Integer> products = new HashMap<>();

     public void addProduct(Product product) {
        this.addProduct(product, 1);

    }

    public void addProduct(Product product, Integer quantity) {
         this.products.put(product, quantity);

    }

    public BigDecimal getSubtotal() {

        BigDecimal value = BigDecimal.ZERO;
        for (Product product : this.products.keySet()) {
            Integer quantity = this.products.get(product);
            BigDecimal price = product.getPrice();
            price = price.multiply(BigDecimal.valueOf(quantity));
            value = value.add(price);
        }
        return value;
    }


    public BigDecimal getTax() {

      BigDecimal tax =  BigDecimal.ZERO;
         for (Map.Entry<Product, Integer> entry : products.entrySet()) {

         }

    return tax;
    }


    public BigDecimal getTotal() {return  BigDecimal.ZERO;}
}

