import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class main {
    final static String[] categories ={
            "https://rozetka.com.ua/computers-notebooks/c80253/",
            "https://rozetka.com.ua/telefony-tv-i-ehlektronika/c4627949/",
            "https://bt.rozetka.com.ua/",
            "https://rozetka.com.ua/tovary-dlya-doma/c2394287/",
            "https://rozetka.com.ua/instrumenty-i-avtotovary/c4627858/",
            "https://rozetka.com.ua/santekhnika-i-remont/c4628418/",
            "https://rozetka.com.ua/dacha-sad-ogorod/c2394297/",
            "https://rozetka.com.ua/sport-i-uvlecheniya/c4627893/",
            "https://rozetka.com.ua/krasota-i-zdorovje/c4629305/",
            "https://rozetka.com.ua/kids/c88468/",
            "https://rozetka.com.ua/office-school-books/c4625734/",
            "https://rozetka.com.ua/tovary-dlya-biznesa/c4627851/"
    };



    public static void main(String[] args) throws IOException {
        System.out.println(getRandomProduct(getProductsFromRandomPage()));
    }



//    public static void main(String[] args) throws IOException {
//        result = new HashMap<>();
//        List<Element> products;
//        while (maxPrise >= 2) {
//            products = getRandomPage(maxPrise);
//            getRandomProduct(products);
//            getRandomProduct(products);
//            getRandomProduct(products);
//        }
//        System.out.println("-".repeat(135));
//        result.forEach((name, price) -> System.out.printf("%-130s|%s\n", name, price));
//        System.out.println("-".repeat(135));
//    }

    public static List getProductsFromRandomPage() throws IOException {
        Document doc;
        doc = Jsoup.connect(getToRandomCategorie()
                .replace("PageNumber",String.valueOf(Math.random()*10))
                ).get();
        List<Element> elementList = doc.body().getElementsByClass("catalog-grid__cell catalog-grid__cell_type_slim");
        return elementList;
    }

    static public String getRandomProduct(List<Element> productsList) {
        int index = (int) (Math.random() * (productsList.size()));
        String productName = productsList.get(index).getElementsByClass("goods-tile__title").text().replaceAll("\\([^)]*\\)", "");
        int productPrice = Integer.valueOf(productsList.get(index).getElementsByClass("goods-tile__price-value").text().replaceAll(" ", ""));
        String imgRef = productsList.get(index).getElementsByClass("lazy_img_hover display-none").attr("data-url");
        return productName+"\n"+productPrice+"\n"+imgRef;
        }

    public static String getToRandomCategorie() throws IOException {
        Document doc;
        doc =Jsoup.connect(categories[(int) (Math.random()*12)]).get();
        return doc.getElementsByClass("tile-cats__heading").get(0).attr("href")+"/page=PageNumber;sort=novelty/";


    }

}

