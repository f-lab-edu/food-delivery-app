# food-delivery-app
주문자와 가게 오너 사이에 배달자를 매칭 시켜주는 O2O 애플리케이션입니다. 주문자는 간편하게 모바일상에서 음식을 구매할 수 있고, 가게 오너는 자동으로 매칭된 배달자에게 음식을 제공하기만 하면 음식을 전달할 수 있습니다.

## 프로젝트 구조
![프로그램아키텍쳐](https://user-images.githubusercontent.com/86475543/171988789-8f5872d4-0804-4746-9765-aaaf23a0ea63.png)

## 배포 구조
![배포아키텍쳐](https://user-images.githubusercontent.com/86475543/171988826-0cd004a0-dc4f-4664-8032-b090ba242058.png)

## 프로젝트의 주요 관심사
- 객체지향적인 코드 구조 설계
- pull request와 review를 통한 나쁜 냄새의 코드의 지속적인 리펙토링
- Google Java convention 준수
- 유의미한 테스트 케이스 작성
- CI/CD 환경 구축
- Spring Security를 통한 OAuth2 인증 구현
- Payment Gateway 연동으로 결제 서비스 제공
- Redis를 활용한 다중 서버 환경 구축

## 브렌치 관리 전략
Git flow 전략을 따름

참고 문헌  
[우아한 형제들 기술 블로그](https://techblog.woowahan.com/2553/)

## 사용 기술 및 인프라
Spring boot, Gradle, Redis, Docker, MySQL, Jenkins, Java11, Naver Cloud Platform

## Wiki
애프리케이션 요구사항과 프로젝트를 진행하며 생겼던 이슈 등을 포스팅한 블로그 url이 포함되어 있습니다.  
[Wiki 바로가기](https://github.com/f-lab-edu/food-delivery-app/wiki)

## ERD
2022-06-02 수정 (Deliver는 추후 추가 예정)

![fooddeliveryapp_er_diagram_2](https://user-images.githubusercontent.com/86475543/171583968-b0c751dd-aad7-470e-8560-0c86adba94bc.png)
