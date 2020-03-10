package bot;

import Service.GameService;
import entity.CATEGORIES;
import entity.Product;
import org.jsoup.internal.StringUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;


public class Bot extends TelegramLongPollingBot {

    private GameService gameService;
    private boolean gameIsGoing = false;
    private boolean questionIsAsked = false;
    private HashMap<User, Integer> currentGuessers;
    private HashMap<User, Integer> playingUsers;
    private Product currentProduct;

    @Override
    public void onUpdateReceived(Update update) {
        String messageText = update.getMessage().getText();
        if (!gameIsGoing) {
            startBot(update);
        } else if (messageText.equals("/register")) {
            try {
                registerUser(update);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (!questionIsAsked) {
            if (messageText.contains("guess") && playingUsers.size() >= 1)
                askQuestion(update);
        }else if (messageText.equals("/endRound")) {
            getWinner(update);
        }
        else if (messageText.equals("/winner")) {
            System.out.println("final Stage");
        } else makeGuesse(update);
    }

    private void increaseScore(User user) {
        int currentScore = playingUsers.get(user);
        playingUsers.put(user, currentScore + 1);
    }

    private boolean getWinner(Update update) {
        int productPrice = currentProduct.getPrice();
        int minDistance = productPrice;
        User winner = null;
        for (Map.Entry<User, Integer> entry : currentGuessers.entrySet()) {
            if (productPrice - entry.getValue() < minDistance) {
                minDistance = productPrice - entry.getValue();
                winner = entry.getKey();
            }
        }
        increaseScore(winner);
        SendMessage message = new SendMessage();
        message.setText(winner.getUserName() + " gПОБЕДЮШКА!");
        message.setChatId(update.getMessage().getChatId());
        questionIsAsked = false;
        try {
            execute(message);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void registerUser(Update update) throws TelegramApiException {
        System.out.println("register");
        User user = update.getMessage().getFrom();
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        if (playingUsers == null) {
            playingUsers = new HashMap<>();
        }
        if (!playingUsers.containsKey(user)) {
            playingUsers.put(user, 0);
            message.setText(user.getUserName() + " Покатились, Розбишака!!!");
        } else {
            message.setText("Ты уже играешь, Жулик!");
        }
        execute(message);
    }

    private boolean askQuestion(Update update) {
        currentGuessers = playingUsers;
        boolean processionResult = true;
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        switch (messageText) {
            case ("/guessGarden"):
                currentProduct = gameService.startRound(CATEGORIES.GARDEN);
                break;
            case ("/guessHealth"):
                currentProduct = gameService.startRound(CATEGORIES.HEALTH);
                break;
            default:
                System.out.println("default case");
                break;
        }
        try {
            System.out.println(currentProduct.getPhoto().getAbsolutePath());
            SendPhoto photoMessage = new SendPhoto();
            photoMessage.setChatId(chatId);
            photoMessage.setCaption(currentProduct.getDescription());
            photoMessage.setPhoto(currentProduct.getPhoto());
            execute(photoMessage);
            questionIsAsked = true;
        } catch (TelegramApiException e) {
            processionResult = false;
            e.printStackTrace();
        }
        return processionResult;
    }

    private boolean startBot(Update update) {
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

    private void makeGuesse(Update update) {
        String message = update.getMessage().getText().substring(1);
        if (StringUtil.isNumeric(message.substring(1))) {
            currentGuessers.put(update.getMessage().getFrom(),
                    Integer.parseInt(message));
        }
    }

    @Override
    public String getBotUsername() {
        return "foodMarketGuessingBot";
    }

    @Override
    public String getBotToken() {
        return "1118538657:AAGE07-mLLLXJp2tiHearfg8EbALIsxBXI0";
    }

    private String botInit() {
        gameService = new GameService();
        gameIsGoing = true;
        return "Hi there! Beware the power of grand RANDOM GENERATION!\n" +
                "To Start a game choose one of the 12 categories of products to guess from->";
    }


}