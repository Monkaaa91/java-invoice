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
         if(quantity <= 0){
             throw new IllegalArgumentException("Quantity should be greater than 0");
         }
         if(products == null || products.isEmpty()) {

             throw new IllegalArgumentException("Product is null");
         }

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
      for (Product product : this.products.keySet()) {
          Integer quantity = this.products.get(product);
          BigDecimal pricetax = product.getPriceWithTax().subtract(product.getPrice());
          pricetax = pricetax.multiply(BigDecimal.valueOf(quantity));
          tax = tax.add(pricetax);
      }

    return getSubtotal().multiply(tax);
    }


    public BigDecimal getTotal() {
         BigDecimal Total =  BigDecimal.ZERO;
         for (Product product : this.products.keySet()) {
         Integer quantity = this.products.get(product);
         BigDecimal price = product.getPriceWithTax();
         price = price.multiply(BigDecimal.valueOf(quantity));
         Total = Total.add(price);
         }
         return Total;
    }
}

