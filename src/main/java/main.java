import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
    private static String pageLink = "https://rozetka.com.ua/produkty/c4624997/page=lowerPageIndex-upperPageIndex;price=2-MaxPrice;sort=novelty/";
    private static String maxPageNumber;
    private static int maxPrise = 10000;
    private static int initialValue = maxPrise;
    private static HashMap<String, Integer> result;

    public static void main(String[] args) throws IOException {
        result = new HashMap<>();
        List<Element> products;
        while (maxPrise >= 2) {
            products = getRandomPage(maxPrise);
            getRandomProduct(products);
            getRandomProduct(products);
            getRandomProduct(products);
        }
        System.out.println("-".repeat(135));
        result.forEach((name, price) -> System.out.printf("%-130s|%s\n", name, price));
        System.out.println("-".repeat(135));
    }

    public static List getRandomPage(int maxPrice) throws IOException {
        maxPageNumber = String.valueOf((int) (Math.random() * 34));
        Document doc;
        doc = Jsoup.connect(
                pageLink.replace("MaxPrice", String.valueOf(maxPrice))
                        .replace("upperPageIndex", maxPageNumber)).get();
        List<Element> elementList = doc.body().getElementsByClass("catalog-grid__cell catalog-grid__cell_type_slim");
        return elementList;
    }

    static public void getRandomProduct(List<Element> productsList) {
        System.out.println("Choosing Carefully" + ".".repeat(100 * maxPrise / initialValue) + " " + maxPrise);
        int index = (int) (Math.random() * (productsList.size()));
        String productName = productsList.get(index).getElementsByClass("goods-tile__title").text().replaceAll("\\([^)]*\\)", "");
        int productPrice = Integer.valueOf(productsList.get(index).getElementsByClass("goods-tile__price-value").text().replaceAll(" ", ""));
        maxPrise -= productPrice;
        result.put(productName, productPrice);
    }

}

