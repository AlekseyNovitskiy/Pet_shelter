package com.example.pet_shelter.listener;

import com.example.pet_shelter.configuration.MenuDescription;
import com.example.pet_shelter.service.UsersService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.io.File;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    UsersService usersService;

    public TelegramBotUpdatesListener(UsersService usersService) {
        this.usersService = usersService;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Value("${nameFileAboutTheNursery}")
    String NAME_FILE_ABOUT_THE_NURSERY; // Место расположения файла информации о приюте
    int NUMBER_CHARACTERS_READ_FILE_ABOUT_THE_NURSERY = 2048; // Максимально количество символов считываемое из файла *Информация о приюте*

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        menuButton();
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .forEach(update -> {
                    if (update.message() != null && update.message().text() != null) {
                        Long chatId = update.message().chat().id();
                        processUpdate(chatId, update);
                    }
                    if (update.callbackQuery() != null) {
                        Long chatId = update.callbackQuery().message().chat().id();
                        callBackUpd(chatId, update);
                        callBackUpdThirdMenu(chatId, update);
                        callBackUpdSecondMenu(chatId,update);
                    }
                });
        logger.info("Processing update: {}", updates);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void callBackUpd(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals("15")) {
            } else if (data.equals(MenuDescription.Option2.name())
            ) {
                telegramBot.execute(new SendMessage(chatId, MenuDescription.Option2.getValue()));
            } else if (data.equals(MenuDescription.Option3.name())) {
                telegramBot.execute(new SendMessage(chatId, MenuDescription.Option3.getValue()));
            } else if (data.equals(MenuDescription.Option4.name())) {
                telegramBot.execute(new SendMessage(chatId, MenuDescription.Option4.getValue()));
            }
        }
    }
    private void callBackUpdSecondMenu(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals("12")) {
                AboutTheNursery(chatId);
            }
            if (data.equals("13")) {
                sendLocationPhoto(chatId);
            }
        }
    }

    private void callBackUpdThirdMenu(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals("2")) {
                inlineButtonsListOfRules(chatId);
            } else if (data.equals("3")) {
                buttonCynologist(chatId);
            } else if (data.equals("6")) {
                sendPetsDocuments(chatId);
            }
        }
    }

    private void processUpdate(Long chatId, Update update) {
        String userMessage = update.message().text();
        switch (userMessage) {
            case "/start":
                telegramBot.execute(new SendMessage(chatId, "Какое-то приветственное сообщение, выберите команду из меню"));
                break;
            case "/menu1":
                InlineKeyboardButton one = new InlineKeyboardButton("рассказать о приюте").callbackData("12");
                InlineKeyboardButton two = new InlineKeyboardButton("расписание работы приюта и адрес, схему проезда").callbackData("13");
                InlineKeyboardButton three = new InlineKeyboardButton("техника безопасности на территории приюта").callbackData("14");
                InlineKeyboardButton four = new InlineKeyboardButton("записать контактные данные для связи").callbackData("15");
                InlineKeyboardButton five = new InlineKeyboardButton("позвать волонтера").callbackData("15");
                InlineKeyboardMarkup keyboardMenu2 = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{one},
                        new InlineKeyboardButton[]{two},
                        new InlineKeyboardButton[]{three},
                        new InlineKeyboardButton[]{four},
                        new InlineKeyboardButton[]{five});
                SendMessage sendMsg = new SendMessage(chatId, "Please select an option:").replyMarkup(keyboardMenu2);
                telegramBot.execute(sendMsg);
                break;
            case "/menu2":
                greetings(chatId,update);
                InlineKeyboardButton first = new InlineKeyboardButton("правила знакомства с собакой").callbackData("1");
                InlineKeyboardButton second = new InlineKeyboardButton("список документов").callbackData("2");
                InlineKeyboardButton third = new InlineKeyboardButton("советы кинолога").callbackData("3");
                InlineKeyboardButton fourth = new InlineKeyboardButton("принять и записать контактные данные").callbackData("4");
                InlineKeyboardButton fifth = new InlineKeyboardButton("позвать волонтера").callbackData("5");
                InlineKeyboardMarkup keyboard3 = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{first},
                        new InlineKeyboardButton[]{second},
                        new InlineKeyboardButton[]{third},
                        new InlineKeyboardButton[]{fourth},
                        new InlineKeyboardButton[]{fifth});
                SendMessage msg = new SendMessage(chatId, "Please select an option:").replyMarkup(keyboard3);
                telegramBot.execute(msg);
                break;
            case "/menu3":
                SendMessage msg3 = new SendMessage(chatId, "Отчет о питомце:");
                telegramBot.execute(msg3);
                break;
            case "/menu4":
                SendMessage msg4 = new SendMessage(chatId, "Волонтер идёт !");
                telegramBot.execute(msg4);
                break;
        }
    }

    public void inlineButtonsListOfRules(Long chatId) {
        InlineKeyboardButton first = new InlineKeyboardButton("документы, чтобы взять собаку").callbackData("6");
        InlineKeyboardButton second = new InlineKeyboardButton("Транспортировка животного").callbackData("7");
        InlineKeyboardButton third = new InlineKeyboardButton("Обустройство дома для щенка").callbackData("8");
        InlineKeyboardButton fourth = new InlineKeyboardButton("Обустройство дома для взрослой собаки").callbackData("9");
        InlineKeyboardButton fifth = new InlineKeyboardButton("Для собаки с ограниченными возможностями").callbackData("10");
        InlineKeyboardButton six = new InlineKeyboardButton("причины отказа ").callbackData("11");
        InlineKeyboardMarkup recomendationKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{first},
                new InlineKeyboardButton[]{second},
                new InlineKeyboardButton[]{third},
                new InlineKeyboardButton[]{fourth},
                new InlineKeyboardButton[]{fifth},
                new InlineKeyboardButton[]{six});
        SendMessage msg = new SendMessage(chatId, "Please select an option:").replyMarkup(recomendationKeyboard);
        telegramBot.execute(msg);
    }

    public void buttonCynologist(Long chatId) {
        InlineKeyboardButton first = new InlineKeyboardButton("Советуем этих кинологов").callbackData("buttonCynologist");
        InlineKeyboardMarkup cynologistKeyboard = new InlineKeyboardMarkup(
                first);
        SendMessage msg = new SendMessage(chatId, "Please select an option:").replyMarkup(cynologistKeyboard);
        telegramBot.execute(msg);
    }

    public void greetings(Long chatId,Update update) {
        String firstName = update.message().chat().firstName();
        String lastName = update.message().chat().lastName();
        telegramBot.execute(new SendMessage(chatId, "Привет " + firstName + " " + lastName));
    }

    public void menuButton() {
        SetMyCommands message = new SetMyCommands(
                new BotCommand("/start","Запустить бота"),
                new BotCommand("/menu1","Узнать информацию о приюте"),
                new BotCommand("/menu2","Как взять собаку из приюта"),
                new BotCommand("/menu3","Прислать отчет о питомце"),
                new BotCommand("/menu4","Позвать волонтера"));//сделать отклик
        telegramBot.execute(message);
    }


    // Информация о питомнике *считывание информации о питомнике и вывод в чат Bot
    private void AboutTheNursery(long chatId) {
        char[] buf = new char[NUMBER_CHARACTERS_READ_FILE_ABOUT_THE_NURSERY];
        try (FileReader reader = new FileReader(NAME_FILE_ABOUT_THE_NURSERY)) {
            reader.read(buf);
        } catch (IOException ex) {
        }
        telegramBot.execute(new SendMessage(chatId, new String(buf)));
    }

    public void sendLocationPhoto(Long chatId) {
        float latitude = (float) 59.82225018231752;
        float longitude = (float) 30.178212453672643;
        telegramBot.execute(new SendMessage(chatId, "Мы работаем по такому графику, наш адрес такой-то"));
        java.io.File file = new File("1.png");
        telegramBot.execute(new SendPhoto(chatId, file));
        telegramBot.execute(new SendMessage(chatId, "Или используйте навигатор"));
        SendLocation location = new SendLocation(chatId, latitude, longitude);
        telegramBot.execute(location);
    }

    public void sendPetsDocuments(Long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Файл со списком нужных документов"));
        java.io.File file = new File("Документы, чтобы взять собаку.docx");
        telegramBot.execute(new SendDocument(chatId, file));
    }

}
