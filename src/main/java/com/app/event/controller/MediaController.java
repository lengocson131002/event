package com.app.event.controller;

import com.app.event.dto.media.request.UploadFileRequest;
import com.app.event.dto.media.response.UploadFileResponse;
import com.app.event.enums.ResponseCode;
import com.app.event.exception.ApiException;
import com.app.event.service.MediaService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/media")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<UploadFileResponse> uploadFile(@Valid @ModelAttribute UploadFileRequest request) {
        String mediaUrl = mediaService.uploadFile(request.getFile());
        if (mediaUrl == null || mediaUrl.isEmpty()) {
            throw new ApiException(ResponseCode.FILE_ERROR_UPLOAD_FAILED);
        }
        return ResponseEntity.ok(new UploadFileResponse(mediaUrl));
    }
}
