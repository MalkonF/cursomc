package me.malkon.cursomc.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	/* Faz o upload de um arquivo la para o bucket no s3 */
	public void uploadFile(String localFilePath) {
		try {
			// pega caminho q esta o arquivo a ser enviado
			File file = new File(localFilePath);
			LOG.info("Iniciando upload");// teste é o nome q vc quer dar p o arquivo qnd ele estiver na nuvem
			s3client.putObject(new PutObjectRequest(bucketName, "teste.jpg", file));
			LOG.info("Upload finalizado");
		} catch (AmazonServiceException e) {
			LOG.info("AmazonServiceException: " + e.getErrorMessage());
			LOG.info("Status code: " + e.getErrorCode());
		} catch (AmazonClientException e) {
			LOG.info("AmazonClientException: " + e.getMessage());
		}
	}
}
