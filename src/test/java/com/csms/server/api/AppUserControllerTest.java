package com.csms.server.api;

import com.csms.server.api.response.ResponseBody;
import com.csms.server.dto.AppUserDto;
import com.csms.server.exception.ObjectDoesNotExistException;
import com.csms.server.exception.ValidationException;
import com.csms.server.service.AppUserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class AppUserControllerTest {

    AppUserController appUserController;
    AppUserService appUserService;
    MockMvc mockMvc;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    String firstName = "firstName";
    String lastName = "lastName";
    String phoneNumber = "+48555444333";
    String emailAddress = "firstName@email.com";

    public AppUserControllerTest() {
        this.appUserService = Mockito.mock(AppUserService.class);
        this.appUserController = new AppUserController(this.appUserService);
    }
/*
    @Before
    public void setUp() {
        this.restTemplate = new RestTemplate();
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.appUserController)
                .build();
    }

    @Test
    void controllerIsNotNull(){
        assertNotNull(this.appUserController);
        assertNotNull(this.appUserService);
    }

    @SuppressWarnings("rawtypes")
    void checkResponseEntityStatusIsOk(ResponseEntity<ResponseBody> responseEntity){
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    void checkResponseBodyHasNoErrors(ResponseEntity<ResponseBody<Object>> responseEntity) {
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().errors);
        assertNotNull(responseEntity.getBody().body);
        assertEquals(Boolean.TRUE, responseEntity.getBody().succeed);
        assertEquals(0, responseEntity.getBody().errors.size());
    }

    void checkResponseBodyIsNullAndHasErrors(ResponseEntity<ResponseBody<Object>> responseEntity) {
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().errors);
        assertNull(responseEntity.getBody().body);
        assertEquals(Boolean.FALSE, responseEntity.getBody().succeed);
        assertTrue(0 < responseEntity.getBody().errors.size());
    }


    @Test
    public void whenUserExists_thenUserReturned() throws Exception {
        AppUserDto appUserDto = new AppUserDto(firstName, lastName, emailAddress, phoneNumber);
        appUserDto.idAppUser = 1L;

        when(appUserService.get(anyLong())).thenReturn(appUserDto);

        ResponseEntity<ResponseBody<AppUserDto>> responseEntity = appUserController.get(appUserDto.idAppUser);
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(appUserDto.firstName, responseEntity.getBody().body.firstName);
        assertEquals(appUserDto.toString(), responseEntity.getBody().body.toString());
    }

    @Test
    void whenUserDoesNotExist_thenErrorReturned(){
        when(appUserService.get(anyLong())).thenReturn(null);

        ResponseEntity<ResponseBody<AppUserDto>> responseEntity = appUserController.get(1L);
        checkResponseEntityStatusIsOk((ResponseEntity) responseEntity);
        checkResponseBodyIsNullAndHasErrors((ResponseEntity) responseEntity);

        assertTrue(Objects.requireNonNull(responseEntity.getBody()).errors.contains(ResponseBody.OBJECT_DOES_NOT_EXIST_ERROR_MESSAGE));
    }

    @Test
    void whenNoUserExist_thenEmptyListReturned() {
        List<AppUserDto> appUserDtoList = new ArrayList<>();

        when(appUserService.getAll()).thenReturn(appUserDtoList);

        ResponseEntity<ResponseBody<List<AppUserDto>>> responseEntity = appUserController.getAll();
        checkResponseEntityStatusIsOk((ResponseEntity) responseEntity);
        checkResponseBodyHasNoErrors((ResponseEntity) responseEntity);
        assertEquals(appUserDtoList.size(), Objects.requireNonNull(responseEntity.getBody()).body.size());
    }

    @Test
    void whenAnyUserExist_thenListReturned(){
        AppUserDto appUserDto1 = new AppUserDto(firstName, lastName, emailAddress, phoneNumber);
        appUserDto1.idAppUser = 1L;
        AppUserDto appUserDto2 = new AppUserDto(firstName, lastName, emailAddress, phoneNumber);
        appUserDto1.idAppUser = 2L;
        List<AppUserDto> appUserDtoList = List.of(appUserDto1, appUserDto2);

        when(appUserService.getAll()).thenReturn(appUserDtoList);

        ResponseEntity<ResponseBody<List<AppUserDto>>> responseEntity = appUserController.getAll();
        checkResponseEntityStatusIsOk((ResponseEntity) responseEntity);
        checkResponseBodyHasNoErrors((ResponseEntity) responseEntity);
        assertEquals(appUserDtoList.size(), Objects.requireNonNull(responseEntity.getBody()).body.size());
    }

    @Test
    void whenInsertionSucceed_thenIdReturned() throws ValidationException {
        AppUserDto appUserDto = new AppUserDto(firstName, lastName, phoneNumber, emailAddress);

        when(appUserService.insert(isA(AppUserDto.class))).thenReturn(1L).thenThrow(new ValidationException("Vali"));

        ResponseEntity<ResponseBody<Long>> responseEntity = appUserController.insert(appUserDto);
        checkResponseEntityStatusIsOk((ResponseEntity) responseEntity);
        checkResponseBodyHasNoErrors((ResponseEntity) responseEntity);
    }

    @Test
    void whenInsertionFailed_thenErrorReturned() throws ValidationException {
        AppUserDto appUserDto = new AppUserDto(firstName, lastName, phoneNumber, emailAddress);

        String exceptionMessage = "Validation exception";
        when(appUserService.insert(isA(AppUserDto.class))).thenThrow(new ValidationException(exceptionMessage));

        ResponseEntity<ResponseBody<Long>> responseEntity = appUserController.insert(appUserDto);
        checkResponseEntityStatusIsOk((ResponseEntity) responseEntity);
        checkResponseBodyIsNullAndHasErrors((ResponseEntity) responseEntity);
    }

    @Test
    void whenUpdateSucceed_thenUserReturned(){
        AppUserDto appUserDto = new AppUserDto(firstName, lastName, phoneNumber, emailAddress);
        appUserDto.idAppUser = 1L;
        AppUserDto appUserDtoUpdated = new AppUserDto("name", "last_name", "123", "1111");

        when(appUserService.get(1L)).thenReturn(appUserDtoUpdated);

        ResponseEntity<ResponseBody<AppUserDto>> responseEntity = appUserController.update(appUserDto);
        checkResponseEntityStatusIsOk((ResponseEntity) responseEntity);
        checkResponseBodyHasNoErrors((ResponseEntity) responseEntity);
        assertEquals(appUserDtoUpdated.firstName, responseEntity.getBody().body.firstName);
        assertEquals(appUserDtoUpdated.phoneNumber, responseEntity.getBody().body.phoneNumber);
    }

    @Test
    void whenUpdateFailed_thenErrorReturned(){
        AppUserDto appUserDto = new AppUserDto(firstName, lastName, phoneNumber, emailAddress);

        when(appUserService.get(1L)).thenReturn(null);

        ResponseEntity<ResponseBody<AppUserDto>> responseEntity = appUserController.update(appUserDto);
        checkResponseEntityStatusIsOk((ResponseEntity) responseEntity);
        checkResponseBodyIsNullAndHasErrors((ResponseEntity) responseEntity);
    }

    @Test
    void whenDeleteFailed_thenErrorReturned() throws Exception {

        doThrow(new ObjectDoesNotExistException("AppUser", 1L)).when(appUserService).delete(1L);

        ResponseEntity<ResponseBody<Boolean>> responseEntity = appUserController.delete(1L);

        checkResponseEntityStatusIsOk((ResponseEntity) responseEntity);
        checkResponseBodyIsNullAndHasErrors((ResponseEntity) responseEntity);
        assertTrue(responseEntity.getBody().errors.toString().contains("doesn't exist"));
    }
*/
}