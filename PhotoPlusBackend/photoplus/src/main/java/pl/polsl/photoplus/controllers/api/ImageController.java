package pl.polsl.photoplus.controllers.api;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.photoplus.services.controllers.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("/picture")
public class ImageController {

    final ImageService imageService;

    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(path = "/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    @PreAuthorize("hasPermission('image', 'single' )")
    public ResponseEntity<Resource> getSingle(@PathVariable("name") final String name) {
        final Resource resource = imageService.getImage(name);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission('image', 'post' )")
    public ResponseEntity post(@RequestParam("image") final MultipartFile image) throws IOException {
        return new ResponseEntity(imageService.saveImage(image));
    }

    @DeleteMapping(path = "/{name}")
    @PreAuthorize("hasPermission('image', 'delete' )")
    public ResponseEntity delete(@PathVariable("name") final String name) throws IOException {
        return new ResponseEntity<>(imageService.deleteImage(name));
    }

    @PatchMapping
    @PreAuthorize("hasPermission('image', 'patch' )")
    public ResponseEntity patch(@RequestParam("image") final MultipartFile image) throws IOException {
        return new ResponseEntity(imageService.patchImage(image));
    }

}
