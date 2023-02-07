package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.DogNullParameterValueException;
import com.example.pet_shelter.listener.TelegramBotUpdatesListener;
import com.example.pet_shelter.model.BinaryContentFile;
import com.example.pet_shelter.model.ReportUsers;
import com.example.pet_shelter.repository.BinaryContentFileRepository;
import com.example.pet_shelter.repository.ReportUsersRepository;
import com.pengrad.telegrambot.model.Update;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

@Service
public class ReportUsersService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Value("${telegram.bot.token}")
    private String token;                   // Ключ telegram bot

    BinaryContentFileRepository binaryContentFileRepository;
    ReportUsersRepository reportUsersRepository;

    public ReportUsersService(BinaryContentFileRepository binaryContentFileRepository, ReportUsersRepository reportUsersRepository) {
        this.binaryContentFileRepository = binaryContentFileRepository;
        this.reportUsersRepository = reportUsersRepository;
    }

    public void uploadReportUser(Update update) throws IOException {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        String file_id = update.message().photo()[update.message().photo().length - 1].fileId();
        URL url = new URL("https://api.telegram.org/bot" + token + "/getFile?file_id=" + file_id);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String getFileResponse = br.readLine();
        JSONObject jresult = new JSONObject(getFileResponse);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        File localFile = new File("./src/main/resources/" + file_path);
        InputStream is = new URL("https://api.telegram.org/file/bot" + token + "/" + file_path).openStream();
        byte[] b = is.readAllBytes();
        copyInputStreamToFile(is, localFile);
        br.close();
        is.close();

        BinaryContentFile binaryContentFile = new BinaryContentFile();
        binaryContentFile.setFileBytes(b);
        binaryContentFileRepository.save(binaryContentFile);

        ReportUsers reportUsers = new ReportUsers();
        reportUsers.setBinaryContentFile(binaryContentFile);
        reportUsers.setFilePath(localFile.getPath());
        reportUsers.setTelegramFileId(file_id);
        reportUsers.setFileSize(b.length);
        reportUsers.setTime(LocalDate.now());
        reportUsers.setChatId(update.message().chat().id());
        reportUsers.setCommentsUser(update.message().caption());
        reportUsersRepository.save(reportUsers);
    }

    /**
     * <b>Поиск отчета по его id идентификатору</b>
     * <br> Используется метод репозитория {@link JpaRepository#findById(Object)}
     *
     * @param id идентификатор отчета
     * @return Возвращает найденый отчет
     */
    public ReportUsers findReportUsers(Long id) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        return reportUsersRepository.findById(id).orElseThrow();
    }


    public ReportUsers updateReport(Long id, ReportUsers reportUsers) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
        ReportUsers updateReportUsers = reportUsersRepository.findById(id).orElse(null);
        if (updateReportUsers != null) {
            updateReportUsers.setCommentsUser(reportUsers.getCommentsUser());
            updateReportUsers.setTime(reportUsers.getTime());
            updateReportUsers.setFilePath(reportUsers.getFilePath());
            updateReportUsers.setChatId(reportUsers.getChatId());
            updateReportUsers.setFileSize(reportUsers.getFileSize());
            updateReportUsers.setBinaryContentFile(reportUsers.getBinaryContentFile());

        } else {
            throw new DogNullParameterValueException("Недостаточно данных при попытке заменить данные у объекта Report");
        }
        return reportUsersRepository.save(updateReportUsers);
    }

}