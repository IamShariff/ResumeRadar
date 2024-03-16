package com.resumeradar.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.resumeradar.exception.ParsingException;
import com.resumeradar.responsedto.ResumeAnalysisResult;
import com.resumeradar.service.ResumeAnalysisService;

/**
 * Service implementation for analyzing resumes and retrieving top results. This
 * class provides methods to analyze resume content, extract keywords, and
 * calculate scores.
 */
@Service
public class ResumeAnalysisServiceImpl implements ResumeAnalysisService {

	/**
	 * Retrieves the top resumes based on specified keywords, count, and uploaded
	 * resume files.
	 *
	 * @param keywords    List of keywords for filtering resumes.
	 * @param resumeFiles List of resume files uploaded for analysis.
	 * @param count       Number of top resumes to retrieve.
	 * @return List of ResumeAnalysisResult objects representing the top resumes.
	 * @throws ParsingException if an error occurs during resume parsing.
	 */
	@Override
	public List<ResumeAnalysisResult> getTopResumes(List<String> keywords, List<MultipartFile> resumeFiles, int count) {
		return resumeFiles.stream().map(resumeFile -> {
			try {
				String resumeContent = new String(Objects.requireNonNull(resumeFile.getBytes()));
				String pdfContent = extractTextFromPdf(resumeFile);
				ResumeAnalysisResult analysisResult = analyzeResume(pdfContent, resumeContent, keywords);
				return new ResumeAnalysisResult(resumeFile.getOriginalFilename(), analysisResult.getResumeScore());
			} catch (IOException e) {
				throw new ParsingException("resume", "Error while parsing resume");
			}
		}).filter(Objects::nonNull).sorted(Comparator.comparingInt(ResumeAnalysisResult::getResumeScore).reversed())
				.limit(count).toList();
	}

	/**
	 * Analyzes the content of a resume and calculates the matching score with
	 * specified keywords.
	 *
	 * @param pdfContent    Content extracted from the PDF version of the resume.
	 * @param resumeContent Content extracted from the original resume file.
	 * @param keywords      List of keywords for filtering resumes.
	 * @return ResumeAnalysisResult object representing the analysis result.
	 * @throws IOException if an error occurs during I/O operations.
	 */
	private ResumeAnalysisResult analyzeResume(String pdfContent, String resumeContent, List<String> keywords)
			throws IOException {
		try (InputStream modelIn = getClass().getResourceAsStream("/static/en-token.bin")) {
			String[] tokens = pdfContent.split("[\\s\\p{Punct}]+");
			Set<String> uniqueKeywords = new HashSet<>(keywords);
			long matchingKeywordsCount = Arrays.stream(tokens).filter(token -> uniqueKeywords
					.removeIf(keyword -> Objects.equals(token.toLowerCase(), keyword.toLowerCase()))).count();

			double totalKeywords = keywords.size();
			double matchingKeywordsPercentage = (matchingKeywordsCount / totalKeywords) * 100;

			return new ResumeAnalysisResult(resumeContent, (int) Math.round(matchingKeywordsPercentage));
		}
	}

	/**
	 * Extracts text content from a PDF file.
	 *
	 * @param multipartFile MultipartFile representing the PDF resume file.
	 * @return String containing the extracted text from the PDF.
	 * @throws IOException if an error occurs during I/O operations.
	 */
	private String extractTextFromPdf(MultipartFile multipartFile) throws IOException {
		String text;
		try (InputStream inputStream = multipartFile.getInputStream()) {
			PdfReader reader = new PdfReader(inputStream);
			int numPages = reader.getNumberOfPages();
			StringBuilder stringBuilder = new StringBuilder();
			for (int pageNum = 1; pageNum <= numPages; pageNum++) {
				text = PdfTextExtractor.getTextFromPage(reader, pageNum);
				stringBuilder.append(text);
			}
			reader.close();
			return stringBuilder.toString();
		} catch (IOException e) {
			throw new ParsingException("resume", "Error while parsing resume");
		}
	}
}
