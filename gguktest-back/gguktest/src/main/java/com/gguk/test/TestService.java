package com.gguk.test;

import org.springframework.web.multipart.MultipartFile;

public interface TestService {
	public boolean uploadFile();

	boolean saveFile(String memberId, MultipartFile file, String fileCat);
}
