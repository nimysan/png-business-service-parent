package com.vluee.png.shrfacade.interfaces.web;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;

@Controller
public class GreetingController {

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		model.addAttribute("salary",
				JSON.parse(IOUtils.readAll(new InputStreamReader(
						GreetingController.class.getClassLoader().getResourceAsStream("sample.json"),
						Charset.forName("utf8")))));
		return "greeting";
	}

	@GetMapping("/getcode")
	public String getCode() {
		return "getcode";
	}
	

}