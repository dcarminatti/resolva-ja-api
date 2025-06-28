package dev.dcarminatti.rja_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFileException extends RuntimeException {
    
    private String fileName;
    private String fileType;
    private String reason;

    public InvalidFileException(String fileName, String reason) {
        super(String.format("Invalid file '%s': %s", fileName, reason));
        this.fileName = fileName;
        this.reason = reason;
    }

    public InvalidFileException(String fileName, String fileType, String reason) {
        super(String.format("Invalid file '%s' of type '%s': %s", fileName, fileType, reason));
        this.fileName = fileName;
        this.fileType = fileType;
        this.reason = reason;
    }

    public InvalidFileException(String message) {
        super(message);
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getReason() {
        return reason;
    }
}
