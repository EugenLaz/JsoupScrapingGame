package bot;

import Service.GameService;
import entity.CATEGORIES;
import entity.Product;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.HashMap;


public class Bot extends TelegramLongPollingBot {

    private GameService gameService;
    private boolean gameIsGoing = false;
    private boolean questionIsAsked = false;
    private HashMap<User, Integer> currentGuessers;
    private HashMap<User, Integer> playingUsers;

    @Override
    public void onUpdateReceived(Update update) {
        String messageText = update.getMessage().getText();
        if (!gameIsGoing) {
            tryToStartBot(update);
        }
        else if (!questionIsAsked) {
            if (messageText.contains("guess"))
                tryToAskQuestion(update);
            else if (messageText.contains("/register")) {
                registerUser(update);
            }
        }
        else if(messageText.contains("/endRound")){

        }
    }

    private void registerUser(Update update) {
        User user = update.getMessage().getFrom();
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        if (!playingUsers.containsKey(user)) {
            playingUsers.put(user, 0);
            message.setText(user.getUserName() + " Покатились, Розбишака!!!");
        } else message.setText("Ты уже играешь, Жулик!");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean tryToAskQuestion(Update update) {
        currentGuessers = playingUsers;
        boolean processionResult = true;
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        Product product;
        switch (messageText) {
            case ("/guessGarden"):
                product = gameService.startRound(CATEGORIES.GARDEN);
                break;
            case ("/guessHealth"):
                product = gameService.startRound(CATEGORIES.HEALTH);
                break;
            default:
                product = new Product("ERROR", -1, new File("https://developers.google.com/maps/documentation/maps-static/images/error-image-generic.png"));
        }
        try {
            SendPhoto photoMessage = new SendPhoto();
            photoMessage.setChatId(chatId);
            photoMessage.setCaption(product.getDescription());
            photoMessage.setPhoto(product.getPhoto());
            execute(photoMessage);
        } catch (TelegramApiException e) {
            processionResult = false;
            e.printStackTrace();
        }
        return processionResult;
    }


    private boolean tryToStartBot(Update update) {
        boolean processionResult = true;
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        if (!gameIsGoing) {
            if (messageText.equals("/sayHi"))
                message.setText(botInit()); //Creating a game Service and getting a message with further instructions
            else message.setText("Firstly you should welcome the game. Maybe you would like to '/sayHi'");
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            processionResult = false;
            e.printStackTrace();
        }
        return processionResult;
    }

    @Override
    public String getBotUsername() {
        return "foodMarketGuessingBot";
    }

    @Override
    public String getBotToken() {
        return "xxxxxxxx:xxxxx-xxxxx-xxxx";
    }

    private String botInit() {
        gameService = new GameService();
        gameIsGoing = true;
        return "Hi there! Beware the power of grand RANDOM GENERATION!\n" +
                "To Start a game choose one of the 12 categories of products to guess from->";
    }


}