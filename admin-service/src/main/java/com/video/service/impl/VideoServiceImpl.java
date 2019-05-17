package com.video.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.video.enums.BGMOperatorTypeEnum;
import com.video.mapper.BgmMapper;
import com.video.mapper.UsersReportCustomMapper;
import com.video.mapper.VideosMapper;
import com.video.pojo.Bgm;
import com.video.pojo.Videos;
import com.video.pojo.vo.Reports;
import com.video.service.VideoService;
import com.video.utils.JsonUtils;
import com.video.utils.PagedResult;
import com.video.web.util.ZKCurator;
import idworker.Sid;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {

	@Resource
	private VideosMapper videosMapper;
		
	@Resource
	private BgmMapper bgmMapper;
	
	@Resource
	private Sid sid;
	
	@Resource
	private ZKCurator zkCurator;
	
	@Resource
	private UsersReportCustomMapper usersReportCustomMapper;
	
	@Override
	public PagedResult queryReportList(Integer page, Integer pageSize) {

		PageHelper.startPage(page, pageSize);

		List<Reports> reportsList = usersReportCustomMapper.selectAllVideoReport();

		PageInfo<Reports> pageList = new PageInfo<Reports>(reportsList);

		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(reportsList);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());

		return grid;
	}

	@Override
	public void updateVideoStatus(String videoId, Integer status) {
		
		Videos video = new Videos();
		video.setId(videoId);
		video.setStatus(status);
		videosMapper.updateByPrimaryKeySelective(video);
	}

	@Override
	public PagedResult videoList(Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);

		List<Videos> list = videosMapper.selectAll();

		PageInfo<Videos> pageList = new PageInfo<>(list);

		PagedResult result = new PagedResult();
		result.setTotal(pageList.getPages());
		result.setRows(list);
		result.setPage(page);
		result.setRecords(pageList.getTotal());

		return result;
	}

	@Override
	public PagedResult queryBgmList(Integer page, Integer pageSize) {
		
		PageHelper.startPage(page, pageSize);

		List<Bgm> list = bgmMapper.selectAll();
		
		PageInfo<Bgm> pageList = new PageInfo<>(list);
		
		PagedResult result = new PagedResult();
		result.setTotal(pageList.getPages());
		result.setRows(list);
		result.setPage(page);
		result.setRecords(pageList.getTotal());
		
		return result;
	}

	@Override
	public void addBgm(Bgm bgm) {
		String bgmId = sid.nextShort();
		bgm.setId(bgmId);
		bgmMapper.insert(bgm);
		
		Map<String, String> map = new HashMap<>();
		map.put("operType", BGMOperatorTypeEnum.ADD.type);
		map.put("path", bgm.getPath());
		
		zkCurator.sendBgmOperator(bgmId, JsonUtils.objectToJson(map));
	}
	
	@Override
	public void deleteBgm(String id) {
		Bgm bgm = bgmMapper.selectByPrimaryKey(id);
		
		bgmMapper.deleteByPrimaryKey(id);
		
		Map<String, String> map = new HashMap<>();
		map.put("operType", BGMOperatorTypeEnum.DELETE.type);
		map.put("path", bgm.getPath());
		
		zkCurator.sendBgmOperator(id, JsonUtils.objectToJson(map));
		
	}

}
