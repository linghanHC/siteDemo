package ca.gc.hc.siteDemo.tests.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ca.gc.hc.siteDemo.constants.Constants;
import ca.gc.hc.siteDemo.controllers.authentication.LoginController;
import ca.gc.hc.siteDemo.services.ScreenDetailBusinessService;
import ca.gc.hc.siteDemo.tests.BaseTest;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class LoginControllerTests extends BaseTest {

	  @InjectMocks
	  private LoginController loginController = new LoginController();

	  @Mock
	  private ScreenDetailBusinessService screenDetailBusinessService;
	  
	  private MockMvc mockMvc;
	    
	  @BeforeEach
	  public void setup() {
	      mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
	  }

	  @Test
	  public void submitTest() throws Exception {
	    
	    // Test happy path
	    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(Constants.DO_LOGIN_URL_MAPPING)
	    		.flashAttr("userLoginForm", getBasicUserLoginData());
	    
	    mockMvc.perform(builder)        
			.andExpect(status().isFound())
			.andExpect(redirectedUrl(Constants.UP_1_LEVEL_URL_MAPPING));

	  }

	  @Test
	  public void submitTestNoLoginForm() throws Exception {
	    
	    // Test no session user login form
	    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(Constants.DO_LOGIN_URL_MAPPING);
	    
	    mockMvc.perform(builder)        
	      .andExpect(status().isFound())
	      .andExpect(redirectedUrl(Constants.UP_1_LEVEL_URL_MAPPING + Constants.ERROR_500_VIEW));

	  }


}
