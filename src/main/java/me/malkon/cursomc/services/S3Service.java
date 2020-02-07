package me.malkon.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	// retorna UrI p retonar o endereco web do novo recurso q foi gerado
	public URI uploadFile(MultipartFile multipartFile) {
		try {
			String fileName = multipartFile.getOriginalFilename();// extrai o nome do arquivo a ser enviado
			InputStream is = multipartFile.getInputStream();
			/*
			 * Objeto inputStream encapsula o processamento de leitura a apartir de uma
			 * origem, no caso a origem vai ser a img a ser enviada representada por
			 * MultPartFile
			 */
			String contentType = multipartFile.getContentType();// retorna uma string contendo o tipo do arquivo q foi
																// enviado
			return uploadFile(is, fileName, contentType);
		} catch (IOException e) {
			throw new RuntimeException("Erro de IO: " + e.getMessage());
		}
	}

	public URI uploadFile(InputStream is, String fileName, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload finalizado");
			return s3client.getUrl(bucketName, fileName).toURI();// retorna a URI contendo o endereco
		} catch (URISyntaxException e) {
			throw new RuntimeException("Erro ao converter URL para URI");

		}
	}
}