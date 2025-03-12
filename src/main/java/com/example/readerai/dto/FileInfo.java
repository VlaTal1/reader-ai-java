package com.example.readerai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

    private String fileName;

    private String originalFileName;

    private String filePath;

    private Long fileSize;

    private String fileType;
}
