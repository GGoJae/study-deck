package org.example.filestore;

import java.util.regex.Pattern;

public class FileFormatValidator {

    private static final Pattern FORBIDDEN_CHARS = Pattern.compile("[\\\\/:*?\"<>|\\x00-\\x1F]");
    private static final Pattern INVALID_NAMES = Pattern.compile("^\\.*$|^\\s*$");

    public static boolean isRightDirFormat(String dirName) {
        if (dirName == null || dirName.isEmpty() || dirName.length() > 255) {
            return false;
        }

        if (dirName.contains(" ")) {
            return false;
        }

        if (FORBIDDEN_CHARS.matcher(dirName).find()) {
            return false;
        }

        return !INVALID_NAMES.matcher(dirName).matches();
    }

    public static boolean isWrongDirFormat(String dirName) {
        return !FileFormatValidator.isRightDirFormat(dirName);
    }

    public static boolean isRightFileFormat(String fileName) {
        if (!isRightDirFormat(fileName)) {
            return false;
        }

        // 특정 확장자만 허용하거나 체크할 로직들있으면 추가
        return true;
    }

    public static boolean isWrongFileFormat(String fileName) {
        return !FileFormatValidator.isRightFileFormat(fileName);
    }
}
