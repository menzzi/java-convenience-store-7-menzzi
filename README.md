# java-convenience-store-precourse

## 기능목록

- [ ]  결제 시스템
    - [ ]  구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내
    - [ ]  사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산
        - [ ]  총구매액은 상품별 가격과 수량을 곱하여 계산하며, 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출
- [ ]  영수증
    - [ ]  구매 내역과 산출한 금액 정보
        - [ ]  고객의 구매 내역과 할인을 요약하여 출력
    - [ ]  영수증 항목
        - [x]  구매 상품 내역: 구매한 상품명, 수량, 가격
        - [x]  증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
        - [x]  금액 정보
            - [x]  총구매액: 구매한 상품의 총 수량과 총 금액
            - [x]  행사할인: 프로모션에 의해 할인된 금액
            - [x]  멤버십할인: 멤버십에 의해 추가로 할인된 금액
            - [x]  내실돈: 최종 결제 금액

      → 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.

- [ ]  재고 관리
    - [x]  각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인
    - [ ]  고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리
    - [ ]  재고를 차감함으로써 시스템은 최신 재고 상태를 유지
    - [x]  고객이 구매할 때 정확한 재고 정보를 제공
    - [x]  구현에 필요한 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.  
    → `src/main/resources/products.md`파일
- [ ]  할인
    - [x]  프로모션 할인
        - [x]  오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인을 적용
        - [x]  프로모션은 N개 구매 시 1개 무료 증정(Buy N Get 1 Free)의 형태로 진행
        - [x]  1+1 또는 2+1 프로모션이 각각 지정된 상품에 적용, 동일 상품에 여러 프로모션이 적용되지 않는다.
        - [x]  프로모션 혜택은 프로모션 재고 내에서만 적용
        - [x]  프로모션 기간 중이라면 프로모션 재고를 우선적으로 차감, 프로모션 재고가 부족할 경우에는 일반 재고를 사용
        - [x]  1️⃣ 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내한다.
        - [x]  2️⃣ 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내한다.
    - [ ]  멤버십 할인
        - [x]  멤버십 회원은 프로모션 미적용 금액의 30%를 할인
        - [ ]  프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용
        - [x]  멤버십 할인의 최대 한도는 8,000원
- [x]  입력
    - [x]  구매할 상품과 수량을 입력 ( 형식 : [콜라-10],[사이다-3])
    - [x]  Y/N 입력 -> 1️⃣ 2️⃣ 3️⃣ 4️⃣
- [ ]  출력
    - [x]  환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내 
      - [x] 만약 재고가 0개라면`재고 없음`을 출력
      - [x] 만약 재고가 프로모션 상품만 있다면 일반 상품도 출력 (재고없음으로)
    - [x]  1️⃣ 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력
    - [x]  2️⃣ 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력
    - [x]  3️⃣ 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력
    - [ ]  영수증 출력  
       - [x] 4️⃣ 추가 구매를 진행할지 또는 종료할지를 선택

## 예외상황

→ `IllegalArgumentException`를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는다.
- [ ] [ERROR] + e.getMessage() 잊지말기 controller에서 [ERROR] 꼭 더해주기
- [x]  구매할 상품과 수량 형식이 올바르지 않은 경우: `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
- [x]  존재하지 않는 상품을 입력한 경우: `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`
- [x]  구매 수량이 재고 수량을 초과한 경우: `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`
- [ ]  기타 잘못된 입력의 경우: `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`  
   - [x] Y/N 말고 다른 문자,숫자를 입력한 경우 (소문자 포함)

### 프로그래밍 요구사항
(내가 실수할 수 있는 부분들만 가져옴)
- [ ]  indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현. 2까지만 허용
- [ ]  함수(또는 메서드)가 한 가지 일만 하도록 최대한 작게
- [ ]  Java Enum을 적용하여 프로그램을 구현
- [ ]  함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현

---
### [ 1,2,3 주차 반성 ]

- [ ]  예외상황들을 계속 생각해보자 (진짜 끝까지 계속)
- [ ]  READEME.md를 나만의 조건들을 함꼐 상세히 명시하자.
- [ ]  clear()와 같은 API 제공하는 메서드 활용하자.
- [ ]  객체를 객체답게 사용하자
- [ ]  기능 분리 좀 더 머리 굴려서 잘하자
- [ ]  하드코딩 피하자
- [ ]  접근제어자도 신경써주자
- [ ]  마지막으로 최선을 다하자

### [ 적용해볼 3주차 피드백 ] 

- [ ]  예외 상황에 대한 고민한다
- [ ]  비즈니스 로직과 UI 로직의 분리한다  
객체의 상태를 보기 위한 로그 메시지 성격이 강하다면, **toString()** 메서드를 통해 상태를 표현한다. 만약 UI에서 사용할 데이터가 필요하다면 getter 메서드를 통해 View 계층으로 데이터를 전달한다.

- [ ]  **final 키워드**를 사용해 값의 변경을 막는다
- [ ]  **객체의 상태 접근을 제한**한다  
객체의 상태 접근을 제한하는 것은 캡슐화(Encapsulation)의 중요한 원칙 중 하나다. 인스턴스 변수의 접근 제어자를 private으로 설정하면 외부에서 직접 해당 변수에 접근하거나 수정하는 것을 방지하여 객체의 상태는 외부에서 통제되지 않고, 객체 내에서만 관리될 수 있다.

- [ ]  객체는 객체답게 사용한다  
**Lotto**에서 데이터를 꺼내지(get) 말고 메시지를 던지도록 구조를 바꿔 데이터를 가지는 객체가 일하도록 한다. 이처럼 **Lotto** 객체에서 데이터를 꺼내(get) 사용하기보다는, 데이터가 가지고 있는 객체가 스스로 처리할 수 있도록 구조를 변경해야 한다. 아래와 같이 데이터를 외부에서 가져와(get) 처리하지 말고, 객체가 자신의 데이터를 스스로 처리하도록 메시지를 던지게 한다.
`상태 데이터를 꺼내 로직을 처리하도록 구현하지 말고 객체에 메시지를 보내 일을 하도록 리팩토링한다.`  
**getter를 무조건 사용하지 말라는 말은 아니다.**  
당연히 getter를 무조건 사용하지 않고는 기능을 구현하기 힘들것이다. 출력을 위한 값 등 순수 값 프로퍼티를 가져오기 위해서라면 어느정도 getter는 허용된다. 그러나, Collection 인터페이스를 사용하는 경우 외부에서 getter메서드로 얻은 값을 통해 상태값을 변경할 수 있다.
이처럼 `Collections.unmodifiableList()` 와 같은 `Unmodifiable Collecion` 을 사용해 외부에서 변경하지 못하도록 하는 게 좋다.

- [ ]  필드(인스턴스 변수)의 수를 줄이기 위해 노력한다
- [ ]  테스트 코드도 코드다  
특히, 반복적으로 수행하는 부분이 있다면 중복을 제거하여 유지보수성을 높이고 가독성을 향상시켜야 한다. 단순히 파라미터 값만 바뀌는 경우라면, 파라미터화된 테스트를 통해 중복을 줄일 수 있다.

- [ ]  단위 테스트하기 어려운 코드를 단위 테스트하기 ⇒ LottoMachine으로 분리하기
- [ ]  private 함수를 테스트 하고 싶다면 클래스(객체) 분리를 고려한다

---
## 전체 구조