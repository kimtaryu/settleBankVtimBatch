package com.kokkan.batch.job.step.retry;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetryClassifierWriter {

    private final RetrySuccessWriter retrySuccessWriter;
    private final RetryFailureWriter retryFailureWriter;

    public ClassifierCompositeItemWriter classifierCompositeItemWriter() {
        ClassifierCompositeItemWriter compositeItemWriter = new ClassifierCompositeItemWriter();
        compositeItemWriter.setClassifier(new RetryClassifier(retrySuccessWriter, retryFailureWriter));
        return compositeItemWriter;
    }
}


class RetryClassifier implements Classifier<RetryDto, ItemStreamWriter<? super RetryDto>> {

    private ItemStreamWriter<RetryDto> successWriter;
    private ItemStreamWriter<RetryDto> failureWriter;

    public RetryClassifier(ItemStreamWriter<RetryDto> successWriter, ItemStreamWriter<RetryDto> failureWriter) {
        this.successWriter = successWriter;
        this.failureWriter = failureWriter;
    }

    /**
     * 성공 - 응답코드가 0000
     * 그외 실패 처리
     * */
    @Override
    public ItemStreamWriter<RetryDto> classify(RetryDto retryDto) {

        if ("0000".equals(retryDto.getResCode())) {
            return successWriter;
        } else {
            return failureWriter;
        }
    }

}
