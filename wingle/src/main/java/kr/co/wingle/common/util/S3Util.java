package kr.co.wingle.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.BadRequestException;
import kr.co.wingle.common.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Util {

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	public String bucket;

	public String upload(MultipartFile file, String path) throws IOException {
		ObjectMetadata objMeta = new ObjectMetadata();

		byte[] bytes = IOUtils.toByteArray(file.getInputStream());
		objMeta.setContentLength(bytes.length);

		ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

		String fileName = getFileName(file);

		amazonS3Client.putObject(new PutObjectRequest(bucket, path + "/" + fileName, byteArrayIs, objMeta)
			.withCannedAcl(CannedAccessControlList.PublicRead));

		return amazonS3Client.getUrl(bucket, path + "/" + fileName).toString();
	}

	public String idCardImageUpload(MultipartFile file) {
		try {
			return upload(file, "idCardImage");
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new InternalServerErrorException(ErrorCode.FILE_UPLOAD_FAIL);
		}
	}

	public String profileImageUpload(MultipartFile file) {
		try {
			return upload(file, "profileImage");
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new InternalServerErrorException(ErrorCode.FILE_UPLOAD_FAIL);
		}
	}

	public String articleImageUpload(MultipartFile file) throws IOException {
		return upload(file, "article");
	}

	public void delete(String url) {
		String key = url.split("https://wingle-bucket.s3.ap-northeast-2.amazonaws.com/")[1];
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);
		amazonS3Client.deleteObject(deleteObjectRequest);
	}

	private String getFileName(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null) {
			throw new NullPointerException(ErrorCode.BAD_FILE_NAME.getMessage());
		}
		String fileExtension = getFileExtension(originalFilename);

		return LocalDateTime.now()
			.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
			.concat(Long.toString(System.nanoTime()))
			.concat(fileExtension);
	}

	private String getFileExtension(String fileName) {
		if (!fileName.contains(".")) {
			throw new BadRequestException(ErrorCode.BAD_FILE_NAME);
		}
		String fileExtension = fileName.substring(fileName.lastIndexOf("."));

		if (fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".jpeg")
			|| fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".heic")) {
			return fileExtension;
		} else {
			throw new BadRequestException(ErrorCode.BAD_FILE_EXTENSION);
		}
	}
}
