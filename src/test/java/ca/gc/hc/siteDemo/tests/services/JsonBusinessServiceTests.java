package ca.gc.hc.siteDemo.tests.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ca.gc.hc.siteDemo.dtos.UserPrincipleDTO;
import ca.gc.hc.siteDemo.services.JsonBusinessService;
import ca.gc.hc.siteDemo.tests.BaseTest;

public class JsonBusinessServiceTests extends BaseTest {
  
  @Test
  public void convertJsonToUserPrincipleTest() throws Exception  {

    JsonBusinessService jsonBusinessService = new JsonBusinessService();
    UserPrincipleDTO userPrinciple = jsonBusinessService.convertJsonToData(testReturnData, UserPrincipleDTO.class);
    
    Assertions.assertEquals(userPrinciple.getUserId(), 1);
        
  }
  
}
