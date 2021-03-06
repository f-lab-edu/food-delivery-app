package com.fdel.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdel.FoodDeliveryAppApplication;
import com.fdel.controller.authmockconfig.WithMockStoreOwner;
import com.fdel.dto.store.StoreDto;
import com.fdel.repository.OrderRepository;
import com.fdel.repository.StoreRepository;
import com.fdel.repository.memoryRepository.store.StoreMemoryRepository;
import com.fdel.service.StoreService;


@Transactional
@SpringBootTest
@ActiveProfiles("concurrent-test")
public class StoreControllerTest {
	
	@Autowired
	StoreController storeController;
	
	@Autowired
	FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private SessionRepositoryFilter sessionRepositoryFilter;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private StoreRepository storeRepository;
	
	private MockMvc mock;
	
	private StoreDto storeDto1;
	private StoreDto storeDto2;

	
	@BeforeEach
	void setup() {
		mock = MockMvcBuilders
			.standaloneSetup(storeController)
			.apply(springSecurity(springSecurityFilterChain))
			.addFilter(sessionRepositoryFilter)
			.build();
		
		storeDto1 = StoreDto.builder().id(1L).name("?????????").address("????????? ????????? ????????? 232").zipcode(25611).build();
		storeDto2 = StoreDto.builder().id(2L).name("?????????").address("????????? ????????? ????????? ????????? 1166").zipcode(25435).build();
		
		System.out.print("storeDto1 id =" + storeDto1.getId());
		System.out.print("storeDto2 id =" + storeDto2.getId());
	}
	
	@AfterEach
	void afterEach() {
		//?????? ?????? ??????????????? test ????????? ?????? ?????? ??????????????? deleteAll()??? ??????????????? ?????????.
		storeRepository.deleteAll();
	}
	
	
	@Test
	@WithMockStoreOwner
	@DisplayName("store??? ???????????? ?????? store??? ?????? ????????? ????????? ????????????.")
	void when_requestStore_then_reponseTheStoreInfo() throws Exception {
		
		//given
		storeService.regist(storeDto1);
		
		//when //then
		mock.perform(get("/stores/1"))
			.andExpect(jsonPath("$.name").value(storeDto1.getName()))
			.andExpect(jsonPath("$.address").value(storeDto1.getAddress()))
			.andExpect(jsonPath("$.zipcode").value(storeDto1.getZipcode()));
	}
	
	@Test
	@WithMockStoreOwner
	@DisplayName("store list??? ???????????? ?????? store?????? ?????? ????????? ????????? ????????????.")
	void when_requestStoreList_then_responseTheStoreListInfo() throws Exception {
		
		//given
		storeService.regist(storeDto1);
		storeService.regist(storeDto2);
		
		//when //then
		mock.perform(get("/stores"))
			.andExpect(jsonPath("$[0].name").value(storeDto1.getName()))
			.andExpect(jsonPath("$[0].address").value(storeDto1.getAddress()))
			.andExpect(jsonPath("$[0].zipcode").value(storeDto1.getZipcode()))
			.andExpect(jsonPath("$[1].name").value(storeDto2.getName()))
			.andExpect(jsonPath("$[1].address").value(storeDto2.getAddress()))
			.andExpect(jsonPath("$[1].zipcode").value(storeDto2.getZipcode()));
	}
	
	@Test
	@WithMockStoreOwner
	@DisplayName("id??? 1??? store ????????? ???????????? id??? 1??? store??? ????????????.")
	void when_requestDeleteId1Store_then_deleteTheId1Store() throws Exception {
	
		//given
		storeService.regist(storeDto1);
		storeService.regist(storeDto2);
		
		//when
		mock.perform(delete("/stores/1"));
		
		//then
		List<StoreDto> allStoreDtoList = storeService.findAll();
		assertThat(allStoreDtoList.size(), equalTo(1));
		assertThat(allStoreDtoList.get(0).getName(), equalTo(storeDto2.getName()));
	}
	
	@Test
	@WithMockStoreOwner
	@DisplayName("id??? 1??? store??? ?????????????????? id??? 1??? store??? ????????????.")
	void when_requestPatchId1Store_then_updateTheId1Store() throws Exception {
		
		//given
		storeService.regist(storeDto1);
		
		//when
		ObjectMapper objectMapper = new ObjectMapper();
		StoreDto patchInfo = 
			StoreDto.builder()
				.id(1L)
				.name("????????? ??????")
				.address("??????????????? ?????????")
				.zipcode(03154)
				.build();
		
		mock.perform(patch("/stores/1")
					.contentType(MediaType.APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(patchInfo))
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
		
		//then
		StoreDto findStoreDto = storeService.findById(1L);
		assertThat(findStoreDto.getName(), equalTo(patchInfo.getName()));
		assertThat(findStoreDto.getAddress(), equalTo(patchInfo.getAddress()));
		assertThat(findStoreDto.getZipcode(), equalTo(patchInfo.getZipcode()));	
	}
	
	
	@Test
	@WithMockStoreOwner
	@DisplayName("store??? ?????? ???????????? store??? ????????????.")
	void when_requestRegisStore_then_saveStore() throws Exception {
		
		//when
		ObjectMapper objectMapper = new ObjectMapper();
		
		mock.perform(post("/stores")
					.contentType(MediaType.APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(storeDto1))
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
		
		//then
		StoreDto findStoreDto = storeService.findById(1L);
		assertThat(findStoreDto.getName(), equalTo(storeDto1.getName()));
		assertThat(findStoreDto.getAddress(), equalTo(storeDto1.getAddress()));
		assertThat(findStoreDto.getZipcode(), equalTo(storeDto1.getZipcode()));
	}
	
}
