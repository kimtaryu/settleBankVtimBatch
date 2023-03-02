package com.kokkan.batch.job.settlebank;

import com.kokkan.batch.job.convert.AbstractConvertVo;
import com.kokkan.batch.job.convert.FixedLength;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 새틀뱅크 출금 요청 VO 객체 (공통부)
 */
@Getter
@NoArgsConstructor
public class HeaderVo extends AbstractConvertVo {

    @FixedLength(length = 4, name = "LENGTH")
    private String length;    //기관 코드
    @FixedLength(length = 8, name = "기관코드")
    private String mchtId;    //기관 코드
    @FixedLength(length = 4, name = "거래종류코드")
    private String transactionType; //거래종류코드
    @FixedLength(length = 1, name = "송수신코드")
    private String transferCode;    //송수신코드
    @FixedLength(length = 8, name = "거래일자")
    private String tradeDate; //거래일자 (yyyymmdd)
    @FixedLength(length = 6, name = "거래시간")
    private String tradeTime; //거래시간 HHMMSS
    @FixedLength(length = 2, name = "개설은행코드")
    private String bankCode; //개설은행코드
    @FixedLength(length = 7, name = "거래번호")
    private String transactionId; //거래번호
    @FixedLength(length = 4, name = "응답코드")
    private String resCode; //응답코드
    @FixedLength(length = 3, name = "신개설은행코드")
    private String newBankCode; //신 개설 은행코드
    @FixedLength(length = 4, name = "예약영역1")
    private String filler1; //예약필드1
    @FixedLength(length = 7, name = "예약영역2")
    private String filler2; //예약필드2
    @FixedLength(length = 7, name = "사용자거래번호")
    private String userTransactionId;
    @FixedLength(length = 1, name = "발생구분")
    private String type;    //발생구분 (O : 기관)
    @FixedLength(length = 1, name = "전문정송방식구분")
    private String formatSendType;  //전문전송방식구분
    @FixedLength(length = 2, name = "예약영역")
    private String filler3; //예약 필드
    @FixedLength(length = 1, name = "자동취소여부")
    private String autoRefundType;  //자동취소 여부

    @Builder
    public HeaderVo(String length, String mchtId, String transactionType, String transferCode, String tradeDate, String tradeTime, String bankCode, String transactionId, String resCode, String newBankCode, String filler1, String filler2, String userTransactionId, String type, String formatSendType, String filler3, String autoRefundType) {
        this.length = length;
        this.mchtId = mchtId;
        this.transactionType = transactionType;
        this.transferCode = transferCode;
        this.tradeDate = tradeDate;
        this.tradeTime = tradeTime;
        this.bankCode = bankCode;
        this.transactionId = transactionId;
        this.resCode = resCode;
        this.newBankCode = newBankCode;
        this.filler1 = filler1;
        this.filler2 = filler2;
        this.userTransactionId = userTransactionId;
        this.type = type;
        this.formatSendType = formatSendType;
        this.filler3 = filler3;
        this.autoRefundType = autoRefundType;
    }
}
