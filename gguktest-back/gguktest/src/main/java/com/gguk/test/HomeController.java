package com.gguk.test;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application home page.
 */
@Controller
@CrossOrigin(origins = {"http://localhost:9090", "http://192.168.0.100:9090"})
public class HomeController {
	
	private Logger log = LogManager.getLogger("dev");
	
	@Autowired
	TestService service;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		log.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@GetMapping("/greeting")
	@ResponseBody
	public ResponseEntity<?> greet() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", true);
		map.put("message", "Hello");
		return ResponseEntity.ok().body(map);
	}

	@PostMapping("/{id}/image")
	@ResponseBody
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
			@PathVariable("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		log.debug("이미지 업로드 요청 처리 시작");
		
		if (service.saveFile(id, file, "image")) {
			map.put("status", true);
			map.put("message", "정상적으로 이미지를 업로드했습니다.");
			return ResponseEntity.ok().body(map);
		} else {
			map.put("status", false);
			map.put("message", "오류가 발생했습니다.");
			return ResponseEntity.internalServerError().body(map);
		}
	}

	@PostMapping("/{id}/video")
	@ResponseBody
	public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file,
			@PathVariable("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		ResponseEntity resp = null;
		
		if (service.saveFile(id, file, "video")) {
			map.put("status", true);
			map.put("message", "정상적으로 동영상을 업로드했습니다.");
			return ResponseEntity.ok().body(map);
		} else {
			map.put("status", false);
			map.put("message", "오류가 발생했습니다.");
			return ResponseEntity.internalServerError().body(map);
		}
	}
}
