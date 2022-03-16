package com.crazyjava.diskmanager.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="${feign.name}", url="${feign.url}")
public interface FeignClientService {
	//#feign 설정 (application.yaml)
	//feign:
  	//name: dataApi
  	//url: http://192.168.10.10:10001

	/** model */
	/* ################################################################################# */
	@RequestMapping(value = "crazyjava/model/save-model-info", consumes = "application/json")
	Map<String, Object> saveModelList(Map<String, Object> param);
	
	
	
	/** message */
	/* ################################################################################# */
	@RequestMapping(value = "crazyjava/message/enqeue-job", consumes = "application/json")
	void enqueueJob(Map<String, Object> param);

}
