package com.example.pet_shelter.listener;

import com.example.pet_shelter.MenuMaker.MenuMaker;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.service.UsersService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final UsersService usersService;

    private final MenuMaker menuMaker;

    public TelegramBotUpdatesListener(UsersService usersService, MenuMaker menuMaker) {
        this.usersService = usersService;
        this.menuMaker = menuMaker;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private int flagButton; // Флаг нажатия кнопки

    @Value("${nameFileAboutTheNursery}")
    String NAME_FILE_ABOUT_THE_NURSERY; // Место расположения файла информации о приюте
    int NUMBER_CHARACTERS_READ_FILE_ABOUT_THE_NURSERY = 2048; // Max символов считываемое из файла *Информация о приюте*

    @Autowired
    private TelegramBot telegramBot;

    /**
     * <i>Инициализация бота<i/> <br>
     * Инициализация меню {@code menuButton()}
     */
    @PostConstruct
    public void init() {
        menuButton();
        telegramBot.setUpdatesListener(this);
    }

    /**
     * <i>Обработка возможных апдейтов<i/>
     *
     * @param updates возможные апдейты
     * @return
     */
    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .forEach(update -> {

                    try {
                        if (update.message() != null && update.message().photo() != null)    // если фото приложено
                        {
                            Long chatId = update.message().chat().id();
                            telegramBot.execute(new SendMessage(chatId, "Начало загрузки"));
                            downloadPhotoFromChat(update);
                            telegramBot.execute(new SendMessage(chatId, "Успешная загрузка"));
                        }
                    } catch (Exception e) {
                        Long chatId = update.message().chat().id();
                        telegramBot.execute(new SendMessage(chatId, "Ошибка, очень странно, сообщите мы разберемся!"));
                    }
                    if (update.message() != null && update.message().text() != null) {
                        Long chatId = update.message().chat().id();
                        processUpdate(chatId, update);
                    }
                    if (update.callbackQuery() != null) {
                        Long chatId = update.callbackQuery().message().chat().id();
                        callBackUpd(chatId, update);
                        callBackUpdThirdMenu(chatId, update);
                        callBackUpdSecondMenu(chatId, update);
                        callBackDataListOfRecommendations(chatId, update);
                    }
                });
        logger.info("Processing update: {}", updates);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * <i>Обработка колбэков первого меню</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void callBackUpd(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals("15")) {
            } else if (data.equals("12")) {
                AboutTheNursery(chatId);
            } else if (data.equals("13")) {
                sendLocationPhoto(chatId);
            }
        }
    }

    /**
     * <i>Обработка колбэков 2-го меню</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void callBackUpdSecondMenu(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals("2")) {
                telegramBot.execute(new SendMessage(chatId, "Please select an option:").replyMarkup(menuMaker.inlineButtonsListOfRules(chatId)));
            } else if (data.equals("3")) {
                telegramBot.execute(new SendMessage(chatId, "Please select an option:").replyMarkup(menuMaker.buttonCynologist(chatId))
                );
            } else if (data.equals("4")) {
                flagButton = 4; // Установка флага нажатия кнопки
            }
        }
    }

    /**
     * <i>Обработка колбэков списка рекомендаций</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void callBackDataListOfRecommendations(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals("6")) {
                sendPetsDocuments(chatId);
            }
        }
    }

    /**
     * <i>Обработка колбэков 3-го меню</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.model.CallbackQuery
     * @see com.pengrad.telegrambot.model.Update
     */
    private void callBackUpdThirdMenu(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
        }
    }


    /**
     * <i>Ловим апдейты</i>
     * Структура кнопок бота и откликов по ним
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.request.SendMessage
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
     * @see com.pengrad.telegrambot.model.request.InlineKeyboardButton
     * @see com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
     * @see com.pengrad.telegrambot.TelegramBot
     * @see com.pengrad.telegrambot.model.Update
     */
    private void processUpdate(Long chatId, Update update) {
        String userMessage = update.message().text();
        if (flagButton == 4) {
            flagButton = 0;  // Сброс флага нажатия кнопки
            createUser(chatId, userMessage);
        } else {
            switch (userMessage) {
                case "/start":
                    telegramBot.execute(new SendMessage(chatId, "Какое-то приветственное сообщение, выберите команду из меню"));
                    break;
                case "/menu1":
                    SendMessage sendMsg = new SendMessage(chatId, "Please select an option:").replyMarkup(menuMaker.menu1Keyboard());
                    telegramBot.execute(sendMsg);
                    break;
                case "/menu2":
                    greetings(chatId, update);
                    SendMessage msg = new SendMessage(chatId, "Please select an option:").replyMarkup(menuMaker.menu2Keyboard());
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
                default:
            }
        }
    }

    /**
     * <i>Приветственное сообщение пользователя в чате</i>
     *
     * @param chatId идентификатор чата
     * @param update апдейт
     * @see com.pengrad.telegrambot.request.SendMessage
     */
    public void greetings(Long chatId, Update update) {
        String firstName = update.message().chat().firstName();
        String lastName = update.message().chat().lastName();
        telegramBot.execute(new SendMessage(chatId, "Привет " + firstName + " " + lastName));
    }

    /**
     * <i>Кнопка меню бота</i>
     *
     * @see com.pengrad.telegrambot.model.BotCommand
     * @see com.pengrad.telegrambot.request.SetMyCommands
     */
    public void menuButton() {
        SetMyCommands message = new SetMyCommands(
                new BotCommand("/start", "Запустить бота"),
                new BotCommand("/menu1", "Узнать информацию о приюте"),
                new BotCommand("/menu2", "Как взять собаку из приюта"),
                new BotCommand("/menu3", "Прислать отчет о питомце"),
                new BotCommand("/menu4", "Позвать волонтера"));
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

    /**
     * <i>Высылает в чат сообщение с текстом, сообщение с изображением карты, навигацию по карте</i> <br>
     *
     * @param chatId идентификатор чата
     * @see com.pengrad.telegrambot.request.SendMessage
     * @see com.pengrad.telegrambot.request.SendLocation
     * @see com.pengrad.telegrambot.request.SendPhoto
     */
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

    /**
     * <i>Высылает в чат документ</i>
     * <br>
     * Использует java.io метод {@link java.io.File#File(URI)}  File} <br>
     * Использует pengrad метод {@link com.pengrad.telegrambot.request.SendMessage } <br>
     * Использует pengrad метод {@link com.pengrad.telegrambot.request.SendDocument } <br>
     *
     * @param chatId идентификатор чата
     */
    public void sendPetsDocuments(Long chatId) {
        telegramBot.execute(new SendMessage(chatId, "Файл со списком нужных документов"));
        java.io.File file = new File("Документы, чтобы взять собаку.docx");
        telegramBot.execute(new SendDocument(chatId, file));
    }

    // Добавление нового пользователя полученного в Telegram Bot
    public void createUser(Long chatId, String str) {
        String[] strDivided = str.split("\"\\\\s*(\\\\s|,|!|\\\\.)\\\\s*\""); // Разбивка строки данных пользователя
        Users user = new Users();
        user.setFirstName(strDivided[0]);
        user.setLastName(strDivided[1]);
        user.setUserPhoneNumber(strDivided[2]);
        user.setUserEmail(strDivided[3]);
        usersService.createUser(user);
    }

    public void downloadPhotoFromChat(Update update) throws IOException {
        String file_id = update.message().photo()[update.message().photo().length - 1].fileId();
        URL url = new URL("https://api.telegram.org/bot" + telegramBot.getToken() + "/getFile?file_id=" + file_id);
        System.out.println(url);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String getFileResponse = br.readLine();
        JSONObject jresult = new JSONObject(getFileResponse);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        System.out.println(file_path);
        File localFile = new File("./src/main/resources/" + file_path);

        InputStream is = new URL("https://api.telegram.org/file/bot" + telegramBot.getToken() + "/" + file_path).openStream();
        copyInputStreamToFile(is, localFile);
        br.close();
        is.close();
    }

}
