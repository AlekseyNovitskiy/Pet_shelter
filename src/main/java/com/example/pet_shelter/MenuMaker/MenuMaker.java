package com.example.pet_shelter.MenuMaker;

import com.example.pet_shelter.configuration.MenuDescription;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class MenuMaker {

    public InlineKeyboardMarkup startMenuKeyboard() {
        InlineKeyboardButton first = new InlineKeyboardButton("\uD83D\uDC08 Кошачий приют").callbackData(MenuDescription.CATSHELTERENTER.name());//сделано
        InlineKeyboardButton second = new InlineKeyboardButton("\uD83D\uDC15 Собачий приют ").callbackData(MenuDescription.DOGSHELTERENTER.name());//сделано
        InlineKeyboardMarkup startKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{first},
                new InlineKeyboardButton[]{second});
        return startKeyboard;
    }

    public InlineKeyboardMarkup afterStartDogKeyBoard() {
        InlineKeyboardButton one = new InlineKeyboardButton("Узнать информацию о приюте").callbackData(MenuDescription.AboutPetShelter.name());//сделано
        InlineKeyboardButton two = new InlineKeyboardButton("Как взять животное из приюта").callbackData(MenuDescription.HOWTOTAKEDOG.name());
        InlineKeyboardButton three = new InlineKeyboardButton("Прислать отчет о питомце").callbackData(MenuDescription.SENDDOGPHOTO.name());//сделано
        InlineKeyboardButton four = new InlineKeyboardButton("Позвать волонтёра").callbackData(MenuDescription.VOLUNTEERCALL.name());//сделано
        InlineKeyboardMarkup startDogKeyBoard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{one},
                new InlineKeyboardButton[]{two},
                new InlineKeyboardButton[]{three},
                new InlineKeyboardButton[]{four});
        return startDogKeyBoard;
    }

    /**
     * <i>Клавиатура menu1</i>
     *
     * @return
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     */
    public InlineKeyboardMarkup menu1Keyboard() {
        InlineKeyboardButton one = new InlineKeyboardButton("рассказать о приюте").callbackData(MenuDescription.AboutPetShelterDocx.name()); //сделано
        InlineKeyboardButton two = new InlineKeyboardButton("расписание работы приюта и адрес, схему проезда").callbackData("SCHEDULE"); //сделано
        InlineKeyboardButton three = new InlineKeyboardButton("техника безопасности на территории приюта").callbackData("SAFETYRULES");
        InlineKeyboardButton four = new InlineKeyboardButton("записать контактные данные для связи").callbackData("WRITECONTACS"); //сделано
        InlineKeyboardButton five = new InlineKeyboardButton("позвать волонтера").callbackData(MenuDescription.VOLUNTEERCALL.name());//сделано
        InlineKeyboardMarkup keyboardMenu1 = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{one},
                new InlineKeyboardButton[]{two},
                new InlineKeyboardButton[]{three},
                new InlineKeyboardButton[]{four},
                new InlineKeyboardButton[]{five});
        return keyboardMenu1;
    }

    /**
     * <i>Клавиатура menu2</i>
     *
     * @return
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     */
    public InlineKeyboardMarkup menu2Keyboard() {
        InlineKeyboardButton first = new InlineKeyboardButton("правила знакомства с собакой").callbackData("1");
        InlineKeyboardButton second = new InlineKeyboardButton("список документов").callbackData("MENULISTOFDOCUMENTS"); //сделано
        InlineKeyboardButton third = new InlineKeyboardButton("советы кинолога").callbackData("CYNOLOGISTADVICE");//сделано
        InlineKeyboardButton fourth = new InlineKeyboardButton("принять и записать контактные данные").callbackData("WRITECONTACS");//сделано
        InlineKeyboardButton fifth = new InlineKeyboardButton("позвать волонтера").callbackData(MenuDescription.VOLUNTEERCALL.name());//сделано
        InlineKeyboardMarkup keyboard2 = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{first},
                new InlineKeyboardButton[]{second},
                new InlineKeyboardButton[]{third},
                new InlineKeyboardButton[]{fourth},
                new InlineKeyboardButton[]{fifth});
        return keyboard2;
    }

    /**
     * <i>Клавиатура menu3</i>
     *
     * @return
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     */
    public InlineKeyboardMarkup menu3Keyboard() {
        InlineKeyboardButton first = new InlineKeyboardButton("отправить отчет").callbackData(MenuDescription.SENDDOGPHOTO.name());//сделано
        InlineKeyboardMarkup keyboard2 = new InlineKeyboardMarkup(
                first);
        return keyboard2;
    }


    /**
     * <i>Клавиатура списка правил</i>
     *
     * @param chatId идентификатор чата
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     * @see com.pengrad.telegrambot.TelegramBot
     */
    public InlineKeyboardMarkup inlineButtonsListOfRules(Long chatId) {
        InlineKeyboardButton first = new InlineKeyboardButton("документы, чтобы взять собаку").callbackData("6");
        InlineKeyboardButton second = new InlineKeyboardButton("Транспортировка животного").callbackData("7");
        InlineKeyboardButton third = new InlineKeyboardButton("Обустройство дома для щенка").callbackData("8");
        InlineKeyboardButton fourth = new InlineKeyboardButton("Обустройство дома для взрослой собаки").callbackData("9");
        InlineKeyboardButton fifth = new InlineKeyboardButton("Для собаки с ограниченными возможностями").callbackData("10");
        InlineKeyboardButton six = new InlineKeyboardButton("причины отказа ").callbackData("11");
        InlineKeyboardMarkup recommendationKeyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{first},
                new InlineKeyboardButton[]{second},
                new InlineKeyboardButton[]{third},
                new InlineKeyboardButton[]{fourth},
                new InlineKeyboardButton[]{fifth},
                new InlineKeyboardButton[]{six});
        return recommendationKeyboard;
    }

    /**
     * <i>Кнопка отклика кинолога</i>
     *
     * @param chatId идентификатор чата
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     */
    public InlineKeyboardMarkup buttonCynologist(Long chatId) {
        InlineKeyboardButton first = new InlineKeyboardButton("Советуем этих кинологов").callbackData("buttonCynologist");
        InlineKeyboardMarkup cynologistKeyboard = new InlineKeyboardMarkup(
                first);
        return cynologistKeyboard;
    }
}
