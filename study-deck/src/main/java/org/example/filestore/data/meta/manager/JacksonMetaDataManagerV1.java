package org.example.filestore.data.meta.manager;

import org.example.filestore.shared.model.Focus;

public class JacksonMetaDataManagerV1 implements MetaDataManager{
//    @Override
//    public Category createCategory(Category category) throws IOException {
//        ObjectMapper mapper = JsonMapper.getInstance();
//
//        Path path = Path.of(".deck", "category.json");
//
//        List<Category> categories = new ArrayList<>();
//        if (Files.exists(path)) {
//            categories = mapper.readValue(path.toFile(), new TypeReference<List<Category>>() {
//            });
//        }
//
//        return null;
//    }

    @Override
    public Focus getFocus() {
        // TODO
        return null;
    }

    @Override
    public Long nextCategoryId() {
        // TODO
        return null;
    }

    @Override
    public void transaction() {
        // TODO
    }

    @Override
    public void commit() {
        // TODO
    }

    @Override
    public void rollback() {
        // TODO
    }
}
