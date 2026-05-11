package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.*;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTax()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getTotal(), Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTax()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getSubtotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    @Test
    public void testPriceWithTaxIncludesExcise() {
        Product wine = new BottleOfWine("Wino", new BigDecimal("10.00"));
        Assert.assertThat(new BigDecimal("16.36"), Matchers.comparesEqualTo(wine.getPriceWithTax().setScale(2)));
    }

    @Test
    public void testTaxAmountPerUnitIncludesExcise() {
        Product wine = new BottleOfWine("Wino", new BigDecimal("10.00"));
        BigDecimal taxPerUnit = wine.getPriceWithTax().subtract(wine.getPrice());
        Assert.assertThat(new BigDecimal("6.36"), Matchers.comparesEqualTo(taxPerUnit.setScale(2)));
    }

    @Test
    public void testInvoiceTaxIncludesExciseForSingleBottle() {
        invoice.addProduct(new BottleOfWine("Wino", new BigDecimal("10.00")), 1);
        Assert.assertThat(new BigDecimal("6.36"), Matchers.comparesEqualTo(invoice.getTax()));
    }

    @Test
    public void testInvoiceTotalIncludesExciseForMultipleItems() {
        invoice.addProduct(new BottleOfWine("Wino", new BigDecimal("10.00")), 2);
        invoice.addProduct(new FuelCanister("Kanister", new BigDecimal("5.00")), 1);
        Assert.assertThat(new BigDecimal("44.13"), Matchers.comparesEqualTo(invoice.getTotal()));
    }

    @Test
    public void testInvoiceTaxSumIncludesExcisePerUnit() {
        invoice.addProduct(new BottleOfWine("Wino", new BigDecimal("10.00")), 2);
        invoice.addProduct(new FuelCanister("Kanister", new BigDecimal("5.00")), 1);

        Assert.assertThat(new BigDecimal("19.13"), Matchers.comparesEqualTo(invoice.getTax()));
    }

    @Test
    public void whenAddingSameProductObjectTwice_quantityIsSummed_andOnlyOnePositionExists() {
        TaxFreeProduct p = new TaxFreeProduct("Kubek", new BigDecimal("5"));
        invoice.addProduct(p);
        invoice.addProduct(p);

        Assert.assertThat(new BigDecimal("10.00"), Matchers.comparesEqualTo(invoice.getSubtotal()));
        String printed = invoice.print();
        Assert.assertTrue(printed.contains("Kubek"));
        Assert.assertTrue(printed.contains("2 szt."));
        Assert.assertTrue(printed.contains("Liczba pozycji: 1"));
    }

    @Test
    public void whenAddingSameProductByNameWithQuantities_quantitiesAreAccumulated() {
        TaxFreeProduct p = new TaxFreeProduct("Dlugopis", new BigDecimal("2.50"));
        invoice.addProduct(p, 3); // 3 szt.
        invoice.addProduct(new TaxFreeProduct("Dlugopis", new BigDecimal("2.50")), 2); // kolejne 2 szt.

        Assert.assertThat(new BigDecimal("12.50"), Matchers.comparesEqualTo(invoice.getSubtotal()));

        String printed = invoice.print();
        Assert.assertTrue(printed.contains("Dlugopis"));
        Assert.assertTrue(printed.contains("5 szt."));
        Assert.assertTrue(printed.contains("Liczba pozycji: 1"));
    }

    @Test
    public void differentNamesCreateSeparatePositions() {
        invoice.addProduct(new TaxFreeProduct("A", new BigDecimal("1")), 1);
        invoice.addProduct(new TaxFreeProduct("B", new BigDecimal("1")), 1);

        Assert.assertThat(new BigDecimal("2.00"), Matchers.comparesEqualTo(invoice.getSubtotal()));
        String printed = invoice.print();
        Assert.assertTrue(printed.contains("Liczba pozycji: 2"));
    }
}
