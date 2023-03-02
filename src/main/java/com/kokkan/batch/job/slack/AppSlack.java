package com.kokkan.batch.job.slack;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.webhook.WebhookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @author PC-2036
 * Slack 알림 처리 서비스
 *
 */
@Slf4j
@Component
public class AppSlack {

	@Value("${slack.webhookUrl}")
	String webhookUrl;

	@Value("${slack.channel}")
	String channel;

	@Value("${slack.username}")
	String username;

	@Value("${slack.iconEmoji}")
	String iconEmoji;

	@Value("${slack.connection-timeout}")
	int conTimeout;

	@Value("${slack.read-timeout}")
	int readTimeout;

	private Slack slack;

	@PostConstruct
	private void init() {
		SlackConfig slackConfig = new SlackConfig();
		slackConfig.setHttpClientCallTimeoutMillis(conTimeout);
		slackConfig.setHttpClientReadTimeoutMillis(readTimeout);
		slack = Slack.getInstance(slackConfig);
	}


	public void send(String message) {

		if(StringUtils.hasText(message)){
			Gson gson = new Gson();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("text", message);
			jsonObject.addProperty("channel", channel);
			jsonObject.addProperty("username", username);
			jsonObject.addProperty("icon_emoji", iconEmoji);
			String jsonStr = gson.toJson(jsonObject);

			String payload = jsonStr;

			try {
				WebhookResponse response = slack.send(webhookUrl, payload);
			} catch (Exception e) {
				log.error("Exception : "+e);
			}
		}

	}


}
