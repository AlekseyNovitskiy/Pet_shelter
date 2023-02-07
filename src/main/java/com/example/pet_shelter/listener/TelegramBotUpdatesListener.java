package com.example.pet_shelter.listener;

import com.example.pet_shelter.MenuMaker.MenuMaker;
import com.example.pet_shelter.configuration.MenuDescription;
import com.example.pet_shelter.exceptions.UsersNullParameterValueException;
import com.example.pet_shelter.model.ReportUsers;
import com.example.pet_shelter.model.Users;
import com.example.pet_shelter.repository.BinaryContentFileRepository;
import com.example.pet_shelter.repository.ReportUsersRepository;
import com.example.pet_shelter.service.ReportUsersService;
import com.example.pet_shelter.service.UsersService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.*;
import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final ReportUsersRepository reportUsersRepository;
    private final BinaryContentFileRepository binaryContentFileRepository;
    private final UsersService usersService;
    private final MenuMaker menuMaker;
    private final ReportUsersService reportUsersService;

    public TelegramBotUpdatesListener(UsersService usersService, MenuMaker menuMaker, ReportUsersService reportUsersService,
                                      BinaryContentFileRepository binaryContentFileRepository,
                                      ReportUsersRepository reportUsersRepository) {
        this.usersService = usersService;
        this.menuMaker = menuMaker;
        this.reportUsersService = reportUsersService;
        this.binaryContentFileRepository = binaryContentFileRepository;
        this.reportUsersRepository = reportUsersRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private boolean isWaitingUserData; //ожидаем сообщение с данными пользователя после нажатия кнопки "записать данные пользователя"
    private boolean isProcess;         //ожидаем поочередно загрузку фотографии и сообщения о состоянии
    private boolean isPhoto;           //ожидаем загрузку фото

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
                    logger.info("Processing update: {}", update);
                    if (update.message() != null && update.message().text() != null || update.message() != null && update.message().photo() != null) {
                        Long chatId = update.message().chat().id();
                            processUpdate(chatId, update);
                    }
                    if (update.callbackQuery() != null) {
                        Long chatId = update.callbackQuery().message().chat().id();
                        startMenuDogs(chatId,update);
                        callBackUpdateMenu1(chatId, update);
                        callBackUpdateMenu2(chatId, update);
//                        callBackUpdateMenu3(chatId, update);
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
    private void callBackUpdateMenu1(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();

            if (data.equals(MenuDescription.WRITECONTACS.name())) {
                isWaitingUserData = true;
                telegramBot.execute(new SendMessage(chatId,
                        "Введите данные пользователя в формате \"Имя Фамилия Телефон Почта (через пробел)\""));
            } else if (data.equals(MenuDescription.AboutPetShelterDocx.name())) {
                AboutTheNursery(chatId);
            } else if (data.equals(MenuDescription.SCHEDULE.name())) {
                sendLocationPhoto(chatId);
            } else if (data.equals(MenuDescription.VOLUNTEERCALL.name())) {
                telegramBot.execute(new SendMessage(chatId, "Волонтер позван"));
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
    private void callBackUpdateMenu2(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
            if (data.equals(MenuDescription.MENULISTOFDOCUMENTS.name())) {
                telegramBot.execute(new SendMessage(chatId, "Список документов").replyMarkup(menuMaker.inlineButtonsListOfRules(chatId)));
            } else if (data.equals(MenuDescription.CYNOLOGISTADVICE.name())) {
                telegramBot.execute(new SendMessage(chatId, "Ознакомьтесь с правилам обращения с собакой или выберите кинолога:").replyMarkup(menuMaker.buttonCynologist(chatId))
                );
            } else if (data.equals(MenuDescription.WRITECONTACS.name())) {
                isWaitingUserData = true;
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

//    /**
//     * <i>Обработка колбэков 3-го меню</i>
//     *
//     * @param chatId идентификатор чата
//     * @param update апдейт
//     * @see com.pengrad.telegrambot.model.CallbackQuery
//     * @see com.pengrad.telegrambot.model.Update
//     */
//    private void callBackUpdateMenu3(Long chatId, Update update) {
//        CallbackQuery callbackQuery = update.callbackQuery();
//        if (update.callbackQuery() != null) {
//            String data = callbackQuery.data();
//            if (data.equals("photo")) {
//                isPhoto = true;
//                telegramBot.execute(new SendMessage(chatId, "Загрузите фото вашего питомца  "));
//            }
//        }
//    }

    private void startMenuDogs(Long chatId, Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        if (update.callbackQuery() != null) {
            String data = callbackQuery.data();
             if (data.equals(MenuDescription.DOGSHELTERENTER.name())) {
                telegramBot.execute(new SendMessage(chatId,"Вы выбрали собачий приют").replyMarkup(menuMaker.afterStartDogKeyBoard()));
            } else if (data.equals(MenuDescription.AboutPetShelter.name())) {
                 telegramBot.execute(new SendMessage(chatId, "Вы выбрали пункт выбрать информацию о приюте").replyMarkup(menuMaker.menu1Keyboard()));
             } else if (data.equals(MenuDescription.HOWTOTAKEDOG.name())) {
                 telegramBot.execute(new SendMessage(chatId, "Вы выбрали пункт как взять информацию о питомце").replyMarkup(menuMaker.menu2Keyboard()));
             } else if (data.equals(MenuDescription.SENDDOGPHOTO.name())) {
                 telegramBot.execute(new SendMessage(chatId, "Загрузите отчет").replyMarkup(menuMaker.menu3Keyboard()));
                 isPhoto = true;
             }
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
        if (isWaitingUserData) {
            try {
                createUser(chatId, userMessage);
                telegramBot.execute(new SendMessage(chatId, "Данные успешно записаны"));
                isWaitingUserData = false;
            } catch (UsersNullParameterValueException e) {
                telegramBot.execute(new SendMessage(chatId, "Не удается распознать данные: " + e.getMessage() + ", попробуйте еще раз."));
            }
        } else if (isPhoto) {
            try {
                reportUsersService.uploadReportUser(update); // Сохранение отчета User
                telegramBot.execute(new SendMessage(chatId, "Изображение успешно сохранено"));
                isPhoto = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            switch (userMessage) {
                case "/start":
                    telegramBot.execute(new SendMessage(chatId, "Какое-то приветственное сообщение, выберите команду из меню, Выберите приют").replyMarkup(menuMaker.startMenuKeyboard()));
                    break;
//                case "/menu1":
//                    SendMessage sendMsg = new SendMessage(chatId, "Please select an option:").replyMarkup(menuMaker.menu1Keyboard());
//                    telegramBot.execute(sendMsg);
//                    break;
//                case "/menu2":
//                    greetings(chatId, update);
//                    SendMessage msg = new SendMessage(chatId, "Please select an option:").replyMarkup(menuMaker.menu2Keyboard());
//                    telegramBot.execute(msg);
//                    break;
//                case "/menu3":
//                    SendMessage msg3 = new SendMessage(chatId, "Please select an option:").replyMarkup(menuMaker.menu3Keyboard());
//                    telegramBot.execute(msg3);
//                    break;
//                case "/menu4":
//                    SendMessage msg4 = new SendMessage(chatId, "Волонтер идёт !");
//                    telegramBot.execute(msg4);
//                    break;
                default:// TODO: добавить обработку неизвестных команд
                    telegramBot.execute(new SendMessage(chatId, "Вы ввели что-то странное!"));
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
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
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
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
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
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        telegramBot.execute(new SendMessage(chatId, "Файл со списком нужных документов"));
        java.io.File file = new File("Документы, чтобы взять собаку.docx");
        telegramBot.execute(new SendDocument(chatId, file));
    }

    /**
     * <i>Добавление нового пользователя, полученного через Telegram Bot</i>
     *
     * @param chatId идентификатор чата
     * @param str    строка с данными пользователя в формате "Имя, Фамилия, Телефон, Почта"
     * @see com.example.pet_shelter.service.UsersService
     */
    public void createUser(Long chatId, String str) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        //  logger.info("Метод" + Object.class.getEnclosingMethod());
        String[] strDivided = str.split("\\s*(\\s|,|!|\\.)\\s*"); // Разбивка строки данных пользователя
        Users user = new Users();
        user.setFirstName(strDivided[0]); // Если нет данных вылезает исключение ArrayIndexOutOfBoundsException
        user.setLastName(strDivided[1]);
        user.setUserPhoneNumber(strDivided[2]);
        user.setUserEmail(strDivided[3]);
        usersService.createUserInDb(user);
    }

    /**
     * Фоновое приложение для поиска User с не сданными отчетами
     */
    @Scheduled(fixedDelay = 60_000L)
    public void run() {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);

        LocalDate date = LocalDate.now().plusMonths(1);  // К текущей дате прибавляем время

        List<ReportUsers> reportUsersList = new ArrayList<>();
        reportUsersList = reportUsersRepository.getAllBy();

        for (int i = 0; i < reportUsersList.size(); i++) {
            LocalDate date1 = reportUsersList.get(i).getTime();
            Period period = Period.between(date, date1); // Вычисление промежутка между датами
            if (date.equals(date1) || period.getDays()>31) {
                long chatId = reportUsersList.get(i).getChatId();
                String str = "Уважаемый владелец! пришлите отчет о питомце. Спасибо!";
                SendResponse response = telegramBot.execute(new SendMessage(chatId, str));
            }
        }
    }
}
