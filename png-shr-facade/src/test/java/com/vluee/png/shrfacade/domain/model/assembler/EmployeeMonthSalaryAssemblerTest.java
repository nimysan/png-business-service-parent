package com.vluee.png.shrfacade.domain.model.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import com.google.common.base.Joiner;
import com.vluee.png.shrfacade.domain.model.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.SalaryCatalog;
import com.vluee.png.shrfacade.domain.model.SalaryItem;
import com.vluee.png.shrfacade.infrastructure.shr.HrServiceForTest;

class EmployeeMonthSalaryAssemblerTest {

	@Test
	void verifyAssembleSalaryDataFromShrResponse() throws IOException {
		EmployeeMonthSalary ems = EmployeeMonthSalaryAssembler.assembleFromShrResponse(Joiner.on("")
				.join(IOUtils.readLines(HrServiceForTest.class.getClassLoader().getResourceAsStream("sample.json"),
						Charset.forName("utf8"))));
		assertThat(ems.getYear()).isNotBlank();
		assertThat(ems.getMonth()).isNotBlank();
		assertThat(ems.getCatalogs()).hasSize(5);

		SalaryCatalog salaryCatalog = ems.getCatalogs().get(0);
		assertThat(salaryCatalog.getCatalog()).isNotBlank();
		assertThat(salaryCatalog.getItems()).hasAtLeastOneElementOfType(SalaryItem.class);
	}

}