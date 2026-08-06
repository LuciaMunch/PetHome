package com.pethome.services.interfaces.commons;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    String subirImagen(MultipartFile archivo);
}
