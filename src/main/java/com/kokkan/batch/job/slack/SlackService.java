package com.kokkan.batch.job.slack;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackService {
	private final AppSlack appSlack;

	public void send(String message) {
		log.debug("send message : "+message);
		appSlack.send(message);
	}

}
