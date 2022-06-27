package com.radel.core.configserver.radelconfigserver;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("default")
class SecurityConfigurationTest {

    public SecurityConfigurationTest(WebApplicationContext wac) {
        this.wac = wac;
    }

    WebApplicationContext wac;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void userWithoutBasicAuthIsNotSupposedToHaveAccess() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userWithBasicAuthIsSupposedToHaveAccessToLoginPage1() throws Exception {

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .claim("scope", "actuator")
                .build();

        mockMvc.perform(get("/actuator/info").with(jwt().jwt(jwt)))
                .andExpect(status().isOk());
    }

    @Test
    void userWithBasicAuthIsSupposedToHaveAccessToLoginPage2() throws Exception {

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .claim("scope", "notActuator")
                .build();

        mockMvc.perform(get("/actuator/info").with(jwt().jwt(jwt)))
                .andExpect(status().isForbidden());
    }

    @Test
    void userWithBasicAuthIsSupposedToHaveAccessToLoginPage() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}
