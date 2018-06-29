package kakao.pay.demo.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import java.security.InvalidParameterException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UriHistoryControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void home() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
        .andExpect(view().name("home"));
    }

    @Test
    public void inputSuccess() throws Exception{
        mockMvc.perform(post("/")
                .param("uri", "http://www.naver.com/aaaa")
                .param("page", String.valueOf(0)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?page=0"));
    }

    @Test
    public void inputFail() throws Exception{
        try {
            mockMvc.perform(post("/"))
                    .andReturn();
        } catch (NestedServletException e){
            Assert.assertTrue(e.getCause() instanceof InvalidParameterException);
        }

    }
}