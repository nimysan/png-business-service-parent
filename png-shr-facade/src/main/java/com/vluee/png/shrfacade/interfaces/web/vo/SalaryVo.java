package com.vluee.png.shrfacade.interfaces.web.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryVo {

	private String year;

	private String month;

	private String remark;

	private List<SalaryCatalogVo> catalogs;

	public void addCatalog(SalaryCatalogVo catalog) {
		if (catalog == null) {
			return;
		}
		if (catalogs == null) {
			catalogs = new ArrayList<SalaryCatalogVo>();
		}
		catalogs.add(catalog);
	}
}
