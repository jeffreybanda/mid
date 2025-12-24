package com.mid.app.polmtrveh.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;

import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.polmtrveh.model.PolMtrVeh;

public class PolMtrVehRepositoryUTest {
	EntityManagerFactory emf;
	EntityManager em;
	PolMtrVehRepository polMtrVehRepository;
	DbCommandExecutor dbCommandExecutor;

	@Before
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("midPU");
		em = emf.createEntityManager();
		polMtrVehRepository = new PolMtrVehRepository();
		polMtrVehRepository.em = em;
		dbCommandExecutor = new DbCommandExecutor(em);
	}

	public void findPolMtrVehRecords() {
		final List<PolMtrVeh> polMtrVehs = dbCommandExecutor.executeCommand(() -> {
			return polMtrVehRepository.findPolMtrVehicleList("mddmct0008091900", 0, 0);
		});

		assertThat(polMtrVehs.size(), is(equalTo(9)));
	}

}
