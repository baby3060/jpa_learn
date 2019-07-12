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
>> 복합키 클래스에는 항상 equals와 hashCode 구현하기
>> 식별 관계 : 부모 테이블의 기본키를 자식 테이블의 기본키로 사용
>> 비식별 관계 : 부모 테이블의 기본 키를 자식 테이블의 외래키로만 사용. 자식 테이블 삽입 시 Join이 걸려있는 부모 테이블의 객체가 DB에 저장되어 있지 않으면, RuntimeException 발생
>>> 필수적 비식별 : 외래키에 Null 허용 안 함
>>> 선택적 비식별 : 외래키에 Null 허용
>>>> 장단점 : @IdClass는 @EmbeddedClass 보다 객체 지향적이지는 않지만, JPQL이 보다 간단함. @EmbeddedClass의 경우 @IdClass 보다 객체지향적이긴 하지만, JPQL이 더 복잡해질 가능성이 큼


> 로딩 전략 : 즉시 로딩, 지연 로딩(조회해 옴과 동시에 Join 걸어 있는 데이터를 불러올 것인지? 아니면, 필요할 때 불러올 것인지?)
>> 지연 로딩으로 가져올 시 team.getMemberList()와 같이 불러온다고 하더라도, 쿼리문을 날리는 것이 아니라 team.getMemberList().get(0)과 같이 실제로 사용할 때 쿼리를 날려서 데이터를 가져온다.
>> @ManyToOne, @OneToOne의 경우 즉시 로딩이 기본값이고, @OneToMany, @ManyToMany의 기본이 지연 로딩.
>>> 하나의 엔티티에서 둘 이상의 컬렉션을 즉시 로딩하는 것은 좋지 않음.
>>> 컬렉션의 즉시 로딩은 항상 Outer 조인을 사용한다(optional의 값에 따라 Inner 조인 또한 사용. @OneToMany와 @ManyToMany는 무조건 Outer Join).

> 영속성 전이 : 연관된 엔티티도 함께 영속 상태로 만들고자 할 때 사용(부모 엔티티를 저장할 때 자식 엔티티도 함께 저장)
>> CascadeType.PERSIST : persist 호출하여 부모 엔티티 저장 시 자식 엔티티도 함께 저장
>> CascadeType.REMOVE : remove 호출하여 부모 엔티티 삭제 시 자식 엔티티도 함께 삭제
>>> 두 CascadeType 모두 flush 시 전이 발생

> 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 기능 : 고아 객체(ORPHAN) 제거, orphanRemoval = true
>> 부모 엔티티의 컬렉션에서 자식 엔티티의 참조만 제거하면 자식 엔티티가 자동으로 삭제
>>> CascadeType.ALL과 orphanRemoval = true를 모두 설정하면 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있다(자식 저장 및 자식 삭제 모두).