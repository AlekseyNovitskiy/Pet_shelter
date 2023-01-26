package com.example.pet_shelter.listener;

import com.example.pet_shelter.configuration.MenuDescription;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

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
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{button1},
                        new InlineKeyboardButton[]{button2},
                        new InlineKeyboardButton[]{button3},
                        new InlineKeyboardButton[]{button4});

                SendMessage sendMessage = new SendMessage(chatId, "Please select an option:").replyMarkup(inlineKeyboard);
                telegramBot.execute(sendMessage);
                break;
        }
    }
}
