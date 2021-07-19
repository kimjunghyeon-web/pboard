package com.icia.pboard.entity;

// 정적초기화
// static {  }

/* 
	전통적인 방식의 상수는 인터페이스를 이용한 약속
	public interface Level {
		// static : 클래스 소속. 모든 객체가 공유
		// final : 변경 불가. 단 생성할 때 값을 지정해야 한다 -> 인스턴스 초기화, (정적 초기화), 생성자
		//         객체마다 값이 다르고 변경불가능한 상수(정기예금 금리)
		// final static : 모든 객체가 공유하는 상수
 		public static final Integer NORMAL = 1;
 		public static final Integer SILVER = 2;
 		public static final Integer GOLD = 3;
 	}
 	문제점
 	int level = Level.NORMAL;		// 타입이 int이므로 상수라는 티가 나지 않는다
 	int level = 10;					// 정상 실행
 	
	
 	enum을 사용하면
 	Level level = Level.NORMAL;
 	Level level = 10; 				// 오류
*/
public enum Level {
	NORMAL, SILVER, GOLD;
}
