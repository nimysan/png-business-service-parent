package com.vluee.png.shrfacade.domain.model.hr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class EmployeeMonthSalary {

	public EmployeeMonthSalary(String year, String month) {
		this.year = year;
		this.month = month;
	}

	@Getter
	private String year;

	@Getter
	private String month;

	@Getter
	@Setter
	private String userName;

	private List<SalaryCatalog> catalogs;

	public List<SalaryCatalog> getCatalogs() {
		if (this.catalogs == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(catalogs);
	}

	public void addCatalog(SalaryCatalog object) {
		if (catalogs == null) {
			catalogs = new ArrayList<SalaryCatalog>(6);
		}
		catalogs.add(object);
	}
}
