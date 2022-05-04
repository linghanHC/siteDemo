package ca.gc.hc.siteDemo.services;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
public class LocalTestDataBusinessService {

	@Value("classpath:/testdata/drug.json")
	private Resource productMasterListTestDataResource;

	@Autowired
	private JsonBusinessService jsonBusinessService;

	public String getProductMasterListTestData() {
		return getTestData(productMasterListTestDataResource);
	}

	private String getTestData(Resource testResource) {
		Reader reader;
		try {
			reader = new InputStreamReader(testResource.getInputStream(), UTF_8);
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}