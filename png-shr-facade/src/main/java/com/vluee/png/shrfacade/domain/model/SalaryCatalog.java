package com.vluee.png.shrfacade.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class SalaryCatalog {

	public SalaryCatalog(String catalog) {
		this.catalog = catalog;
		this.items = new ArrayList<SalaryItem>(5);
	}

	@Getter
	private String catalog;

	@Getter
	private List<SalaryItem> items;

	public void addSalaryItem(SalaryItem salaryItem) {
		if (items == null) {
			items = new ArrayList<SalaryItem>(5);
		}
		this.items.add(salaryItem);
	}

}
