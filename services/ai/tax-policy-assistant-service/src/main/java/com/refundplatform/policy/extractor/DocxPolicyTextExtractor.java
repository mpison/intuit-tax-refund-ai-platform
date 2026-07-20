package com.refundplatform.policy.extractor;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@Component
public class DocxPolicyTextExtractor
        implements PolicyTextExtractor {

    @Override
    public boolean supports(
            MultipartFile file) {

        return extension(file)
                .equals("docx");
    }

    @Override
    public String extract(
            MultipartFile file) {

        try (
            XWPFDocument document =
                    new XWPFDocument(
                            file.getInputStream()
                    );

            XWPFWordExtractor extractor =
                    new XWPFWordExtractor(
                            document
                    )
        ) {

            return extractor.getText();
        }
        catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to extract text from DOCX policy",
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
