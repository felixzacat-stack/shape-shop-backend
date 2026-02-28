package com.shapeshop;

import com.shapeshop.config.ShapeShopTest;
//import com.shapeshop.config.TestConfig;
//import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * Tests authentication and authorization.
 */
//@Import(TestConfig.class)
@EnableWebMvc
//@RunWith(SpringRunner.class)
//@SpringBootTest
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationHTTPTests extends ShapeShopTest {

	StatusResultMatchers matcher = MockMvcResultMatchers.status();
    @Autowired
    private MockMvc mvc;


    @Test
    public void shouldNotAuth() throws Exception {

        // forbidden. 403 - private endpoint
        mvc.perform(MockMvcRequestBuilders.get("/user")).andExpect(matcher.isForbidden()); //this url for testing purposes
    	mvc.perform(MockMvcRequestBuilders.get("/admin")).andExpect(matcher.isForbidden());

    	//OK. 200 - public endpoint
    	mvc.perform(MockMvcRequestBuilders.get("/carlscafe/products")).andExpect(matcher.isOk());

        // unauthorized. 401 - authenticate a nonexistant user
    	String requestJson = "{\"username\": \"IDoNotExistInTheDB\",\"password\": \"foo\"}";
        mvc.perform(MockMvcRequestBuilders.post("/authenticate").contentType("application/json").content(requestJson)).andExpect(matcher.isUnauthorized());
    }


    @Test
    public void shouldAuthUser() throws Exception {
    	String requestJson = "{\"username\": \"user\",\"password\": \"user\"}";

        //OK. 200
    	ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/authenticate").contentType("application/json").content(requestJson)).andExpect(matcher.isOk());
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        System.out.println("contentAsString (token) " + contentAsString);
        String token = contentAsString.substring(8, contentAsString.length()-2);
        System.out.println("token " + token);
        assertNotNull(token);

        //OK. 200
    	resultActions = mvc.perform(MockMvcRequestBuilders.get("/user").header("Authorization", "Bearer " + token)).andExpect(matcher.isOk());

        result = resultActions.andReturn();
        contentAsString = result.getResponse().getContentAsString();
        System.out.println("contentAsString " + contentAsString);
        assertEquals("user", contentAsString);

        // forbidden. 403 - A logged in 'user' should NOT be able to access /admin
    	mvc.perform(MockMvcRequestBuilders.get("/admin").header("Authorization", "Bearer " + token)).andExpect(matcher.isForbidden());

    }


    @Test
    public void shouldAuthAdmin() throws Exception {

    	String requestJson = "{\"username\": \"admin\",\"password\": \"admin\"}";

        //OK. 200
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/authenticate").contentType("application/json").content(requestJson)).andExpect(matcher.isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println("contentAsString " + contentAsString);
        String token = contentAsString.substring(8, contentAsString.length()-2);
        assertNotNull(token);

        //OK. 200 - A logged in 'admin' should be able to access /user
    	resultActions = mvc.perform(MockMvcRequestBuilders.get("/user").header("Authorization", "Bearer " + token)).andExpect(matcher.isOk());

        result = resultActions.andReturn();
        contentAsString = result.getResponse().getContentAsString();
        System.out.println("contentAsString " + contentAsString);
        assertEquals("user", contentAsString);

        //OK. 200 - A logged in 'admin' should be able to access /admin
        mvc.perform(MockMvcRequestBuilders.get("/admin").header("Authorization", "Bearer " + token)).andExpect(matcher.isOk());
    }
}
