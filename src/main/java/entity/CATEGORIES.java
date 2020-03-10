package entity;

public enum CATEGORIES {
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