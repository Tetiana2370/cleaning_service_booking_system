package com.csms.server.service;

import com.csms.server.dto.AppUserDto;
import com.csms.server.exception.ObjectDoesNotExistException;
import com.csms.server.exception.ValidationException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
class AppUserServiceTest {

    @Autowired
    Flyway flyway;

    @Autowired
    AppUserService appUserService;

    long idAppUserRole_Admin = 1L;
    String defaultFirstName = "Lara";
    String defaultLastName = "Smith";
    String defaultPhoneNumber = "+12121";
    String defaultEmailAddress = "adam.smith@ll.com";
    List<String> uniqueUsernames = new ArrayList<>(List.of("katty", "matty", "juli123", "UsErNo1", "blablabla", "agent007", "batman", "spiderman", "deadpool", "joker", "harleyQuinn", "superman", "aquaman"));

    Long insertDefaultUser() throws ValidationException {
        AppUserDto appUserDto = new AppUserDto(idAppUserRole_Admin, defaultFirstName, defaultLastName, defaultPhoneNumber, defaultEmailAddress, uniqueUsernames.get(0), uniqueUsernames.get(0) + "pass");
        uniqueUsernames.remove(0);
        return appUserService.insert(appUserDto);
    }

    @BeforeAll
    void prepareDB(){
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void validate() {
    }

    @Test
    void insertWithNoExceptions() throws ValidationException {
        Long idUser = insertDefaultUser();

        assertNotNull(idUser);
    }

    @Test
    void getExistent() throws ValidationException {
        long idUserInserted = insertDefaultUser();
        AppUserDto appUserDtoGotFromDB = appUserService.get(idUserInserted);

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
    void getNonExistent(){
        long idNonExistentUser = 100000;

        assertDoesNotThrow(() -> appUserService.get(idNonExistentUser), new ObjectDoesNotExistException("User", idNonExistentUser).getMessage());
    }


    @Test
    void getAll() throws ValidationException {
        insertDefaultUser();
        List<AppUserDto> appUserDtoList = appUserService.getAll();

        assertNotNull(appUserDtoList);
        assertTrue(appUserDtoList.size() > 0);
        assertNotNull(appUserDtoList.get(0));
    }

    @Test
    void update() throws ValidationException, ObjectDoesNotExistException {
        long idUserInserted = insertDefaultUser();

        AppUserDto appUserDtoToUpdate = appUserService.get(idUserInserted);
        appUserDtoToUpdate.firstName = defaultFirstName.concat("V2");
        appUserDtoToUpdate.lastName = defaultLastName.concat("V2");
        appUserDtoToUpdate.phoneNumber = defaultPhoneNumber.concat("V2");
        appUserDtoToUpdate.emailAddress = defaultEmailAddress.concat("V2");
        appUserDtoToUpdate.active = false;


        appUserService.update(appUserDtoToUpdate);

        AppUserDto appUserDtoUpdated = appUserService.get(idUserInserted);

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
    void delete() throws ValidationException, ObjectDoesNotExistException {
        long idUserInserted = insertDefaultUser();

        appUserService.delete(idUserInserted);

        assertNull(appUserService.get(idUserInserted));
    }

    @Test
    void throwsObjectDoesNotExistsException(){
        long idNonExistentUser = 100000;

        AppUserDto appUserDtoWithNonExistentId = new AppUserDto(idAppUserRole_Admin, defaultFirstName, defaultLastName, defaultPhoneNumber, defaultEmailAddress, "nonExistantUser", "pass");
        appUserDtoWithNonExistentId.idAppUser = idNonExistentUser;
        assertThrows(ObjectDoesNotExistException.class, () -> appUserService.delete(idNonExistentUser));
        assertThrows(ObjectDoesNotExistException.class, () -> appUserService.update(appUserDtoWithNonExistentId));
    }

    @Test
    void doesNotThrowExceptionWhenObjectDoesNotExists(){
        long idNonExistentUser = 1000000;
        assertDoesNotThrow(() -> appUserService.get(idNonExistentUser), new ObjectDoesNotExistException("User", idNonExistentUser).getMessage());
    }

}