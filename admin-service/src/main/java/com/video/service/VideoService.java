package com.video.service;

import com. video.pojo.Bgm;
import com. video.utils.PagedResult;

public interface VideoService {

	void addBgm(Bgm bgm);
	
	PagedResult queryBgmList(Integer page, Integer pageSize);
	
	void deleteBgm(String id);
	
	PagedResult queryReportList(Integer page, Integer pageSize);
	
	void updateVideoStatus(String videoId, Integer status);

	PagedResult videoList(Integer page, Integer pageSize);
}
