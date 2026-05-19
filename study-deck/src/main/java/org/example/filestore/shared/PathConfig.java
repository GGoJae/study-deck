package org.example.filestore.shared;

import java.nio.file.Path;

public class PathConfig {
    public static final Path WORKING_DIR = Path.of(".deck");

    // file system manager path
    public static final Path FILE_SYSTEM_WORK_PATH = Path.of(WORKING_DIR.toString(), "work");
    public static final Path FILE_SYSTEM_TMP_PATH = Path.of(WORKING_DIR.toString(), "tmp");

    // meta data manager path
    public static final Path META_DATA_WORK_PATH = Path.of(WORKING_DIR.toString(), "meta.json");
    public static final Path META_DATA_TMP_PATH = Path.of(WORKING_DIR.toString(), "meta.tmp");

    // category manager path
    public static final Path CATEGORY_WORK_PATH = Path.of(WORKING_DIR.toString(), "category.json");
    public static final Path CATEGORY_TMP_PATH = Path.of(WORKING_DIR.toString(), "category.tmp");

    // subCategory manager path
    public static final Path SUBCATEGORY_WORK_PATH = Path.of(WORKING_DIR.toString(), "subcategory.json");
    public static final Path SUBCATEGORY_TMP_PATH = Path.of(WORKING_DIR.toString(), "subcategory.tmp");

    // card manager working file name
    public static final String CARD_WORK_NAME = "card.json";
    public static final String CARD_TMP_NAME = "card.tmp";
}
