package ca.gc.hc.siteDemo.tests.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ca.gc.hc.siteDemo.constants.Constants;
import ca.gc.hc.siteDemo.controllers.HomeController;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HomeControllerTests {

  @InjectMocks
  private HomeController homeController = new HomeController();
  
  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;
  
  private MockMvc mockMvc;
    
  @BeforeEach
  public void setup() {
      mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
  }
  
  @Test
  public void displayPageTest() throws Exception {
    
	    // Test happy path
	    MockHttpSession session = new MockHttpSession();
	    
	    MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(Constants.ROOT_URL_MAPPING)
	        .session(session);

	    mockMvc.perform(builder)
	      .andExpect(status().isOk())
	      .andExpect(view().name(Constants.MAIN_MENU_VIEW));
    
  }

}
