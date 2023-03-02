package com.kokkan.batch.job.enums;

import lombok.Getter;

@Getter
public enum ResCodeEnum {

	C0000("0000", "성공", false),
	C0100("0100", "주소값 검색 범위 초과", false),
	C0101("0101", "원거래 없음", false),
	C0102("0102", "세션 종료", false),
	C0400("0400", "유효하지 않은 요청 정보", false),
	C0401("0401", "전화번호 또는 비밀번호가 유효하지 않습니다.", false),
	C1000("1000", "데이터 조회 불가", false),

	/*
	이하 효성 연결 시 에러 코드
	 */
	C2001("2001", "회원 등록 실패", false),

	/*
	이하 서비스 에러코드
	 */
	C9090("9090", "외부연결 실패", false),
	C9091("9091", "I/O Stream 생성 실패", false),
	C9092("9092", "Stream Write 실패", false),
	C9093("9093", "Stream Read 실패", false),
	C9094("9094", "요청 데이터 생성 실패", false),
	C9095("9095", "응답 데이터 파싱 실패", false),
	C9999("9999", "시스템오류", false)

	;

	private String code;
	private String msg;
	private boolean isNotify;

	private ResCodeEnum(String code, String msg, boolean isNotify) {
		this.code = code;
		this.msg = msg;
		this.isNotify = isNotify;
	}


}
