package com.vluee.png.shrfacade.domain.model;

import java.util.List;

import lombok.Getter;

public class EmployeeMonthSalary {

	@Getter
	private String year;

	@Getter
	private String month;

	private List<SalaryCatalog> catalogs;
}
