package com.video.controller;

import com. video.enums.VideoStatusEnum;
import com. video.pojo.Bgm;
import com. video.service.VideoService;
import com. video.utils.IMoocJSONResult;
import com. video.utils.PagedResult;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Controller
@RequestMapping("video")
public class VideoController {
	
	@Autowired
	private VideoService videoService;
	
	@GetMapping("/showReportList")
	public String showReportList() {
		return "video/reportList";
	}
	
	@GetMapping("/reportList")
	@ResponseBody
	public PagedResult reportList(Integer page) {
		
		PagedResult result = videoService.queryReportList(page == null ? 1 : page, 10);
		return result;
	}
	@GetMapping("/list")
	@ResponseBody
	public PagedResult videoList(Integer page) {

		PagedResult result = videoService.videoList(page == null ? 1 : page, 10);
		return result;
	}
	@PostMapping("/forbidVideo")
	@ResponseBody
	public IMoocJSONResult forbidVideo(String videoId) {
		
		videoService.updateVideoStatus(videoId, VideoStatusEnum.FORBID.value);
		return IMoocJSONResult.ok();
	}

	@GetMapping("/showBgmList")
	public String showBgmList() {
		return "video/bgmList";
	}
	
	@GetMapping("/queryBgmList")
	@ResponseBody
	public PagedResult queryBgmList(Integer page) {
		return videoService.queryBgmList(page == null ? 1 : page, 10);
	}
	
	@GetMapping("/showAddBgm")
	public String login() {
		return "video/addBgm";
	}
	
	@PostMapping("/addBgm")
	@ResponseBody
	public IMoocJSONResult addBgm(Bgm bgm) {
		
		videoService.addBgm(bgm);
		return IMoocJSONResult.ok();
	}
	
	@PostMapping("/delBgm")
	@ResponseBody
	public IMoocJSONResult delBgm(String bgmId) {
		videoService.deleteBgm(bgmId);
		return IMoocJSONResult.ok();
	}

	@PostMapping("/delVideo")
	@ResponseBody
	public IMoocJSONResult delVideo(String videoId) {
		videoService.updateVideoStatus(videoId,0);
		return IMoocJSONResult.ok();
	}

	@PostMapping("/bgmUpload")
	@ResponseBody
	public IMoocJSONResult bgmUpload(@RequestParam("path") MultipartFile[] files) throws Exception {
		
		// 文件保存的命名空间
//		String fileSpace = File.separator + "imooc_videos_dev" + File.separator + "mvc-bgm";
		// 保存到数据库中的相对路径
		String uploadPathDB = "/bgm";
		
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (files != null && files.length > 0) {
				
				String fileName = files[0].getOriginalFilename();
				if (!StringUtils.isEmpty(fileName)) {
					// 文件上传的最终保存路径
					String finalPath = "/Users/zhouchao/Desktop/biye/dev" + uploadPathDB + File.separator + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					
					File outFile = new File(finalPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
				
			} else {
				return IMoocJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return IMoocJSONResult.errorMsg("上传出错...");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		
		return IMoocJSONResult.ok(uploadPathDB);
	}
	
}
