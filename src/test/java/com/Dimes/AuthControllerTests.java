package com.Dimes;


import com.Dimes.Controllers.AuthController;
import com.Dimes.Models.JwtRequest;
import com.Dimes.Models.Lender;
import com.Dimes.Models.LoginViewModel;
import com.Dimes.Services.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
public class AuthControllerTests extends JsonManager {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private Lender lender = new Lender(1,"charles company",1000.0,2.3,50.0,"nehe@gmail.com","Nehe","12345678","Admin");

    @Test
    void saveLenderTest() throws  Exception
    {
        when(authService.RegisterLender(any(Lender.class))).thenReturn(true);
        when(authService.isEmailValid("nehe@gmail.com")).thenReturn(true);
        MvcResult result = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(super.mapToJson(lender)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().replaceAll("\"","")).isEqualTo("Registered successfully");
    }

    @Test
    @DisplayName("saveLenderTestFails")
    void saveLenderTest2() throws  Exception
    {
        when(authService.RegisterLender(any(Lender.class))).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(super.mapToJson(lender)))
                .andExpect(status().isExpectationFailed())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().replaceAll("\"","")).isEqualTo("An error occurred while registering");
    }

    @Test
    @DisplayName("saveLenderTestFails_WhenUsernameExists")
    void saveLenderTest3() throws  Exception
    {
        when(authService.checkIfLenderExists(lender.getUsername())).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(super.mapToJson(lender)))
                .andExpect(status().isConflict())
                .andReturn();
        assertThat(result.getResponse().getContentAsString().replaceAll("\"","")).isEqualTo("Username Exists");
    }

    @Test
    @DisplayName("saveLenderWithInvalidEmail")
    void saveLenderTest4() throws  Exception
    {
        when(authService.isEmailValid("@nehemiah.com")).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(super.mapToJson(lender)))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().replaceAll("\"","")).isEqualTo("Invalid email");
    }

    @Test
    void createAuthenticationTokenTest() throws Exception
    {
        JwtRequest jwtRequest = new JwtRequest("charles","1234Pass");

        MvcResult mvcResult = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(super.mapToJson(jwtRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String result = mvcResult.getResponse().getContentAsString();
        Token token = super.mapFromJson(result,Token.class);
        //System.out.println("TOKEN IS: "+ token.getToken() );

        assertThat(token.getToken()).isNotBlank();


    }

    @Test
    void failToCreateAuthenticationTokenTest() throws Exception
    {
        JwtRequest jwtRequest = new JwtRequest("wrongUsername","wrongPassword");

        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(super.mapToJson(jwtRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUserIdDetailsTest() throws Exception
    {
        MvcResult mvcResult = mockMvc.perform(get("/api/getUserIdDetails")
                .headers(new Token().getHttpHeaders()))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(mvcResult.getResponse().getContentType(),"application/json");
    }

   /* @Test
    void getUserIdDetailsFailTest() throws Exception
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+new Token().testWrongToken);

        MvcResult mvcResult = mockMvc.perform(get("/api/getUserIdDetails")
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(mvcResult.getResponse().getContentType(),"application/json");
    }
*/
   @Test
   void getAllLenderDetails() throws  Exception
   {
       when(authService.getAllLenderDetails()).thenReturn(Collections.singletonList(new Lender()));

       mockMvc.perform(get("/api/getAllLenderDetails")
               .headers(new Token().getHttpHeaders()))
               .andExpect(status().isOk());

   }



}//test class

class Token{

    private String token;

    String testRightToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaGFybGVzIiwiZXhwIjoxNTcyMjc0ODc2LCJpYXQiOjE1NzIyNTY4NzZ9.OYsmXBDfrISBwwcfbvSyphXM5KRggdeKBR_ajF0AT5VGj13SueB-T1ycWZaPjhJyz-4wntfro-BwGHYgDBT09w";
    String testWrongToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0b20iLCJleHAiOjE1NzIxMTcxNzQsImlhdCI6MTU3MjA5OTE3NH0.H1zP6iGhAmWPfx22z2I8XV_NAV_BIAYR9GuTDxGTbUwApiscJle8vz7t5so6ZnZfETwyRByEDgyz8apdHZOS_A";

    public String getToken() {
        return token;
    }

    public HttpHeaders getHttpHeaders()
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+testRightToken);
        return httpHeaders;
    }



}