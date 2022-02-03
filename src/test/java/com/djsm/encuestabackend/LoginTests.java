package com.djsm.encuestabackend;

import com.djsm.encuestabackend.models.request.UserLoginRequestModel;
import com.djsm.encuestabackend.models.request.UserRegisterRequestModel;
import com.djsm.encuestabackend.repositories.UserRepository;
import com.djsm.encuestabackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginTests {

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
    public void postLogin_sinCredenciales_retornaForbidden(){

    ResponseEntity<Object> response = login(null, Object.class);
    assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);

    }

    @Test
    public void postLogin_conCredencialesIncorrectas_retornaForbidden(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);
        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail("sda@gmail.com");
        model.setPassword("12345678");

        ResponseEntity<Object> response = login(model, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);

    }

    @Test
    public void postLogin_conCredencialesIncorrectas_retornaOK(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);
        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());

        ResponseEntity<Object> response = login(model, Object.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void postLogin_conCredencialesIncorrectas_retornoAuthToken(){
        UserRegisterRequestModel user = TestUtil.createValidUser();
        userService.createUser(user);
        UserLoginRequestModel model = new UserLoginRequestModel();
        model.setEmail(user.getEmail());
        model.setPassword(user.getPassword());

        ResponseEntity<Map<String,String>> response = login(model, new ParameterizedTypeReference<Map<String, String>>(){});

        Map<String,String> boby = response.getBody();

        String token = boby.get("token");

        assertTrue(token.contains("Bearer"));

    }


    /*simula el envio de la peticion post*/
    public <T> ResponseEntity<T> login(UserLoginRequestModel data, Class<T> responseType){
        return testRestTemplate.postForEntity(TestUtil.API_LOGIN_URL,data,responseType);
    }

    /*simula el envio de la peticion post con parametized*/
    public <T> ResponseEntity<T> login(UserLoginRequestModel data, ParameterizedTypeReference responseType){
        HttpEntity<UserLoginRequestModel> entity = new HttpEntity<UserLoginRequestModel>(data, new HttpHeaders());
        return testRestTemplate.exchange(TestUtil.API_LOGIN_URL, HttpMethod.POST, entity,responseType);
    }



}
