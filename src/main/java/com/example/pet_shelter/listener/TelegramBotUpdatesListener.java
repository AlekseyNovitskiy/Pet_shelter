package com.example.pet_shelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Value("${nameFileAboutTheNursery}")
    String NAME_FILE_ABOUT_THE_NURSERY; // Место расположения файла информации о питомнике
    int NUMBER_SIMVOLOV_READ_FILE_ABOUT_THE_NURSERY = 2048; // Максимально количество символов считываемое из файла *Информация о приюте*

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null)
                .filter(update -> update.message().text() != null)
                .forEach(this::processUpdate);
        logger.info("Processing update: {}", updates);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        String userMessage = update.message().text();
        Long chatId = update.message().chat().id();
        switch (userMessage) {
            case "/start":
                telegramBot.execute(new SendMessage(chatId, "Какое-то приветственное сообщение, выберите команду из меню"));
                break;
            case "/menu1":
                InlineKeyboardButton button1 = new InlineKeyboardButton("Узнать информацию о приюте").callbackData("option1"); //понять как вытащить ответ по нажатию. callbackdata или что-то другое
                InlineKeyboardButton button2 = new InlineKeyboardButton("Как взять собаку из приюта").callbackData("option2");
                InlineKeyboardButton button3 = new InlineKeyboardButton("Прислать отчет о питомце").callbackData("option2");
                InlineKeyboardButton button4 = new InlineKeyboardButton("Позвать волонтера").callbackData("option2");
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{button1},
                        new InlineKeyboardButton[]{button2},
                        new InlineKeyboardButton[]{button3},
                        new InlineKeyboardButton[]{button4});
                SendMessage sendMessage = new SendMessage(chatId, "Please select an option:").replyMarkup(inlineKeyboard);
                telegramBot.execute(sendMessage);
                break;
            case "/1":
                AboutTheNursery(chatId);
                break;
        }
    }

    // Информация о питомнике *считывание информации о питомнике и вывод в чат Bot
    private void AboutTheNursery(long chatId) {
        char[] buf = new char[NUMBER_SIMVOLOV_READ_FILE_ABOUT_THE_NURSERY];
        try (FileReader reader = new FileReader(NAME_FILE_ABOUT_THE_NURSERY)) {
            reader.read(buf);
        } catch (IOException ex) {
        }
        telegramBot.execute(new SendMessage(chatId, new String(buf)));
    }
}
