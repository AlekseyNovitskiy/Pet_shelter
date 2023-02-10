package com.example.pet_shelter.service;

import com.example.pet_shelter.exceptions.SheltersNullParameterValueException;
import com.example.pet_shelter.exceptions.UsersNullParameterValueException;
import com.example.pet_shelter.listener.TelegramBotUpdatesListener;
import com.example.pet_shelter.model.Shelters;
import com.example.pet_shelter.repository.ShelterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
public class ShelterService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }


    /**
     * <i>Создание нового приюта в базу данных</i>
     *
     * @param shelter передает приют
     * @see com.example.pet_shelter.repository.ShelterRepository
     * @see com.example.pet_shelter.model.Shelters
     */
    public Shelters cteateShelter(Shelters shelter) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);

        if (shelter.getNameShelter().isBlank() || shelter.getNameShelter() == null) {
            throw new SheltersNullParameterValueException("Имя приюта не указано");
        }
        return this.shelterRepository.save(shelter);
    }

    /**
     * <i>Удаляет из базы приют по id</i>
     *
     * @param id Id приюта в базе данных
     * @see com.example.pet_shelter.repository.UsersRepository
     */
    public Shelters deleteShelter(Long id) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);
            Shelters deleteShelter = shelterRepository.findById(id).orElse(null);
        shelterRepository.deleteById(id);
        return deleteShelter;
    }

    /**
     * <i>Заменяет старые параметры пользователя на те что были переданы.
     * Если объект по id не найден будет выкинуто исключение UsersNullParameterValueException.
     * При отсутсвии одного из полей у передаваемого объекта user будет выкинуто исключение NullPointerException.
     * </i>
     *
     * @param id   Id приюта в базе данных
     * @param shelter объект приюта
     * @see com.example.pet_shelter.repository.ShelterRepository
     * @see com.example.pet_shelter.model.Shelters
     */
    public Shelters updateShelter(Long id, Shelters shelter) {
        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        logger.info("Current Method is - " + methodName);

        Shelters updateShelter = shelterRepository.findById(id).orElse(null);
        if (updateShelter != null) {
            updateShelter.setNameShelter(shelter.getNameShelter());
            updateShelter.setDescriptionShelter(shelter.getDescriptionShelter());

        } else {
            throw new UsersNullParameterValueException("Недостаточно данных при попытке заменить данные у объекта shelter");
        }
        return shelterRepository.save(updateShelter);

    }


    /**
     * <i>Список всех приютов
     * @see com.example.pet_shelter.repository.ShelterRepository
     * @see com.example.pet_shelter.model.Shelters
     */
    public Collection<Shelters> getAllShelters() {
            return this.shelterRepository.getAllBy();
    }

    /**
     * <i>Информация о приюте
     * @see com.example.pet_shelter.repository.ShelterRepository
     * @see com.example.pet_shelter.model.Shelters
     */
    public Shelters getShelter(Long id) {
        return shelterRepository.findById(id).orElse(null);
    }

}
