# jpa_learn
JPA 학습 테스트용

> JPA 구현체 중 Hibernate 사용
>> JPA 사용 시 EntityManagerFactory를 생성하기 위해서 persistence.xml 파일은 무조건 META-INF 폴더 밑에 있어야 한다(기본값, 웹이 아니어도).
>> XML 작성 시 &lt;property name="hibernate.c3p0.acquireRetryDelay"&gt;250&lt;/property&gt; 와 같이 값을 바깥쪽으로 뽑으면 에러가 발생한다.
>> EntityManagerFactory 는 하나만 설정되어야 한다(비용이 큼).
>>> EntityManagerFactory를 하나만 생성하고, EntityManager를 계속 생성하면 된다(CRUD에 사용되는 객체).
>>>> createQuery에 JPQL을 작성하여 사용할 경우 대소문자 엄격하게 구분함(entity의 클래스명과 동일해야 함)
>> JPQL 사용 시 asterisk(Select * From)과 같이 사용 못함(Select m From Member m 과 같이 사용해야 함)
>>>>> 키로 걸려 있을 경우 List 해제가 실시간으로 이뤄지지 않음.

> MySQL 사용

- [X] DDL 사용
- [X] Member 테이블과 Board 테이블 연관관계 걸어보기(1 대 다, 참조키 설정해야 함) : 단방향, 양방향 둘 다
- [X] 상속 매핑 걸어보기(Item, Book, Snack) extends

>>>> @GeneratedValues에 특정 전략을 선택하지 않을 경우 Table 전략으로 생성됨
>>>> InheritanceType.TABLE_PER_CLASS으로 설정하였을 경우 GeneratedValue로 자동 키 생성 시 전략을 IDENTIFY로 설정했더니 에러가 발생하였다.
>>>> @Entity는 @Entity(@Inheritance를 통한 전략과 @DiscriminatorColumn 사용. @PrimaryKeyJoinColumn으로 ID 재정의)이거나 @MappedSuperclass 클래스만 상속 가능

> 복합키(JPA에서의 복합키는 @IdClass, @EmbeddedId로 지정)
>> 식별 관계 : 부모 테이블의 기본키를 자식 테이블의 기본키로 사용
>> 비식별 관계 : 부모 테이블의 기본 키를 자식 테이블의 외래키로만 사용. 자식 테이블 삽입 시 Join이 걸려있는 부모 테이블의 객체가 DB에 저장되어 있지 않으면, RuntimeException 발생
>>> 필수적 비식별 : 외래키에 Null 허용 안 함
>>> 선택적 비식별 : 외래키에 Null 허용