package com.example.pet_shelter.listener;

import com.example.pet_shelter.configuration.MenuDescription;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Value("${nameFileAboutTheNursery}")
    String NAME_FILE_ABOUT_THE_NURSERY; // Место расположения файла информации о приюте
    int NUMBER_CHARACTERS_READ_FILE_ABOUT_THE_NURSERY = 2048; // Максимально количество символов считываемое из файла *Информация о приюте*

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
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
            if (data.equals(MenuDescription.Option1.name())) {
                telegramBot.execute(new SendMessage(chatId, MenuDescription.Option1.getValue()));
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
                InlineKeyboardButton button1 = new InlineKeyboardButton("Узнать информацию о приюте").callbackData(MenuDescription.Option1.name());
                InlineKeyboardButton button2 = new InlineKeyboardButton("Как взять собаку из приюта").callbackData(MenuDescription.Option2.name());
                InlineKeyboardButton button3 = new InlineKeyboardButton("Прислать отчет о питомце").callbackData(MenuDescription.Option3.name());
                InlineKeyboardButton button4 = new InlineKeyboardButton("Позвать волонтера").callbackData(MenuDescription.Option4.name());
                InlineKeyboardMarkup inlineKeyboardMenu1 = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{button1},
                        new InlineKeyboardButton[]{button2},
                        new InlineKeyboardButton[]{button3},
                        new InlineKeyboardButton[]{button4});

                SendMessage sendMessage = new SendMessage(chatId, "Please select an option:").replyMarkup(inlineKeyboardMenu1);
                telegramBot.execute(sendMessage);
                break;
            case "/menu2":
                greetings(chatId,update);
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
            case "/menu3":
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



    // Информация о питомнике *считывание информации о питомнике и вывод в чат Bot
    private void AboutTheNursery(long chatId) {
        char[] buf = new char[NUMBER_CHARACTERS_READ_FILE_ABOUT_THE_NURSERY];
        try (FileReader reader = new FileReader(NAME_FILE_ABOUT_THE_NURSERY)) {
            reader.read(buf);
        } catch (IOException ex) {
        }
        telegramBot.execute(new SendMessage(chatId, new String(buf)));
    }
}
