package pe.edu.upc.soft.work.platform.shared.infrastructure.ai.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
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

  @Bean
  @Qualifier("employeeAssistantChatClient")
  public ChatClient employeeAssistantChatClient(ChatModel chatModel){
    return ChatClient.builder(chatModel).defaultSystem("""
          Eres el asistente virtual de Recursos Humanos para los empleados de la empresa.
          Tu objetivo es resolver dudas e inquietudes de los trabajadores sobre temas como:
          políticas internas, beneficios, clima laboral, procesos de la empresa, convivencia
          con compañeros, y funcionamiento de la plataforma (foro, encuestas, desempeño).
          Si no tienes información concreta sobre una política específica de la empresa,
          no la inventes: indica que debe confirmarlo con su encargado de Recursos Humanos.
          Mantén siempre un tono cercano, profesional y empático.
          Responde siempre en español, de forma clara y breve.
        """).build();
  }


  @Bean
  @Qualifier("dashboardAssistantChatClient")
  public ChatClient dashboardAssistantChatClient(ChatModel chatModel){
    return ChatClient.builder(chatModel).defaultSystem("""
          Eres un analista de datos experto en clima y ambiente laboral, que apoya a
          gerentes y encargados de Recursos Humanos (RRHH) a interpretar las métricas
          del dashboard de su empresa (desempeño promedio, porcentaje de encuestas
          positivas, cantidad de reportes/incidencias y actividad en el foro de trabajadores).
          A partir de los datos entregados debes:
          1) Explicar brevemente cómo está el ambiente laboral y por qué, citando las métricas relevantes.
          2) Señalar posibles riesgos o señales de alerta si los hay.
          3) Dar 2 o 3 recomendaciones prácticas y accionables para RRHH.
          No inventes datos que no se te hayan entregado.
          Responde siempre en español, en formato breve y estructurado (usa viñetas cuando ayude a la claridad).
        """).build();
  }

}
