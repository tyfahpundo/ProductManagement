package zw.co.afrosoft.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Product implements Rateable<Product>, Serializable {
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
    private int id;
    private String name;
    private BigDecimal price;
    private Rating rating;

    Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    Product(int id, String name, BigDecimal price) {
        this(id,name,price, Rating.NOT_RATED);
    }

    public int getId() {
        return id;
    }

//    public void setId(final int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

//    public void setName(final String name) {
//        this.name = name;
//    }

    public BigDecimal getPrice() {
        return price;
    }

//    public void setPrice(final BigDecimal price) {
//        this.price = price;
//    }
    public BigDecimal getDiscount(){

        return price.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }
    @Override
    public Rating getRating() {
        return rating;
    }


    public LocalDate getBestBefore() {
        return LocalDate.now();
    }

    @Override
    public String toString() {
        return "id= " + id +
                ", name= '" + name + '\'' +
                ", price= " + price +
                ", rating= " + rating.getStars() +
                ", bestBefore= " + getBestBefore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id){
            return false;
        } else
            return true;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
