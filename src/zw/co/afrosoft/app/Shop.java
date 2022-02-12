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
//        pm.createProduct(101,"Tea",BigDecimal.valueOf(1.99), Rating.NOT_RATED);
//        pm.reviewProduct(101,Rating.FOUR_STAR,"Nice hot cup of tea");
//        pm.reviewProduct(101,Rating.TWO_STAR,"Rather weak tea");
//        pm.reviewProduct(101,Rating.THREE_STAR,"Good tea");
//        pm.reviewProduct(101,Rating.ONE_STAR,"Hot cup of tea");
//        pm.reviewProduct(101,Rating.THREE_STAR,"Just regular tea");
//        pm.reviewProduct(101,Rating.FIVE_STAR,"Not that hot though");
//        pm.printProductReport(101);
//        pm.dumpData();
//        pm.restoreData();
//        pm.printProductReport(101);
//
//
//        pm.printProducts(p->p.getPrice().floatValue()<2, (p1,p2)-> p2.getRating().ordinal()-p1.getRating().ordinal());
//
//        Comparator<Product> ratingSorter = (p1,p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
//        Comparator<Product> priceSorter = (p1,p2) -> p2.getPrice().compareTo(p1.getPrice());

    }
}
