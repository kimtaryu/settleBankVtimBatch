package com.kokkan.batch.job.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author PC-2036
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface FixedLength {

	//dataType 상수
	public static final int DT_STRING = 10;
	public static final int DT_NUMBER = 11;
	public static final int DT_BINARY = 12;


	/**
	 * 로깅 시 필드 이름
	 * @return
	 */
	public String name();

	/**
	 * 필드 고정 길이
	 * @return
	 */
	public int length() default 0;

	/**
	 * 가변데이터 길이 데이터의 길이
	 * @return
	 */
	public int variableLength() default 0;


	/**
	 * 가변데이터 길이 데이터의 길이
	 * @return
	 */
	public int variableLengthPosition() default 0;

	/**
	 * 가변데이터 길이 정보 타입
	 *
	 * @return
	 */
	public int variableLengthDataType() default DT_STRING;


	/**
	 * 필드 내 데이터 타입
	 * ex : DT_XXXX
	 * @return
	 */
	public int dataType() default DT_STRING;		//데이터 타입



	/** 옵션 필드
	 * @return
	 */
	public byte[] optionField() default {0x00};


	/**
	 * 데이터 파싱 전 배열 내 길이 만큼 고정 길이 파싱
	 * @return
	 */
	public int[] preLengthList() default {};

	/**
	 * 가변길이 파싱 시 가변길이 포함 여부
	 * @return
	 */
	public boolean includeLengthData() default false;

}
