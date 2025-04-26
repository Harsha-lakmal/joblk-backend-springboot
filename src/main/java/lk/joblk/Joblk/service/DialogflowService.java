package lk.joblk.Joblk.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


@Service
public class DialogflowService {

    public String detectIntentTexts(String projectId, String text, String sessionId, String languageCode) throws Exception {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource("dialogflow.json").getInputStream());

        SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (SessionsClient sessionsClient = SessionsClient.create(sessionsSettings)) {
            SessionName session = SessionName.of(projectId, sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(request);
            return response.getQueryResult().getFulfillmentText();
        }
    }
}