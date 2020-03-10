package com.vluee.png.shrfacade.interfaces.web.assembler;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.vluee.png.shrfacade.domain.model.hr.EmployeeMonthSalary;
import com.vluee.png.shrfacade.domain.model.hr.SalaryCatalog;
import com.vluee.png.shrfacade.domain.model.hr.SalaryItem;
import com.vluee.png.shrfacade.interfaces.web.vo.SalaryCatalogVo;
import com.vluee.png.shrfacade.interfaces.web.vo.SalaryItemVo;
import com.vluee.png.shrfacade.interfaces.web.vo.SalaryVo;

@Component
public final class SalaryVoAssembler {

	private static Map<String, Integer> CATALOG_PRE_ORDERS = ImmutableMap.of("_TOTAL", 100, "应发项目", 10, "扣款项目", 20,
			"其他项目", 30);

	private static DecimalFormat df = new DecimalFormat("#########0.00");

	public SalaryVo assemble(EmployeeMonthSalary salaryModel) {
		SalaryVo vo = new SalaryVo();
		vo.setYear(salaryModel.getYear());
		vo.setMonth(salaryModel.getMonth());
		vo.setRemark(getRemarkFromModel(salaryModel));
		salaryModel.getCatalogs().forEach(t -> {
			if (isRemarkCatalog(t)) {
				return;
			}
			vo.addCatalog(convertToCatalogVo(t));

		});
		// 排好序，前端界面直接按这个顺序输出
		vo.getCatalogs().sort(new Comparator<SalaryCatalogVo>() {
			@Override
			public int compare(SalaryCatalogVo o1, SalaryCatalogVo o2) {
				return o1.getOrder() - o2.getOrder();
			}
		});
		return vo;
	}

	private SalaryCatalogVo convertToCatalogVo(SalaryCatalog cataogModel) {
		SalaryCatalogVo vo = new SalaryCatalogVo();
		vo.setTitle(translateTitle(cataogModel.getCatalog()));
		Integer order = CATALOG_PRE_ORDERS.get(cataogModel.getCatalog());
		if (order == null) {
			order = 50;
		}
		vo.setOrder(order);

		cataogModel.getItems().forEach(t -> {
			SalaryItemVo ivo = new SalaryItemVo(t.getName(), formatLabel(t.getPay()));
			vo.addItem(ivo);
		});
		return vo;
	}

	private String translateTitle(String catalog) {
		if (("_TOTAL").contentEquals(catalog)) {
			return "总计";
		} else {
			return catalog;
		}
	}

	private String formatLabel(String pay) {
		if (StringUtils.isBlank(pay)) {
			return "0.00";
		} else {
			return df.format(Double.parseDouble(pay));
		}
	}

	private String getRemarkFromModel(EmployeeMonthSalary salaryModel) {
		try {
			SalaryItem salaryItem = salaryModel.getCatalogs().stream().filter(t -> isRemarkCatalog(t)).findFirst().get()
					.getItems().get(0);
			String pay = salaryItem.getPay();
			if (StringUtils.isBlank(pay)) {
				return "无";
			}
			return pay;
		} catch (Exception e) {
			return "无";
		}
	}

	private boolean isRemarkCatalog(SalaryCatalog catalog) {
		return catalog != null && catalog.getCatalog().contentEquals("_REMARK");
	}
}
