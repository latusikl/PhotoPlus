package pl.polsl.photoplus.controllers.api;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.photoplus.services.controllers.PictureService;

import java.io.IOException;

@RestController
@RequestMapping("/picture")
public class PictureController {

    final PictureService pictureService;

    public PictureController(final PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping(path = "/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getSingle(@PathVariable("name") final String name) {
        final Resource resource = pictureService.getImage(name);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity post(@RequestParam("image") final MultipartFile image) throws IOException {
        return new ResponseEntity(pictureService.saveImage(image));
    }

    @DeleteMapping(path = "/{name}")
    public ResponseEntity delete(@PathVariable("name") final String name) throws IOException {
        return new ResponseEntity<>(pictureService.deleteImage(name));
    }

    @PatchMapping
    public ResponseEntity patch(@RequestParam("image") final MultipartFile image) throws IOException {
        return new ResponseEntity(pictureService.patchImage(image));
    }

}
