package com.refundplatform.policy.extractor;

import org.springframework.web.multipart.MultipartFile;

public interface PolicyTextExtractor {

    boolean supports(
            MultipartFile file);

    String extract(
            MultipartFile file);
}
