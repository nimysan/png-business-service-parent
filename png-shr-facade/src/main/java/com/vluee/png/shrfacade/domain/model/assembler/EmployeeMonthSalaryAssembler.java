package com.vluee.png.shrfacade.domain.model.assembler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.hr.SalaryCatalog;
import com.vluee.png.shrfacade.domain.model.hr.SalaryItem;

public class EmployeeMonthSalaryAssembler {

	public static EmployeeMonthSalary assembleFromShrResponse(String shrResponse) {
		JSONObject parseObject = JSON.parseObject(shrResponse);
		JSONObject data = parseObject.getJSONArray("datas").getJSONObject(0);
		EmployeeMonthSalary ems = new EmployeeMonthSalary(data.getString("year"), data.getString("month"));
		JSONArray cats = data.getJSONArray("data").getJSONObject(0).getJSONArray("times").getJSONObject(0)
				.getJSONArray("cats");
		for (int i = 0; i < cats.size(); i++) {
			JSONObject catalogObject = cats.getJSONObject(i);
			SalaryCatalog sc = new SalaryCatalog(catalogObject.getString("catalog"));
			JSONArray itemsArray = catalogObject.getJSONArray("items");
			for (int p = 0; p < itemsArray.size(); p++) {
				sc.addSalaryItem(itemsArray.getObject(p, SalaryItem.class));
			}
			ems.addCatalog(sc);
		}
		return ems;
	}
}
