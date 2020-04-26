package pl.polsl.photoplus.services.controllers;

import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.photoplus.services.controllers.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
public class ImageService {

    private final ResourceLoader resourceLoader;
    private final String IMG_PATH = "src/main/resources/images";

    public ImageService(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource getImage(final String name) {
        File imgDir = new File(IMG_PATH);
        File foundImage = Arrays.stream(imgDir.listFiles())
                .filter(x -> x.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> { throw new NotFoundException("No image with name: " + name,
                ImageService.class.getSimpleName()); });
        return resourceLoader.getResource("file:" + IMG_PATH + File.separator + foundImage.getName());
    }

    public HttpStatus saveImage(final MultipartFile file) throws IOException {
        Tika tika = new Tika();
        String detectedType = tika.detect(file.getBytes());
        if (!detectedType.startsWith("image")) {
            return HttpStatus.BAD_REQUEST;
        }

        if (Files.exists(Paths.get(IMG_PATH , file.getOriginalFilename()))) {
            return HttpStatus.CONFLICT;
        }
        Files.copy(file.getInputStream(), Paths.get(IMG_PATH , file.getOriginalFilename()));
        return HttpStatus.CREATED;
    }

    public HttpStatus deleteImage (final String name) throws IOException {
        File imgDir = new File(IMG_PATH);
        File foundImage = Arrays.stream(imgDir.listFiles())
                .filter(x -> x.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> { throw new NotFoundException("No image with name: " + name,
                        ImageService.class.getSimpleName()); });
        Files.delete(Paths.get(IMG_PATH + File.separator + foundImage.getName()));
        return HttpStatus.NO_CONTENT;
    }

    public HttpStatus patchImage (final MultipartFile file) throws IOException {
        Files.copy(file.getInputStream(), Paths.get(IMG_PATH , file.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING);
        return HttpStatus.CREATED;
    }
}
