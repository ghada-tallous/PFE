package com.example.Pfe.service;

import com.example.Pfe.entites.Media;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    Media saveMedia(MultipartFile file, Long publicationId) throws IOException;
    List<Media> saveMultipleMedia(MultipartFile[] files, Long publicationId) throws IOException; // Nouvelle méthode
    Media getMedia(Long id);
    void deleteMedia(Long id);
    void deleteMediaByPublicationId(Long publicationId);
    List<Media> findByPublicationId(Long publicationId);
}