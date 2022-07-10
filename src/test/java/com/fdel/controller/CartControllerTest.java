package com.fdel.controller;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdel.dto.cart.MenuWantToPurchaseDto;
import com.fdel.repository.CartRedisRepository;
import com.fdel.repository.CartRepository;
import com.fdel.service.CartService;

@Execution(ExecutionMode.SAME_THREAD)
@SpringBootTest
public class CartControllerTest {

	private MockMvc mock;
	
	@Autowired
	private CartController cartController;
	
	@Autowired
	private SessionRepositoryFilter sessionRepositoryFilter;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	@Qualifier("redisCartTemplate")
	private RedisTemplate redisTemplate;
	
	MenuWantToPurchaseDto menuWantToPurchaseDto1;
	
	@BeforeEach
	void setup() {
		
		/* 
		 * sessionRepositoryFilter가 없어야 MockHttpSession이 작동한다.
		 * 참조 : https://github.com/spring-projects/spring-session/issues/2037
		 */
		mock = MockMvcBuilders
			.standaloneSetup(cartController)
			.apply(springSecurity(springSecurityFilterChain))
			.build();
		
		//mockDto init
		menuWantToPurchaseDto1 = new MenuWantToPurchaseDto(1L); //menu ID가 1인 menu
		menuWantToPurchaseDto1.setName("피자");
		menuWantToPurchaseDto1.setPrice(30000);
		menuWantToPurchaseDto1.setNumberOfWantToBuy(2);
		
	}
	
	@AfterEach
	void afterWach() {
		if(cartRepository instanceof CartRedisRepository) {
			CartRedisRepository cartRedisRepository = (CartRedisRepository)cartRepository;
			cartRedisRepository.flushAll();
		}
	}
	
	@Test
	@DisplayName("현재 장바구니의 리스트를 요청하면 리스트를 담아서 응답해준다.")
	void when_requestCurrentCartMenuList_thenResonseWithTheList() throws Exception {
		//given
		//mockDto init
		MenuWantToPurchaseDto menuWantToPurchaseDto2 = new MenuWantToPurchaseDto(2L); //menu ID가 2인 menu
		menuWantToPurchaseDto2.setName("국밥");
		menuWantToPurchaseDto2.setPrice(8000);
		menuWantToPurchaseDto2.setNumberOfWantToBuy(5);
		
		MenuWantToPurchaseDto menuWantToPurchaseDto3 = new MenuWantToPurchaseDto(3L); //menu ID가 3인 menu
		menuWantToPurchaseDto3.setName("보쌈");
		menuWantToPurchaseDto3.setPrice(25000);
		menuWantToPurchaseDto3.setNumberOfWantToBuy(3);
		
		//mockSession init
		MockHttpSession	mockSession = new MockHttpSession();
		
		cartService.addOrUpdateMenuOfCart(menuWantToPurchaseDto1, mockSession);
		cartService.addOrUpdateMenuOfCart(menuWantToPurchaseDto2, mockSession);
		cartService.addOrUpdateMenuOfCart(menuWantToPurchaseDto3, mockSession);
		
		//when //then
		mock.perform(get("/cart/menus").session(mockSession)
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(menuWantToPurchaseDto1.getId().intValue())))
			.andExpect(jsonPath("$[0].name", is(menuWantToPurchaseDto1.getName())))
			.andExpect(jsonPath("$[0].price", is(menuWantToPurchaseDto1.getPrice())))
			.andExpect(jsonPath("$[0].numberOfWantToBuy", is(menuWantToPurchaseDto1.getNumberOfWantToBuy())))
			.andExpect(jsonPath("$[1].id", is(menuWantToPurchaseDto2.getId().intValue())))
			.andExpect(jsonPath("$[1].name", is(menuWantToPurchaseDto2.getName())))
			.andExpect(jsonPath("$[1].price", is(menuWantToPurchaseDto2.getPrice())))
			.andExpect(jsonPath("$[1].numberOfWantToBuy", is(menuWantToPurchaseDto2.getNumberOfWantToBuy())))
			.andExpect(jsonPath("$[2].id", is(menuWantToPurchaseDto3.getId().intValue())))
			.andExpect(jsonPath("$[2].name", is(menuWantToPurchaseDto3.getName())))
			.andExpect(jsonPath("$[2].price", is(menuWantToPurchaseDto3.getPrice())))
			.andExpect(jsonPath("$[2].numberOfWantToBuy", is(menuWantToPurchaseDto3.getNumberOfWantToBuy())));
			
	}
	
	@Test
	@DisplayName("장바구니에 메뉴 추가 요청을 보내면 메뉴를 추가한다.")
	void when_requestAddMenuToCart_then_MenuMustBeAdded() throws Exception {
		//given
		//mockSession init
		MockHttpSession	mockSession = new MockHttpSession();
		
		//when
		String content = objectMapper.writeValueAsString(menuWantToPurchaseDto1);
		mock.perform(patch("/cart/menus").session(mockSession)
				.content(content)
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		//then
		List<MenuWantToPurchaseDto> cartMenuList = cartService.getCartMenuList(mockSession);
		assertThat(cartMenuList, contains(menuWantToPurchaseDto1));
	}
	
	@Test
	@DisplayName("장바구니 삭제를 요청하면 지워져야 한다.")
	void when_deleteCartRequest_then_theCartMustBeDeleted() throws Exception {
		
		//given
		//mockSession init
		MockHttpSession	mockSession = new MockHttpSession();
		
		cartService.addOrUpdateMenuOfCart(menuWantToPurchaseDto1, mockSession);
		
		//when
		mock.perform(delete("/cart/menus").session(mockSession)
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		//then
		//삭제된 카트의 리스트를 요청하면 빈카트 리스트를 반환한다.
		List<MenuWantToPurchaseDto> cartMenuList = cartService.getCartMenuList(mockSession);
		assertTrue(cartMenuList.isEmpty());
	}
	
	@Test
	@DisplayName("특정 메뉴 삭제를 요청하면 해당 메뉴만 삭제되어야 한다.")
	void when_requestDeleteMenu_then_deleteOnlyTheMenu() throws Exception {
		
		//given
		//mockSession init
		MockHttpSession	mockSession = new MockHttpSession();
		
		//mockDto init
		MenuWantToPurchaseDto menuWantToPurchaseDto2 = new MenuWantToPurchaseDto(2L); //menu ID가 2인 menu
		menuWantToPurchaseDto2.setName("국밥");
		menuWantToPurchaseDto2.setPrice(8000);
		menuWantToPurchaseDto2.setNumberOfWantToBuy(5);
	
		MenuWantToPurchaseDto menuWantToPurchaseDto3 = new MenuWantToPurchaseDto(3L); //menu ID가 3인 menu
		menuWantToPurchaseDto3.setName("보쌈");
		menuWantToPurchaseDto3.setPrice(25000);
		menuWantToPurchaseDto3.setNumberOfWantToBuy(3);
				
		cartService.addOrUpdateMenuOfCart(menuWantToPurchaseDto1, mockSession);
		cartService.addOrUpdateMenuOfCart(menuWantToPurchaseDto2, mockSession);
		cartService.addOrUpdateMenuOfCart(menuWantToPurchaseDto3, mockSession);
		
		//when
		mock.perform(delete("/cart/menus/2").session(mockSession)
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		
		//then
		List<MenuWantToPurchaseDto> cartMenuList = cartService.getCartMenuList(mockSession);
		//sort
		Collections.sort(cartMenuList, (e1, e2)->{
				Long result = e1.getId()-e2.getId();
				return result.intValue();
			});
		
		//catains matcher는 요소들의 수와 순서가 모두 같아야함
		assertThat(cartMenuList, contains(menuWantToPurchaseDto1, menuWantToPurchaseDto3));
	}
	
}

	