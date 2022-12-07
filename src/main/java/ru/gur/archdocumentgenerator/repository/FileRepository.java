package ru.gur.archdocumentgenerator.repository;

public interface FileRepository {

    void save(byte[] buffer, String fileName);

    void remove(String fileName);

    byte[] find(String fileName);

    String getUrl(String name);
}
