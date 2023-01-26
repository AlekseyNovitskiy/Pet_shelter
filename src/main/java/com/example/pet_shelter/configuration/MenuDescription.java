package com.example.pet_shelter.configuration;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;

public enum MenuDescription {
    Option1("Информация о приюте"),
    Option2("Как взять собаку из приюта"),
    Option3("Прислать отчет о питомце"),
    Option4("Позвать волонтера");

    private final String value;

    public String getValue() {
        return value;
    }

    MenuDescription(String value) {
        this.value = value;
    }
}
