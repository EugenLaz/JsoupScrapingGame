import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class main {

    public static void main(String[] args) throws IOException {
        System.out.println(getRandomProduct(getProductsFromRandomPage()));
    }

    public static String getToСategory(CATEGORIES category) throws IOException {
        Document doc;
        doc = Jsoup.connect(category.getLink()).get();
        return doc.getElementsByClass("tile-cats__heading").get(0).attr("href") + "/page=PageNumber;sort=novelty/";
    }

    public static List getProductsFromRandomPage() throws IOException {
        Document doc;
        doc = Jsoup.connect(getToСategory(CATEGORIES.HEALTH)
                .replace("PageNumber", String.valueOf(Math.random() * 10))
        ).get();
        List<Element> elementList = doc.body().getElementsByClass("catalog-grid__cell catalog-grid__cell_type_slim");
        return elementList;
    }

    static public String getRandomProduct(List<Element> productsList) {
        int productPrice;
        String productName;
        String imgRef;
        //60 is the default amount of products on one page
        Element product = productsList.get((int) (Math.random() * 60));
        productName = product.getElementsByClass("goods-tile__title").text()
                .replaceAll("\\([^)]*\\)", ""); //deleting all access codes from Name of the product
        productPrice = Integer.valueOf(
                product.getElementsByClass("goods-tile__price-value").text().replaceAll(" ", ""));
        imgRef = product.getElementsByClass("lazy_img_hover display-none").attr("data-url");
        return productName + "\n" + productPrice + "\n" + imgRef;
    }

    enum CATEGORIES {
        COMPUTERS("https://rozetka.com.ua/computers-notebooks/c80253/"),
        PHONES("https://rozetka.com.ua/telefony-tv-i-ehlektronika/c4627949/"),
        Appliances("https://bt.rozetka.com.ua/"),
        HOUSEHOLD("https://rozetka.com.ua/tovary-dlya-doma/c2394287/"),
        INSTRUMENTS("https://rozetka.com.ua/instrumenty-i-avtotovary/c4627858/"),
        REPAIRS("https://rozetka.com.ua/santekhnika-i-remont/c4628418/"),
        GARDEN("https://rozetka.com.ua/dacha-sad-ogorod/c2394297/"),
        SPORT("https://rozetka.com.ua/sport-i-uvlecheniya/c4627893/"),
        HEALTH("https://rozetka.com.ua/krasota-i-zdorovje/c4629305/"),
        KIDS("https://rozetka.com.ua/kids/c88468/"),
        OFFICE("https://rozetka.com.ua/office-school-books/c4625734/"),
        BUISNESS("https://rozetka.com.ua/tovary-dlya-biznesa/c4627851/");

        private final String link;

        CATEGORIES(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
        }
    }



}

