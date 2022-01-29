package zw.co.afrosoft.app;

import zw.co.afrosoft.data.Product;
import zw.co.afrosoft.data.ProductManager;
import zw.co.afrosoft.data.Rating;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;

public class Shop {

    public static void main(String[] args) {
        ProductManager pm = new ProductManager(Locale.US);

        pm.createProduct(101,"Tea",BigDecimal.valueOf(1.99), Rating.NOT_RATED);
//        pm.printProductReport(101);
        pm.reviewProduct(101,Rating.FOUR_STAR,"Nice hot cup of tea");
        pm.reviewProduct(101,Rating.TWO_STAR,"Rather weak tea");
        pm.reviewProduct(101,Rating.THREE_STAR,"Good tea");
        pm.reviewProduct(101,Rating.ONE_STAR,"Hot cup of tea");
        pm.reviewProduct(101,Rating.FIVE_STAR,"Just regular tea");
        pm.reviewProduct(101,Rating.FOUR_STAR,"Not that hot though");
//        pm.printProductReport(101);

        pm.createProduct(102,"Coffie",BigDecimal.valueOf(2.99), Rating.FOUR_STAR);
        pm.reviewProduct(102,Rating.THREE_STAR,"Good Coffie");
        pm.reviewProduct(102,Rating.ONE_STAR,"Hot cup of Coffie");
        pm.reviewProduct(102,Rating.FIVE_STAR,"Just regular Coffie");
//        pm.printProductReport(102);

        pm.createProduct(103,"Cake",BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
        pm.reviewProduct(103,Rating.THREE_STAR,"Good ");
        pm.reviewProduct(103,Rating.ONE_STAR,"Sweet Cake");
        pm.reviewProduct(103,Rating.FIVE_STAR,"Yummy");
        pm.reviewProduct(103,Rating.FOUR_STAR,"Not fresh");
//        pm.printProductReport(103);
        Comparator<Product> ratingSorter = (p1,p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
        Comparator<Product> priceSorter = (p1,p2) -> p2.getPrice().compareTo(p1.getPrice());
        pm.printProducts(ratingSorter);
        pm.printProducts(priceSorter);

        pm.printProducts(ratingSorter.thenComparing(priceSorter));


//        Product p4 = pm.createProduct(105,"Cookie",BigDecimal.valueOf(3.99),Rating.TWO_STAR,LocalDate.now());
//        Product p5 = p3.applyRating(Rating.THREE_STAR);
//
//        Product p6 = pm.createProduct(104,"Chocolate",BigDecimal.valueOf(3.99), Rating.FIVE_STAR);
//        Product p7 = pm.createProduct(104,"Chocolate",BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//        Product p8 = p4.applyRating(Rating.FOUR_STAR);
//        Product p9 = p1.applyRating(Rating.TWO_STAR);
//
//        System.out.println(p1.getBestBefore());
//        System.out.println(p3.getBestBefore());
//        System.out.println(p1);
//        System.out.println(p2);
//        System.out.println(p3);
//        System.out.println(p4);
//        System.out.println(p5);
//        System.out.println(p6);
//        System.out.println(p7);
//        System.out.println(p8);
//        System.out.println(p9);
    }
}
