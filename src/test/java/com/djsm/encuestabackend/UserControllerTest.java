package com.djsm.encuestabackend;

import com.djsm.encuestabackend.entities.UserEntity;
import com.djsm.encuestabackend.models.request.UserLoginRequestModel;
import com.djsm.encuestabackend.models.request.UserRegisterRequestModel;
import com.djsm.encuestabackend.models.responses.UserRest;
import com.djsm.encuestabackend.models.responses.ValidationErrors;
import com.djsm.encuestabackend.repositories.UserRepository;
import com.djsm.encuestabackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;


import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {



    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    public void cleanUp(){
        userRepository.deleteAll();
    }


    @Test
    public void createUser_sinNingunDato_retornaBadRequest(){
        UserRegisterRequestModel user = new UserRegisterRequestModel();

        ResponseEntity<Object> response = register(user, Object.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

    }

    @Test
    public void createUser_sinElCampoNombre_retornaBadRequest(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        user.setName(null);
        ResponseEntity<Object> response = register(user, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }


    @Test
    public void createUser_sinElCampoPassword_retornaBadRequest(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        user.setPassword(null);
        ResponseEntity<Object> response = register(user, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_sinElCampoEmail_retornaBadRequest(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        user.setEmail(null);
        ResponseEntity<Object> response = register(user, Object.class);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_sinNingunDato_retornaErroresDeValidacion(){

        UserRegisterRequestModel user = new UserRegisterRequestModel();

        ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);

        Map<String, String> errors = response.getBody().getErrors();

        assertEquals(errors.size(),3);
    }

    @Test
    public void createUser_sinNombre_retornaErrorDeMensajeDeValidacion(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        user.setName(null);
        ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);
        Map<String, String> errors = response.getBody().getErrors();
        assertTrue(errors.containsKey("name"));
    }

    @Test
    public void createUser_sinEmail_retornaErrorDeMensajeDeValidacion(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        user.setEmail(null);
        ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);
        Map<String, String> errors = response.getBody().getErrors();
        assertTrue(errors.containsKey("email"));

    }

    @Test
    public void createUser_sinPassword_retornaErroresDeMensajeDeValidacion(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        user.setPassword(null);
        ResponseEntity<ValidationErrors> response = register(user, ValidationErrors.class);
        Map<String, String> errors = response.getBody().getErrors();

        assertTrue(errors.containsKey("password"));
    }

    @Test
    public void createUser_conUsuarioValido_retornaOK(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        ResponseEntity<UserRest> response =register(user, UserRest.class);
        assertEquals(response.getStatusCode(),HttpStatus.OK);

    }

    @Test
    public void createUser_conUsuarioValido_retornaUserRest(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        ResponseEntity<UserRest> response = register(user, UserRest.class);

        assertEquals(response.getBody().getName(), user.getName());
    }

    @Test
    public void createUser_conUsuarioValido_persisteElUsuario(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        ResponseEntity<UserRest> response = register(user, UserRest.class);
        UserEntity userDB = userRepository.findById(response.getBody().getId());
        assertNotNull(userDB);
    }


    @Test
    public void createUser_conUsuarioValido_persistePasswordconHashEnLaDB(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        ResponseEntity<UserRest> response = register(user,UserRest.class);
        UserEntity userDB = userRepository.findById(response.getBody().getId());
        assertNotEquals(user.getPassword(),userDB.getEncryptedPassword());
    }

    @Test
    public void createUser_conUsuarioValidoConEmailExistente_retornaBadRequest(){
        UserRegisterRequestModel user = TestUtil.createValidUser();

        register(user, UserRest.class);

        ResponseEntity<UserRest> response2 = register(user, UserRest.class);

        assertEquals(response2.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void createUser_conUsuarioValidoConEmailExistente_retornaMensajeDeValidacion(){
        UserRegisterRequestModel user = TestUtil.createValidUser();

        register(user, UserRest.class);

        ResponseEntity<ValidationErrors> response2 = register(user,ValidationErrors.class);

        Map<String,String> errors = response2.getBody().getErrors();

        assertTrue(errors.containsKey("email"));
    }

    @Test
    public void getUser_sinTokenDeAutenticacion_retornaForbidden(){
        ResponseEntity<Object> response = getUser(null,new ParameterizedTypeReference<Object>(){});
        assertEquals(response.getStatusCode(),HttpStatus.FORBIDDEN);
    }

    @Test
    public void gerUser_conTokenDeAutenticacion_retornaUserOK(){

        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);
        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());
        ResponseEntity<Map<String, String>>  responseLogin = login(model, new ParameterizedTypeReference<Map<String, String>>() {
        });

        String token = responseLogin.getBody().get("token").replace("Bearer ", "");

        ResponseEntity<UserRest> response = getUser(token, new ParameterizedTypeReference<UserRest>() { });

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void gerUser_conTokenDeAutenticacion_retornaUserUserRest(){

        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);
        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());
        ResponseEntity<Map<String, String>>  responseLogin = login(model, new ParameterizedTypeReference<Map<String, String>>() {
        });

        String token = responseLogin.getBody().get("token").replace("Bearer ", "");

        ResponseEntity<UserRest> response = getUser(token, new ParameterizedTypeReference<UserRest>() { });

        assertEquals(user.getName(), response.getBody().getName());
    }


    /*simula el envio de la peticion post*/
    public <T> ResponseEntity<T> register(UserRegisterRequestModel data, Class<T> responseType){
        return testRestTemplate.postForEntity(TestUtil.API_URL,data,responseType);
    }


    /*simula el envio de la peticion post con parametized*/
    public <T> ResponseEntity<T> getUser(String token,ParameterizedTypeReference responseType){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> entity = new HttpEntity<Object>(null, headers);
        return testRestTemplate.exchange(TestUtil.API_URL, HttpMethod.GET, entity,responseType);
    }


    /*simula el envio de la peticion post con parametized*/
    public <T> ResponseEntity<T> login(UserLoginRequestModel data, ParameterizedTypeReference responseType){
        HttpEntity<UserLoginRequestModel> entity = new HttpEntity<UserLoginRequestModel>(data, new HttpHeaders());
        return testRestTemplate.exchange(TestUtil.API_LOGIN_URL, HttpMethod.POST, entity,responseType);
    }




}
