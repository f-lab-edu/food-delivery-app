package com.fdel.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdel.controller.authmockconfig.WithMockStoreOwner;
import com.fdel.dto.menu.MenuDto;
import com.fdel.repository.MenuRepository;
import com.fdel.service.MenuService;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class MenuControllerTest {
	
	@Autowired
	MenuController menuController;
	
	@Autowired
	FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private SessionRepositoryFilter sessionRepositoryFilter;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	private MockMvc mock;
	
	private MenuDto mockMenuDto0;
	private MenuDto mockMenuDto1;
	
	@BeforeEach
	void setup() {
		mock = MockMvcBuilders
			.standaloneSetup(menuController)
			.apply(springSecurity(springSecurityFilterChain))
			.addFilter(sessionRepositoryFilter)
			.build();
		
		mockMenuDto0 = MenuDto.builder().name("돈가스").price(5000).stockQuantity(30).build();
		mockMenuDto1 = MenuDto.builder().name("떠볶이").price(3000).stockQuantity(20).build();
	}
	
	@AfterEach
	void afterEach() {
		menuRepository.deleteAll();
		
		//h1 db의 id의 auto_increment를 1로 초기화합니다.
		entityManager
        	.createNativeQuery("ALTER TABLE MENU ALTER COLUMN `menu_id` RESTART WITH 1")
        	.executeUpdate();
	}

	@Test
	@WithMockStoreOwner
	@DisplayName("menu를 조회하면 해당 메뉴의 대한 정보를 담아서 응답한다.")
	void when_requestMenu_then_reponseTheMenuInfo() throws Exception {
		
		//given
		menuService.regist(mockMenuDto0);
		
		//when //then
		mock.perform(get("/menus/1"))
			.andExpect(jsonPath("$.name").value(mockMenuDto0.getName()))
			.andExpect(jsonPath("$.price").value(mockMenuDto0.getPrice()))
			.andExpect(jsonPath("$.stockQuantity").value(mockMenuDto0.getStockQuantity()));
	}
	
	@Test
	@WithMockStoreOwner
	@DisplayName("menu list를 조회화면 해당 메뉴들에 대한 정보를 담아서 응답한다.")
	void when_requestMenuList_then_responseTheMenuListInfo() throws Exception {
		
		//given
		menuService.regist(mockMenuDto0);
		menuService.regist(mockMenuDto1);
		
		//when //then
		mock.perform(get("/menus"))
			.andExpect(jsonPath("$[0].name").value(mockMenuDto0.getName()))
			.andExpect(jsonPath("$[0].price").value(mockMenuDto0.getPrice()))
			.andExpect(jsonPath("$[0].stockQuantity").value(mockMenuDto0.getStockQuantity()))
			.andExpect(jsonPath("$[1].name").value(mockMenuDto1.getName()))
			.andExpect(jsonPath("$[1].price").value(mockMenuDto1.getPrice()))
			.andExpect(jsonPath("$[1].stockQuantity").value(mockMenuDto1.getStockQuantity()));
	}
	
	
	@Test
	@WithMockStoreOwner
	@DisplayName("id가 1인 menu 삭제를 요청하면 id가 1인 menu를 삭제한다.")
	void when_requestDeleteId1Menu_then_deleteTheId1Menu() throws Exception {
	
		//given
		menuService.regist(mockMenuDto0);
		menuService.regist(mockMenuDto1);
		
		//when
		mock.perform(delete("/menus/1"));
		
		//then
		mock.perform(get("/menus"))
			.andExpect(jsonPath("$", Matchers.hasSize(1)))
			.andExpect(jsonPath("$[0].name").value(mockMenuDto1.getName()));
		
	}
	
	@Test
	@WithMockStoreOwner
	@DisplayName("id가 1인 menu를 수정요청하면 id가 1인 menu가 수정된다.")
	void when_requestPatchId1Menu_then_updateTheId1Menu() throws Exception {
		
		//given
		menuService.regist(mockMenuDto0);
		
		//when
		ObjectMapper objectMapper = new ObjectMapper();
		MenuDto patchInfo = 
			MenuDto.builder()
				.id(1L)
				.name("햄버거")
				.price(3000)
				.stockQuantity(100)
				.build();
		
		mock.perform(patch("/menus/1")
					.contentType(MediaType.APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(patchInfo))
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
		
		//then
		mock.perform(get("/menus/1"))
			.andExpect(jsonPath("$.name").value(patchInfo.getName()))
			.andExpect(jsonPath("$.price").value(patchInfo.getPrice()))
			.andExpect(jsonPath("$.stockQuantity").value(patchInfo.getStockQuantity()));	
	}
	
	
	@Test
	@WithMockStoreOwner
	@DisplayName("menu를 등록 요청하면 menu가 저장된다.")
	void when_requestRegisMenu_then_saveMenu() throws Exception {
		
		//when
		ObjectMapper objectMapper = new ObjectMapper();
		MenuDto patchInfo = 
			MenuDto.builder()
				.name("치킨")
				.price(8000)
				.stockQuantity(70)
				.build();
		
		mock.perform(post("/menus")
					.contentType(MediaType.APPLICATION_JSON)
		            .content(objectMapper.writeValueAsString(patchInfo))
	            .accept(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
		
		//then
		mock.perform(get("/menus/1"))
			.andExpect(jsonPath("$.name").value(patchInfo.getName()))
			.andExpect(jsonPath("$.price").value(patchInfo.getPrice()))
			.andExpect(jsonPath("$.stockQuantity").value(patchInfo.getStockQuantity()));	
	}

}
