package com.csms.server.dao;

import com.csms.server.dto.AppUserRoleDto;
import com.csms.server.exception.ObjectDoesNotExistException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
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
class AppUserRoleDaoTest {

    @Autowired
    AppUserRoleDao appUserRoleDao;

    @Autowired
    Flyway flyway;

    @BeforeAll
    void prepareDB(){
        flyway.clean();
        flyway.migrate();
    }
    List<String> uniqueRoleNameList = new ArrayList<>(List.of("manager", "adminTrainee", "anotherImportantRole", "roleRoleRole", "345x345"));


    Long insertRole(String roleName, boolean admin, boolean active){
        AppUserRoleDto appUserDto = new AppUserRoleDto(roleName, admin);
        appUserDto.active = active;

        return appUserRoleDao.insert(appUserDto);
    }

    @Test
    void insert() {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        Long idUserRole = insertRole(roleName, false, true);
        assertNotNull(idUserRole);
    }

    @Test
    void get() throws Exception {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        long idRoleInserted = insertRole(roleName, true, false);
        AppUserRoleDto appUserRoleDtoGotFromDB = appUserRoleDao.get(idRoleInserted);

        assertNotNull(appUserRoleDtoGotFromDB);
        assertEquals(idRoleInserted, appUserRoleDtoGotFromDB.idAppUserRole);
        assertEquals(roleName, appUserRoleDtoGotFromDB.name);
        assertTrue(appUserRoleDtoGotFromDB.admin);
        assertFalse(appUserRoleDtoGotFromDB.active);
    }

    @Test
    void getAll() {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        insertRole(roleName, false, false);
        List<AppUserRoleDto> appUserDtoList = appUserRoleDao.getAll();

        assertNotNull(appUserDtoList);
        assertTrue(appUserDtoList.size() > 0);
        assertNotNull(appUserDtoList.get(0));
    }

    @Test
    void update() throws Exception {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        long idRoleInserted = insertRole(roleName, false, false);

        AppUserRoleDto appUserDtoToUpdate = appUserRoleDao.get(idRoleInserted);
        appUserDtoToUpdate.name = roleName.concat("V2");
        appUserDtoToUpdate.active = true;
        appUserDtoToUpdate.admin = true;

        appUserRoleDao.update(appUserDtoToUpdate);

        AppUserRoleDto appUserDtoUpdated = appUserRoleDao.get(idRoleInserted);

        assertNotNull(appUserDtoUpdated);
        assertEquals(appUserDtoToUpdate.idAppUserRole, appUserDtoUpdated.idAppUserRole);
        assertEquals(appUserDtoToUpdate.name, appUserDtoUpdated.name);
        assertEquals(appUserDtoToUpdate.admin, appUserDtoUpdated.admin);
        assertEquals(appUserDtoToUpdate.active, appUserDtoUpdated.active);

    }

    @Test
    void delete() throws Exception {
        String roleName = uniqueRoleNameList.get(0);
        uniqueRoleNameList.remove(0);
        long idRoleInserted = insertRole(roleName, true, true);

        appUserRoleDao.delete(idRoleInserted);

        assertThrows(ObjectDoesNotExistException.class, () -> appUserRoleDao.get(idRoleInserted));
    }

    @Test
    void throwsObjectDoesNotExistsException(){
        long nonExistentId = 100000L;

        AppUserRoleDto appUserDtoWithNonExistentId = new AppUserRoleDto("roleName", true);
        appUserDtoWithNonExistentId.idAppUserRole = nonExistentId;
        assertThrows(ObjectDoesNotExistException.class, () -> appUserRoleDao.delete(nonExistentId));
        assertThrows(ObjectDoesNotExistException.class, () -> appUserRoleDao.get(nonExistentId));
        assertThrows(ObjectDoesNotExistException.class, () -> appUserRoleDao.update(appUserDtoWithNonExistentId));
    }

    @Test
    @AfterAll
    void deleteAll(){
        appUserRoleDao.deleteAll();

        assertEquals(0, appUserRoleDao.getAll().size());
    }
}