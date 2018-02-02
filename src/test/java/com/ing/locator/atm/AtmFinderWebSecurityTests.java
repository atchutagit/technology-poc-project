package com.ing.locator.atm;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AtmFinderWebSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testProtectedUrlSuccess() throws Exception {
    		this.mockMvc
            .perform(get("/atm-finder").with(user("user").password("password")))
            .andExpect(status().isOk())
            .andExpect(authenticated().withUsername("user"));
    }

    @Test
    public void testProtectedUrlFailed() throws Exception {
		this.mockMvc 
            .perform(get("/atm-finder"))
            .andExpect(status().is3xxRedirection());
    }
    
    @Test
    public void testUnProtectedUrlSuccess() throws Exception {
		this.mockMvc
            .perform(get("/atms/locations"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void testLoginSuccess() throws Exception {
		this.mockMvc
            .perform(formLogin().user("user").password("password"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/atm-finder"))
            .andExpect(authenticated().withUsername("user"));
    }

    @Test
    public void testLoginFailed() throws Exception {
		this.mockMvc
            .perform(formLogin().user("user").password("invalidpassword"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"))
            .andExpect(unauthenticated());
    }
}
