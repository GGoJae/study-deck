package org.example.filestore.filesystem.naming;

import java.util.UUID;

public class UuidFileNameGenerator implements FileNameGenerator{
    @Override
    public String getFileName() {
        return UUID.randomUUID().toString();
    }
}
