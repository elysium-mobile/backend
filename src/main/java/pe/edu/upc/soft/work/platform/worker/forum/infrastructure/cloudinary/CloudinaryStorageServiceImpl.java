package pe.edu.upc.soft.work.platform.worker.forum.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.soft.work.platform.worker.forum.domain.model.valueObjects.FileType;
import pe.edu.upc.soft.work.platform.worker.forum.domain.services.ExternalStorageService;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryStorageServiceImpl implements ExternalStorageService {

  private static final String FOLDER_VIDEOS = "workersforum/videos";
  private static final String FOLDER_IMAGES = "workersforum/images";
  private static final String FOLDER_PDFS   = "workersforum/pdfs";

  private final Cloudinary cloudinary;

  public CloudinaryStorageServiceImpl(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }


  @Override
  public UploadResult upload(MultipartFile file, FileType fileType) {
    try {
      Map<?, ?> uploadOptions = buildUploadOptions(fileType);
      Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), uploadOptions);

      String url      = (String) result.get("secure_url");
      long   bytes    = ((Number) result.get("bytes")).longValue();
      String fileSize = formatFileSize(bytes);

      return new UploadResult(url, fileSize);

    } catch (IOException e) {
      throw new RuntimeException(
          "[CloudinaryStorageServiceImpl] Error al subir el archivo a Cloudinary: " + e.getMessage(), e);
    }

  }

  @Override
  public void delete(String publicUrl) {
    try {
      String publicId = extractPublicId(publicUrl);
      cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    } catch (IOException e) {
      throw new RuntimeException(
          "[CloudinaryStorageServiceImpl] Error al eliminar el archivo de Cloudinary: " + e.getMessage(), e);
    }
  }

  private Map<?, ?> buildUploadOptions(FileType fileType) {
    return switch (fileType) {
      case VIDEO -> ObjectUtils.asMap(
          "resource_type", "video",
          "folder",        FOLDER_VIDEOS
      );
      case JPEG -> ObjectUtils.asMap(
          "resource_type", "image",
          "folder",        FOLDER_IMAGES
      );
      case PDF -> ObjectUtils.asMap(
          "resource_type", "raw",
          "folder",        FOLDER_PDFS
      );
    };
  }

  private String extractPublicId(String secureUrl) {
    String afterUpload = secureUrl.substring(secureUrl.indexOf("/upload/") + 8);
    int dotIndex = afterUpload.lastIndexOf('.');
    return (dotIndex != -1) ? afterUpload.substring(0, dotIndex) : afterUpload;
  }

  private String formatFileSize(long bytes) {
    if (bytes < 1024)       return bytes + " B";
    if (bytes < 1048576)    return String.format("%.1f KB", bytes / 1024.0);
    if (bytes < 1073741824) return String.format("%.1f MB", bytes / 1048576.0);
    return String.format("%.1f GB", bytes / 1073741824.0);
  }
}
