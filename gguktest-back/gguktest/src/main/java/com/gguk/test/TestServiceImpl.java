package com.gguk.test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TestServiceImpl implements TestService {
	
	private Logger log = LogManager.getLogger("dev");

	@Autowired
	private ResourceLoader rsLoader;
	
	private boolean fileSaveHelper(MultipartFile file, String uploadLocation, String saveFileName) {
		Resource resource = rsLoader.getResource(uploadLocation);
		String uploadFolder = "";
		try {
			uploadFolder = resource.getFile().getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		log.debug("파일 저장 시도");
		
		File saveFile = new File(uploadFolder + "/" + saveFileName);
		try {
			file.transferTo(saveFile);
			log.debug("파일 저장 성공: " + uploadFolder + "/" + saveFileName);
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveFile(String memberId, MultipartFile file, String fileCat) {
		if (!fileCat.equals("image") && !fileCat.equals("video")) {
			return false;
		}
		
		String filenameOriginalFull = file.getOriginalFilename();
		String filenameOriginal = 
				filenameOriginalFull.substring(0, filenameOriginalFull.lastIndexOf("."));
		String fileExtension = 
				filenameOriginalFull.substring(filenameOriginalFull.lastIndexOf(".") + 1, filenameOriginalFull.length());
		String saveFileName = memberId + "." + fileExtension;
//		String saveFileName = memberId;

		return fileSaveHelper(file, "resources/" + fileCat, saveFileName);
	}
	
	public boolean uploadFile() {
		return true;
	}
}
