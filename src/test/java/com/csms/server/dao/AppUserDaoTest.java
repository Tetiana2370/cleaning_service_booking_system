package com.csms.server.dao;

import com.csms.server.dto.AppUserDto;
import com.csms.server.exception.ObjectDoesNotExistException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppUserDaoTest {

    @Autowired
    AppUserDao appUserDao;

    @Autowired
    Flyway flyway;

    long idAppUserRole_Admin = 1L;
    String defaultFirstName = "firstName";
    String defaultLastName = "lastName";
    String defaultPhoneNumber = "+48555444333";
    String defaultEmailAddress = "firstName@email.com";
    List<String> uniqueUsernames =new ArrayList<>(List.of("katty", "matty", "juli123", "UsErNo1", "blablabla", "agent007", "batman", "spiderman", "deadpool", "joker", "harleyQuinn", "superman", "aquaman"));

    @BeforeAll
    void prepareDB(){
        flyway.clean();
        flyway.migrate();
    }

    Long insertDefaultAdminUser(){
        AppUserDto appUserDto = new AppUserDto(idAppUserRole_Admin, defaultFirstName, defaultLastName, defaultPhoneNumber, defaultEmailAddress, uniqueUsernames.get(0), uniqueUsernames.get(0) + "pass");
        uniqueUsernames.remove(0);
        return appUserDao.insert(appUserDto);
    }

    @Test
    void insert() {
        Long idUser = insertDefaultAdminUser();
        assertNotNull(idUser);
    }


    @Test
    void get() throws Exception {
        long idUserInserted = insertDefaultAdminUser();
        AppUserDto appUserDtoGotFromDB = appUserDao.get(idUserInserted);

        assertNotNull(appUserDtoGotFromDB);
        assertEquals(idUserInserted, appUserDtoGotFromDB.idAppUser);
        assertEquals(defaultFirstName, appUserDtoGotFromDB.firstName);
        assertEquals(defaultLastName, appUserDtoGotFromDB.lastName);
        assertEquals(defaultPhoneNumber, appUserDtoGotFromDB.phoneNumber);
        assertEquals(defaultEmailAddress, appUserDtoGotFromDB.emailAddress);
        assertEquals(0, appUserDtoGotFromDB.version);
        assertTrue(appUserDtoGotFromDB.active);
    }

    @Test
    void getAll() {
        insertDefaultAdminUser();
        List<AppUserDto> appUserDtoList = appUserDao.getAll();

        assertNotNull(appUserDtoList);
        assertTrue(appUserDtoList.size() > 0);
        assertNotNull(appUserDtoList.get(0));
    }

    @Test
    void update() throws Exception {
        long idUserInserted = insertDefaultAdminUser();

        AppUserDto appUserDtoToUpdate = appUserDao.get(idUserInserted);
        appUserDtoToUpdate.firstName = defaultFirstName.concat("V2");
        appUserDtoToUpdate.lastName = defaultLastName.concat("V2");
        appUserDtoToUpdate.phoneNumber = defaultPhoneNumber.concat("V2");
        appUserDtoToUpdate.emailAddress = defaultEmailAddress.concat("V2");
        appUserDtoToUpdate.active = false;


        appUserDao.update(appUserDtoToUpdate);

        AppUserDto appUserDtoUpdated = appUserDao.get(idUserInserted);

        assertNotNull(appUserDtoUpdated);
        assertEquals(appUserDtoToUpdate.idAppUser, appUserDtoUpdated.idAppUser);
        assertEquals(appUserDtoToUpdate.firstName, appUserDtoUpdated.firstName);
        assertEquals(appUserDtoToUpdate.lastName, appUserDtoUpdated.lastName);
        assertEquals(appUserDtoToUpdate.phoneNumber, appUserDtoUpdated.phoneNumber);
        assertEquals(appUserDtoToUpdate.emailAddress, appUserDtoUpdated.emailAddress);
        assertEquals(appUserDtoToUpdate.version + 1, appUserDtoUpdated.version);
        assertEquals(Boolean.FALSE, appUserDtoToUpdate.active);
    }

    @Test
    void delete() throws Exception {
        long idUserInserted = insertDefaultAdminUser();

        appUserDao.delete(idUserInserted);

        assertThrows(ObjectDoesNotExistException.class, () -> appUserDao.get(idUserInserted));
    }

    @Test
    void throwsObjectDoesNotExistsException(){
        long nonExistentId = 100000L;

        AppUserDto appUserDtoWithNonExistentId = new AppUserDto(idAppUserRole_Admin, defaultFirstName, defaultLastName, defaultPhoneNumber, defaultEmailAddress, "uniqueUser", "hisPassword");
        appUserDtoWithNonExistentId.idAppUser = nonExistentId;
        assertThrows(ObjectDoesNotExistException.class, () -> appUserDao.delete(nonExistentId));
        assertThrows(ObjectDoesNotExistException.class, () -> appUserDao.get(nonExistentId));
        assertThrows(ObjectDoesNotExistException.class, () -> appUserDao.update(appUserDtoWithNonExistentId));
    }

    @Test
    @AfterAll
    void deleteAll(){
        appUserDao.deleteAll();

        assertEquals(0, appUserDao.getAll().size());
    }
}