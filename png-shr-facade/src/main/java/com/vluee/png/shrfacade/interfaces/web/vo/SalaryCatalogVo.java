package com.vluee.png.shrfacade.interfaces.web.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryCatalogVo {

	private String title;
	private Integer order;

	private List<SalaryItemVo> items;

	public void addItem(SalaryItemVo ivo) {
		if (ivo == null) {
			return;
		}
		if (items == null) {
			items = new ArrayList<SalaryItemVo>(4);
		}
		items.add(ivo);
	}

}
