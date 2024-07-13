package com.lionrock.simple.executor.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lionrock.simple.executor.model.CoinPrice;

@Mapper
public interface CoinPriceMapper {
	
	CoinPrice getLatestCoinPriceByCode(@Param("code") String code);
	
	int insertLatestCoinPrice(@Param("coinPrice") CoinPrice coinPrice);

}
