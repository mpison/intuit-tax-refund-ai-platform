package com.refundplatform.policy.extractor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class TextPolicyTextExtractor
        implements PolicyTextExtractor {

    @Override
    public boolean supports(
            MultipartFile file) {

        return extension(file)
                .equals("txt");
    }

    @Override
    public String extract(
            MultipartFile file) {

        try {
            return new String(
                    file.getBytes(),
                    StandardCharsets.UTF_8
            );
        }
        catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to extract text from TXT policy",
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
