package pe.edu.upc.soft.work.platform.shared.infrastructure.cloudinary.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

  @Value("${CLOUDINARY_URL}")
  private String cloudinaryUrl;

  @Bean
  public Cloudinary cloudinary() {
    return new Cloudinary(cloudinaryUrl);
  }

}
