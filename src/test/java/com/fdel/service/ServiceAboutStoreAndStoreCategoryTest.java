package com.fdel.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fdel.entity.RestaurantType;
import com.fdel.entity.RestaurantType.Name;
import com.fdel.entity.Store;
import com.fdel.repository.StoreRepository;

/**
 * Store과 StoreCategory에 관련된 서비스를 테스트합니다.
 */
@Transactional
@ActiveProfiles("test") //test profile은 embedded h2 DB를 사용해서 테스트 합니다.
@SpringBootTest
public class ServiceAboutStoreAndStoreCategoryTest {
	
	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	StoreService storeService;
	
	@Autowired
	EntityManager em;
	
	Store store1;
	
	@BeforeEach
	public void beforeEach() {
		store1 =
			Store.builder()
			.name("간장갈비")
			.address("강원도 강릉시 남부로 232 ")
			.zipcode(25611)
			.build();
		
		storeRepository.save(store1); //영속화를 통하여 id를 할당받음
	}

	@Test
	@DisplayName("Store에 StoreCategory를 할당해 저장하면 StoreCategory와 StoreStoreCategory 모두 제대로 저장이 되어야 한다.")
	void when_saveStoreAddedStoreCategory_then_mustBeSavedStoreCategoryAndStoreStoreCategory() {
		//when
		storeService.addAndRegistRestaurantType(store1.getId(), RestaurantType.Name.CKICKEN); //치킨 카테고리를 추가
		em.flush();
		em.clear();
		Store store = storeRepository.findById(store1.getId()).get();
		
		//then
		assertTrue(store.getStoreRestaurantTypeList().stream()
				.filter(ssc->ssc.getStoreCategory().getName().equals(RestaurantType.Name.CKICKEN))
				.findAny()
				.isPresent()); 
	}
	
	@Test
	@DisplayName("Store에 이미 있는 카테고리를 할당하려고 하면 예외가 발생해야 한다.")
	void when_addCategoryToStoreThatIsAlreayExsit_then_throwIllegalStateException() {
		//given
		storeService.addAndRegistRestaurantType(store1.getId(), RestaurantType.Name.CKICKEN); //치킨 카테고리를 추가
		//when then
		assertThrows(IllegalStateException.class, () -> {
	    	storeService.addAndRegistRestaurantType(store1.getId(), RestaurantType.Name.CKICKEN); //치킨 카테고리를 한번 더 추가
	    });
	}
	
	@Test
	@DisplayName("Store에 StoreCategory를 여러개 할당할 수 있다.")
	void multiple_StoreCategories_can_be_assigned_to_a_Store() {
		storeRepository.save(store1);
		//when
		storeService.addAndRegistRestaurantType(store1.getId(), RestaurantType.Name.CKICKEN);
		storeService.addAndRegistRestaurantType(store1.getId(), RestaurantType.Name.PIZZA);
		storeService.addAndRegistRestaurantType(store1.getId(), RestaurantType.Name.RICE_PORRIDGE_NOODLE);
		em.flush();
		em.clear();
		
		//then
		Store store = storeRepository.findById(store1.getId()).get();
		List<RestaurantType> catagoryList = store
			.getStoreRestaurantTypeList()
			.stream().map(ssc->ssc.getStoreCategory())
			.collect(Collectors.toList());
		
		List<Name> catagoryNameList = catagoryList.stream()
			.map(sc->sc.getName()).collect(Collectors.toList());
		
		assertThat(catagoryNameList.size(), equalTo(3));
		assertTrue(catagoryNameList.contains(RestaurantType.Name.CKICKEN));
		assertTrue(catagoryNameList.contains(RestaurantType.Name.PIZZA));
		assertTrue(catagoryNameList.contains(RestaurantType.Name.RICE_PORRIDGE_NOODLE));
	}
	
}
