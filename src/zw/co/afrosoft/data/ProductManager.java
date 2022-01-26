package zw.co.afrosoft.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductManager {
    private Product product;
    private Review review;

    private Locale locale;
    private ResourceBundle resources;
    private DateTimeFormatter dateFormat;
    private NumberFormat moneyFormat;

    public ProductManager() {
    }

    public ProductManager(Locale locale) {
        this.locale = locale;
        resources = ResourceBundle.getBundle("zw.co.afrosoft.data.resources",locale);
        dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        moneyFormat = NumberFormat.getCurrencyInstance(locale);
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore){
        product = new Food(id,name,price,rating,bestBefore);
        return product;
    }
    public Product createProduct(int id, String name, BigDecimal price, Rating rating){
        product = new Drink(id,name,price,rating);
        return product;
    }
    public Product reviewProduct(Product product, Rating rating, String comments){
        review = new Review(rating,comments);
        this.product = product;
        return this.product;
    }
    public void printProductReport(){
        StringBuilder txt = new StringBuilder();
        txt.append(MessageFormat.format(resources.getString("product"),
                product.getName(),
                moneyFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateFormat.format(product.getBestBefore())
                ));
        txt.append('\n');
        if(review != null){
            txt.append(MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments()));
        }
        else{
            txt.append(resources.getString("no.reviews"));
        }
        txt.append('\n');
    }
}
