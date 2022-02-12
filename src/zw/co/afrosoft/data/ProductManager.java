package zw.co.afrosoft.data;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {
    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;
    private ResourceBundle config = ResourceBundle.getBundle("zw.co.afrosoft.data.config");
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private Path reportsFolder = Path.of(config.getString("reports.folder"));
    private Path dataFolder = Path.of(config.getString("data.folder"));
    private Path tempFolder = Path.of(config.getString("temp.folder"));

    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private static Map<String,ResourceFormatter> formatters =
            Map.of("en-GB", new ResourceFormatter(Locale.UK),
                    "en-US", new ResourceFormatter(Locale.US));
    private static final Logger log = Logger.getLogger(ProductManager.class.getName());
    public ProductManager() {
    }

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }
    public ProductManager(String languageTag) {
        changeLocale(languageTag);
        loadAllData();
    }
    public void changeLocale(String languageTag){
        formatter = formatters.getOrDefault(languageTag,formatters.get("en-GB"));
    }
    public static Set<String> getSupportedLocales(){
        return formatters.keySet();
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore){
        Product product = new Food(id,name,price,rating,bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }
    public Product createProduct(int id, String name, BigDecimal price, Rating rating){
        Product product = new Drink(id,name,price,rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }
    public Product reviewProduct(int id, Rating rating, String comments){
        try {
            return reviewProduct(findProduct(id),rating,comments);
        } catch (ProductManagerException e) {
            log.info(e.getMessage());
        }
        return null;
    }
    public Product reviewProduct(Product product, Rating rating, String comments){
        List<Review> reviews = products.get(product);
        products.remove(product,reviews);
        reviews.add(new Review(rating,comments));
        product = product.applyRating(
                Rateable.convert(
                        (int) Math.round(
                                reviews.stream()
                                        .mapToInt(r-> r.getRating().ordinal())
                                        .average()
                                        .orElse(0))));
        products.put(product,reviews);
        return product;
    }
    public Product findProduct(int id) throws ProductManagerException {
        return products.keySet()
                .stream()
                .filter(product -> product.getId()==id)
                .findFirst()
                .orElseThrow(()-> new ProductManagerException("Product with id "+id+" not found!"));
    }
    public void printProductReport(int id){
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            log.severe("Error printing product report "+ e.getMessage());
        }
    }
    public void printProductReport(Product product) throws IOException{
        List<Review> reviews = products.get(product);
        Path productFile = reportsFolder.resolve(MessageFormat.format(config.getString("report.file"),product.getId()));
        try(PrintWriter out = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE),"UTF-8"))){
            out.append(formatter.formatProduct(product)).append(System.lineSeparator());
            Collections.sort(reviews);
            if(reviews.isEmpty()) {
                out.append(formatter.getText("no.reviews")).append(System.lineSeparator());
            }else{
                out.append(reviews.stream()
                        .map(review -> formatter.formatReview(review)+ System.lineSeparator())
                        .collect(Collectors.joining()));
            }
        }
    }
    private Review parseReview(String txt){
        Review review = null;
        try {
            Object[] values = reviewFormat.parse(txt);
            review = new Review(Rateable.convert(Integer.parseInt((String)values[0])), (String)values[1]);
        } catch (ParseException | NumberFormatException e) {
            log.warning("Error while parsing the review "+txt);
        }
        return review;
    }
    private void dumpData(){
        try{
            if(Files.notExists(tempFolder)){
                Files.createDirectory(tempFolder);
            }
            Path tempFile = tempFolder.resolve(MessageFormat.format(config.getString("temp.file"), Instant.now()));
            try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tempFile,StandardOpenOption.CREATE))){
                out.writeObject(products);
                products = new HashMap<>();
            }
        } catch (IOException e) {
            log.severe("Error While dumping data "+ e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    private void restoreData(){
        try{
            Path tempFile = Files.list(tempFolder)
                    .filter(path -> path.getFileName().toString().endsWith("tmp"))
                    .findFirst().orElseThrow();
            try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))){
                products = (HashMap) in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            log.severe("Error while loading data "+e.getMessage());
        }
    }
    private void loadAllData(){
        try {
            products = Files.list(dataFolder)
                    .filter(file -> file.getFileName().startsWith("product"))
                    .map(this::loadProduct)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(product -> product,
                            this::loadReviews));
        } catch (IOException e) {
            log.severe("Error while loading data "+ e.getMessage());
        }
    }
    private Product loadProduct(Path file){
        Product product = null;
        try {
            product = parseProduct(Files.lines(dataFolder.resolve(file), StandardCharsets.UTF_8).findFirst().orElseThrow());
        } catch (IOException e) {
            log.warning("Error wile loading the products "+ e.getMessage());
        }
        return product;
    }
    private List<Review> loadReviews(Product product){
        List<Review> reviews = null;
        Path file = dataFolder.resolve(MessageFormat.format(config.getString("reviews.data.file"),product.getId()));
        if(Files.notExists(file)){
            reviews = new ArrayList<>();
        }
        else{
            try {
                reviews = Files.lines(file, StandardCharsets.UTF_8)
                        .map(this::parseReview)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                log.warning("Error loading the reviews "+ e.getMessage());
            }
        }
        return reviews;
    }
    private Product parseProduct(String txt){
        Product product = null;
        try {
            Object[] values = productFormat.parse(txt);
            int id = Integer.parseInt((String)values[1]);
            String name = (String)values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String)values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String)values[4]));
            switch((String)values[0]){
                case "D":
                    product = new Drink(id,name,price,rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String)values[5]);
                    product = new Food(id,name,price,rating,bestBefore);
            }
        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
            log.warning("Error while parsing the product "+txt+ e.getMessage());
        }
        return product;
    }
    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter){
        StringBuilder txt = new StringBuilder();
        products.keySet().stream()
                .sorted(sorter)
                .filter(filter)
                .forEach(product -> txt.append(formatter.formatProduct(product) + '\n'));
        System.out.println(txt);
    }

    private static class ResourceFormatter{
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        public ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("zw.co.afrosoft.data.resources",locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }
        private String formatProduct(Product product){
            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore())
            );
        }
        private String formatReview(Review review){
            return MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }
        private String getText(String key){
            return resources.getString(key);
        }
    }
}
