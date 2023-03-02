package com.kokkan.batch.job.settlebank;

import com.kokkan.batch.job.convert.AbstractConvertVo;
import com.kokkan.batch.job.convert.FixedLength;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 새틀뱅크 출금 요청 VO 객체 (공통부)
 */
@Getter
@NoArgsConstructor
public class RemitResVo extends AbstractConvertVo {
    @FixedLength(name = "공통 헤더")
    private HeaderVo headerVo;
    @FixedLength(length = 16, name = "입금계좌번호(암호화)")
    private String accountNum;    //입금받는 계좌 번호
    @FixedLength(length = 20, name = "예금주명")
    private String accountName; //입금받는 예금주명
    @FixedLength(length = 2, name = "입금은행")
    private String bankCode;    //입금받는 은행코드
    @FixedLength(length = 4, name = "입금은행지점(사용X)")
    private String bankBranch; //입금 은행지점 (미사용)
    @FixedLength(length = 13, name = "거래금액(송금금액)", dataType = FixedLength.DT_NUMBER)
    private String remitAmt; //
    @FixedLength(length = 20, name = "송금의뢰인명")
    private String sendName; //송금의뢰인명
    @FixedLength(length = 13, name = "예비필드")
    private String filler1; //예비필드
    @FixedLength(length = 15, name = "CMS_CD")
    private String cmsCd; //CMS_CD
    @FixedLength(length = 14, name = "예비필드")
    private String filler2; //예비필드
    @FixedLength(length = 3, name = "신은행코드")
    private String newBankCode; //신은행코드
    @FixedLength(length = 10, name = "예약필드2")
    private String filler3; //예약필드2

    /**
     * 시스템 에러 발생 시
     *
     * @param headerVo
     */
    public RemitResVo(HeaderVo headerVo) {
        this.headerVo = headerVo;
    }
}
