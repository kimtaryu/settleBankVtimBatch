package com.kokkan.batch.job.exception;

import com.kokkan.batch.job.enums.ResCodeEnum;
import lombok.Getter;

/**
 * @author PC-2036
 * Exception에 대한 장애 복구가 필요 없을 경우 사용하는 Exception wrapper.
 * 해당 프로젝트 결제 프로세스 상 장애 복구가 불가능하며, 장애 처리를 진행한다.
 */
@Getter
public class UnCheckedException extends RuntimeException {

    private String location;    //위치 로그
    private String msg;    //응답 메시지
    private String log;    //log 출력 메시지
    private String code;    //client 응답 로그
    private ResCodeEnum resCodeEnum;

    /**
     * 강제 에러 발생 시
     *
     * @param resCodeEnum
     * @param log
     */
    public UnCheckedException(ResCodeEnum resCodeEnum, String log) {
        this.code = resCodeEnum.getCode();
        this.msg = resCodeEnum.getMsg();
        this.log = log;
        this.resCodeEnum = resCodeEnum;
        this.location = getLocationName();
    }

    public UnCheckedException(ResCodeEnum resCodeEnum, String log, String msg) {
        this.code = resCodeEnum.getCode();
        this.msg = msg;
        this.log = log;
        this.resCodeEnum = resCodeEnum;
        this.location = getLocationName();
    }

    /**
     * Exception 처리 시
     *
     * @param resCodeEnum
     * @param e
     */
    public UnCheckedException(ResCodeEnum resCodeEnum, Exception e) {
        super(e);
        this.code = resCodeEnum.getCode();
        this.msg = resCodeEnum.getMsg();
        this.resCodeEnum = resCodeEnum;
        this.location = getLocationName();
    }


    /**
     * Exception 처리 및 로깅 필요 시
     *
     * @param resCodeEnum
     * @param log
     * @param e
     */
    public UnCheckedException(ResCodeEnum resCodeEnum, String log, Exception e) {
        super(e);
        this.code = resCodeEnum.getCode();
        this.msg = resCodeEnum.getMsg();
        this.log = log;
        this.resCodeEnum = resCodeEnum;
        this.location = getLocationName();
    }


    /**
     * Exception 객체 생성 위치
     *
     * @return
     */
    private String getLocationName() {
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        className = className.substring(className.lastIndexOf(".") + 1, className.length());
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        return "[" + className + "::" + methodName + "]";

    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append(location + "errCode : " + code);
        if (msg != null) {
            sb.append(", message : " + msg);
        }
        if (log != null) {
            sb.append(", log : " + log);
        }
        if (super.getMessage() != null) {
            sb.append(", Exception : " + super.getMessage());
        }
        return sb.toString();
    }


}
