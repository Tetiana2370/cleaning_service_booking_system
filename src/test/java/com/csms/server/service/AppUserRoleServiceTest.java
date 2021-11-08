package com.csms.server.service;

import com.csms.server.dto.AppUserRoleDto;
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
public class AppUserRoleServiceTest {
    @Autowired
    Flyway flyway;

    @Autowired
    AppUserRoleService appUserRoleService;

    List<String> uniqueRoleNameList = new ArrayList<>(List.of("employee", "customer", "manager", "admin", "adminTrainee", "anotherImportantRole"));

    Long insertRole(String roleName, boolean admin, boolean active) throws ValidationException {
        AppUserRoleDto appUserDto = new AppUserRoleDto(roleName, admin);
        appUserDto.active = active;

        return appUserRoleService.insert(appUserDto);
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
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        Long idUser = insertRole(roleName, true, true);

        assertNotNull(idUser);
    }

    @Test
    void getExistent() throws ValidationException {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        long idUserInserted = insertRole(roleName, true, false);
        AppUserRoleDto appUserRoleDtoGotFromDB = appUserRoleService.get(idUserInserted);

        assertNotNull(appUserRoleDtoGotFromDB);
        assertEquals(idUserInserted, appUserRoleDtoGotFromDB.idAppUserRole);
        assertEquals(roleName, appUserRoleDtoGotFromDB.name);
        assertTrue(appUserRoleDtoGotFromDB.admin);
        assertFalse(appUserRoleDtoGotFromDB.active);
    }

    @Test
    void getNonExistent(){
        long idNonExistentUser = 100000;

        assertDoesNotThrow(() -> appUserRoleService.get(idNonExistentUser), new ObjectDoesNotExistException("User", idNonExistentUser).getMessage());
    }


    @Test
    void getAll() throws ValidationException {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        insertRole(roleName, false, false);
        List<AppUserRoleDto> appUserRoleDtoList = appUserRoleService.getAll();

        assertNotNull(appUserRoleDtoList);
        assertTrue(appUserRoleDtoList.size() > 0);
        assertNotNull(appUserRoleDtoList.get(0));
    }

    @Test
    void update() throws ValidationException, ObjectDoesNotExistException {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        long idUserInserted = insertRole(roleName, true, false);

        AppUserRoleDto appUserRoleDtoToUpdate = appUserRoleService.get(idUserInserted);
        appUserRoleDtoToUpdate.name = roleName.concat("V2");
        appUserRoleDtoToUpdate.admin = false;
        appUserRoleDtoToUpdate.active = true;

        appUserRoleService.update(appUserRoleDtoToUpdate);

        AppUserRoleDto appUserRoleDtoUpdated = appUserRoleService.get(idUserInserted);

        assertNotNull(appUserRoleDtoUpdated);
        assertEquals(appUserRoleDtoToUpdate.idAppUserRole, appUserRoleDtoUpdated.idAppUserRole);
        assertEquals(appUserRoleDtoToUpdate.name, appUserRoleDtoUpdated.name);
        assertFalse( appUserRoleDtoToUpdate.admin);
        assertTrue(appUserRoleDtoToUpdate.active);
    }

    @Test
    void delete() throws ValidationException, ObjectDoesNotExistException {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        long idUserRoleInserted = insertRole(roleName, true, true);

        appUserRoleService.delete(idUserRoleInserted);

        assertNull(appUserRoleService.get(idUserRoleInserted));
    }

    @Test
    void throwsObjectDoesNotExistsException(){
        long idNonExistentUser = 100000;
        String roleName = uniqueRoleNameList.get(0);

        AppUserRoleDto appUserRoleDtoWithNonExistentId = new AppUserRoleDto(roleName, true);
        appUserRoleDtoWithNonExistentId.idAppUserRole = idNonExistentUser;
        assertThrows(ObjectDoesNotExistException.class, () -> appUserRoleService.delete(idNonExistentUser));
        assertThrows(ObjectDoesNotExistException.class, () -> appUserRoleService.update(appUserRoleDtoWithNonExistentId));
    }

    @Test
    void doesNotThrowExceptionWhenObjectDoesNotExists(){
        long idNonExistentUser = 1000000;
        assertDoesNotThrow(() -> appUserRoleService.get(idNonExistentUser), new ObjectDoesNotExistException("User", idNonExistentUser).getMessage());
    }
}
