package Service;

import entity.CATEGORIES;
import entity.Product;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GameService {

    final private int PRODUCTS_ON_PAGE = 60;

    CATEGORIES currentCategory;

    public Product startRound(CATEGORIES category)  {
        currentCategory=category;
        try {
            return getRandomProduct(getProductsFromRandomPage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String getToCategory() throws IOException {
        Document doc;
        doc = Jsoup.connect(currentCategory.getLink()).get();
        return doc.getElementsByClass("tile-cats__heading").get(0).attr("href") + "/page=PageNumber;sort=novelty/";
    }

    public  List getProductsFromRandomPage() throws IOException {
        Document doc;
        doc = Jsoup.connect(getToCategory()
                .replace("PageNumber", String.valueOf(Math.random() * 10))
        ).get();
        List<Element> elementList = doc.body().getElementsByClass("catalog-grid__cell catalog-grid__cell_type_slim");
        return elementList;
    }

     public Product getRandomProduct(List<Element> productsList) {
        Element productList = productsList.get((int) (Math.random() * PRODUCTS_ON_PAGE));
        String productName =
                productList.getElementsByClass("goods-tile__title").text()
                .replaceAll("\\([^)]*\\)", ""); //deleting all access codes from Name of the product
        int productPrice =
                Integer.valueOf(productList.getElementsByClass("goods-tile__price-value").text().replaceAll(" ", ""));
         URL photoLink = null;
         try {
             photoLink = new URL(
                     productList.getElementsByClass("lazy_img_hover display-none").attr("data-url"));
         } catch (MalformedURLException e) {
             e.printStackTrace();
         }
         File file = new File("resources/images/");
         try {
             FileUtils.copyURLToFile(photoLink,file);
         } catch (IOException e) {
             e.printStackTrace();
         }
         return new Product(productName,productPrice,file);
    }





}

