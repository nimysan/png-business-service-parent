package com.vluee.png.shrfacade.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author SeanYe
 *
 */
@AllArgsConstructor
public class SalaryItem {

	@Getter
	private String name;

	@Getter
	private boolean positiveValue = true;

	@Getter
	private String value;
	
}
