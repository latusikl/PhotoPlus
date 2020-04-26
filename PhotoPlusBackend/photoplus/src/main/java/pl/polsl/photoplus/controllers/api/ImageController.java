package pl.polsl.photoplus.controllers.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.photoplus.model.dto.ImageModelDto;
import pl.polsl.photoplus.services.controllers.ImageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;

    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(path = "/{code}", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasPermission('image', 'single' )")
    public ResponseEntity<ByteArrayResource> getSingle(@PathVariable("code") final String code) {
        final ImageModelDto img = imageService.getSingleObject(code);
        final ByteArrayResource resource = new ByteArrayResource(img.getBytes());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasPermission('image', 'post' )")
    public ResponseEntity postImage(final MultipartFile file) throws IOException {
        if (!this.imageService.isPicture(file)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(imageService.save(List.of(
                new ImageModelDto(null, file.getOriginalFilename(), file.getBytes()))));
    }

    @DeleteMapping("/delete/{code}")
    @PreAuthorize("hasPermission('image', 'delete' )")
    public ResponseEntity delete(@PathVariable("code") final String code) {
        return new ResponseEntity(imageService.delete(code));
    }

    @PatchMapping("/{code}")
    @PreAuthorize("hasPermission('image', 'patch' )")
    public ResponseEntity patch(final MultipartFile file, @PathVariable("code") final String code) throws IOException {
        final ImageModelDto img = new ImageModelDto(code, file.getOriginalFilename(), file.getBytes());
        return new ResponseEntity(imageService.patch(img, code));
    }
}
