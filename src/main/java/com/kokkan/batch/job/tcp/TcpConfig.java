package com.kokkan.batch.job.tcp;


import com.kokkan.batch.job.enums.ResCodeEnum;
import com.kokkan.batch.job.exception.UnCheckedException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TcpConfig {

    private final String ip;
    private final int port;
    private int connectionTimeout;
    private int readTimeout;

    private final int lengthPosition;
    private final int preHeaderLength;

    @Builder
    public TcpConfig(String ip, int port, int connectionTimeout, int readTimeout, int lengthPosition, int preHeaderLength) {
        super();
        this.ip = ip;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.lengthPosition = lengthPosition;
        this.preHeaderLength = preHeaderLength;

        if (this.ip == null) {
            throw new UnCheckedException(ResCodeEnum.C9999, "ip is null");
        } else if (this.port == 0) {
            throw new UnCheckedException(ResCodeEnum.C9999, "port is null");
        }

        if (this.connectionTimeout == 0) {
            this.connectionTimeout = 5000;
        }
        if (this.readTimeout == 0) {
            this.readTimeout = 30000;
        }

    }


}
