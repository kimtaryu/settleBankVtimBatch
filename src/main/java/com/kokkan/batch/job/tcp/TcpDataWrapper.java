package com.kokkan.batch.job.tcp;

public interface TcpDataWrapper {

    byte[] encryption(byte[] data);

    byte[] decryption(byte[] encData);

}
