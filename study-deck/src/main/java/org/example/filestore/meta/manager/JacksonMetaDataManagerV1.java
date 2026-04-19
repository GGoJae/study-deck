package org.example.filestore.meta.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.core.category.domain.Category;
import org.example.filestore.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JacksonMetaDataManagerV1 implements MetaDataManager{
    @Override
    public Category createCategory(Category category) throws IOException {
        ObjectMapper mapper = JsonMapper.getInstance();

        Path path = Path.of(".deck", "category.json");

        List<Category> categories = new ArrayList<>();
        if (Files.exists(path)) {
            categories = mapper.readValue(path.toFile(), new TypeReference<List<Category>>() {
            });
        }

        return null;
    }
}
