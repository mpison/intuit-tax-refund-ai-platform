package com.refundplatform.policy.extractor;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@Component
public class PdfPolicyTextExtractor
        implements PolicyTextExtractor {

    @Override
    public boolean supports(
            MultipartFile file) {

        return extension(file)
                .equals("pdf");
    }

    @Override
    public String extract(
            MultipartFile file) {

        try (
            PDDocument document =
                    Loader.loadPDF(
                            file.getBytes()
                    )
        ) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            return stripper.getText(
                    document
            );
        }
        catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to extract text from PDF policy",
                    exception
            );
        }
    }

    private String extension(
            MultipartFile file) {

        String fileName =
                file.getOriginalFilename();

        if (
                fileName == null
                || !fileName.contains(".")
        ) {

            return "";
        }

        return fileName
                .substring(
                        fileName.lastIndexOf('.') + 1
                )
                .toLowerCase(
                        Locale.ROOT
                );
    }
}
