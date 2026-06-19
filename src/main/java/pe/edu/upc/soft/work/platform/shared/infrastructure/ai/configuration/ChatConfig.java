package pe.edu.upc.soft.work.platform.shared.infrastructure.ai.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {
  @Bean
  public ChatClient chatClient(ChatModel chatModel){
    return ChatClient.builder(chatModel).defaultSystem("""
          Eres un asistente especializado en encuestas de clima laboral (Dentro del Feedback BC del proyecto).
          Ayudas a redactar preguntas, resumir resultados y sugerir mejoras de encuestas.
          Responde siempre en español y de forma breve.
        """).build();
  }

}
