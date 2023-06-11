/* EAIMD - eaimd.org - 2023 */
package org.eaimd.ai_md.producer.caller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eaimd.activator.Activator;
import org.eaimd.ai_md.producer.caller.model.AiMdCallerRequest_body;
import org.eaimd.ai_md.producer.caller.model.AiMdCallerRequest_message;
import org.eaimd.ai_md.producer.caller.model.AiMdCallerResponse_body;
import org.eaimd.ai_md.utils.CU;
import org.eclipse.core.runtime.Platform;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiMdCaller {

	static String url = "https://api.openai.com/v1/chat/completions";
	
	static String header_Authorization = findAuthorization();
	
	static String aiTextAction = "Rephrase the text (and keep the Markdown tags)";
	
	static String aiModel = "gpt-3.5-turbo-0301";
	
	static String aiRole = "user";

	@SneakyThrows(IOException.class)
	private static String findAuthorization() {

		return CU.loadInputStreamInString(Platform.getBundle(Activator.PLUGIN_ID).getResource("secrets/ChatGPT_Authorization.txt").openStream()); 
	}
	
	@SneakyThrows({URISyntaxException.class, InterruptedException.class, IOException.class})
	public static String callRephrase(String text) {
		
		AiMdCallerRequest_message request_message = new AiMdCallerRequest_message(aiRole, aiTextAction + ": " + text);
		
		AiMdCallerRequest_body request_body = new AiMdCallerRequest_body(aiModel);
		request_body.getMessages().add(request_message);
		
		
		String requestBody = CU.jsonSerialize(request_body);
		System.out.println(requestBody);
		
		HttpClient httpClient = HttpClient.newHttpClient();
		
		URI postURI = new URI(url);
		
		HttpRequest httpRequestPost = HttpRequest.newBuilder()
                .uri(postURI)
                .header("Content-Type", "application/json")
                .header("Authorization", header_Authorization)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
		
		HttpResponse<String> postResponse = httpClient.send(httpRequestPost, HttpResponse.BodyHandlers.ofString());
		System.out.println(postResponse.body());
		
		try {
			AiMdCallerResponse_body response_body = CU.jsonDeserialize(postResponse.body(), AiMdCallerResponse_body.class);
			
			String resultText = response_body.getChoices().get(0).getMessage().getContent();
			
			System.out.println(resultText);
			
			return resultText;
			
		} catch (Exception e) {
			return "result text error";
		}
	}
}
