package stcube;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lab.stcube.dao.imp.MongoFeatureDaoImp;

public class TestMongoFeatureDaoImp {


	private static MongoFeatureDaoImp dao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dao = new MongoFeatureDaoImp();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testCountTile2d() {
		List<Map<String, Object>> lst = dao.countTile2d(76.26000, 77.37000, 61.3000, 65.4000, 10);
		// y_min = 602723
		// y_max = 7757364
		// x_max = 7757364
		System.out.println(lst.size());
		assertNotNull(lst);
		assertTrue(lst.size() > 0);
	}

}
