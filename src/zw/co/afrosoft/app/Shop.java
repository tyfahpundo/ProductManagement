package zw.co.afrosoft.app;

import zw.co.afrosoft.data.Product;
import zw.co.afrosoft.data.ProductManager;
import zw.co.afrosoft.data.Rating;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

public class Shop {

    public static void main(String[] args) {
        ProductManager pm = new ProductManager(Locale.US);

        Product p1 = pm.createProduct(101,"Tea",BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pm.printProductReport(p1);
        p1 = pm.reviewProduct(p1,Rating.FOUR_STAR,"Nice hot cup of tea");
        p1 = pm.reviewProduct(p1,Rating.TWO_STAR,"Rather weak tea");
        p1 = pm.reviewProduct(p1,Rating.THREE_STAR,"Good tea");
        p1 = pm.reviewProduct(p1,Rating.ONE_STAR,"Hot cup of tea");
        p1 = pm.reviewProduct(p1,Rating.FIVE_STAR,"Just regular tea");
        p1 = pm.reviewProduct(p1,Rating.FOUR_STAR,"Not that hot though");
        pm.printProductReport(p1);

        Product p2 = pm.createProduct(102,"Coffie",BigDecimal.valueOf(2.99), Rating.FOUR_STAR);
        p2 = pm.reviewProduct(p2,Rating.THREE_STAR,"Good Coffie");
        p2 = pm.reviewProduct(p2,Rating.ONE_STAR,"Hot cup of Coffie");
        p2 = pm.reviewProduct(p2,Rating.FIVE_STAR,"Just regular Coffie");
        pm.printProductReport(p2);
        Product p3 = pm.createProduct(103,"Cake",BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
        p3 = pm.reviewProduct(p3,Rating.THREE_STAR,"Good ");
        p3 = pm.reviewProduct(p3,Rating.ONE_STAR,"Sweet Cake");
        p3 = pm.reviewProduct(p3,Rating.FIVE_STAR,"Yummy");
        p3 = pm.reviewProduct(p3,Rating.FOUR_STAR,"Not fresh");
        pm.printProductReport(p3);
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
