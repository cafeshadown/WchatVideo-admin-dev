package com.video.mapper;

import com. video.pojo.vo.Reports;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UsersReportCustomMapper {
    List<Reports> selectAllVideoReport();
}