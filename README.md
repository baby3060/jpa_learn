# jpa_learn
JPA 학습 테스트용

> JPA 구현체 중 Hibernate 사용
>> JPA 사용 시 EntityManagerFactory를 생성하기 위해서 persistence.xml 파일은 무조건 META-INF 폴더 밑에 있어야 한다(기본값, 웹이 아니어도).
>> XML 작성 시 &lt;property name="hibernate.c3p0.acquireRetryDelay"&gt;250&lt;/property&gt; 와 같이 값을 바깥쪽으로 뽑으면 에러가 발생한다.
>> EntityManagerFactory 는 하나만 설정되어야 한다(비용이 큼).
>>> EntityManagerFactory를 하나만 생성하고, EntityManager를 계속 생성하면 된다(CRUD에 사용되는 객체).
>>>> createQuery에 JPQL을 작성하여 사용할 경우 대소문자 엄격하게 구분함(entity의 클래스명과 동일해야 함)
>> JPQL 사용 시 asterisk(Select * From)과 같이 사용 못함(Select m From Member m 과 같이 사용해야 함)

> MySQL 사용

- [ ] Member 테이블과 Board 테이블 연관관계 걸어보기(1 대 다, 참조키 설정해야 함) : 단방향, 양방향 둘 다
- [ ] 상속 매핑 걸어보기(Item, Book, Snack) extends