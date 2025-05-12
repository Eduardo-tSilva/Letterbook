package br.com.Letterbook.Letterbook.controller;

import br.com.Letterbook.Letterbook.model.Book;
import br.com.Letterbook.Letterbook.model.DTO.BookDTO;
import br.com.Letterbook.Letterbook.model.Image;
import br.com.Letterbook.Letterbook.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping({"","/"})
    public String showBooks(@RequestParam(value = "search", required = false, defaultValue = "") String search, Model model) {
        List<Book> books;

        if(search.isEmpty()) {
            books = bookRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } else {
            books = bookRepository.findByTitle(search);
        }

        model.addAttribute("books", books);
        model.addAttribute("search", search);
        return "books/allBooks";
    }

    @GetMapping("/createBook")
    public String createBook(Model model) {
        BookDTO bookDTO = new BookDTO();
        model.addAttribute("book", bookDTO);
        return "books/createBook";
    }

    @PostMapping("/createBook")
    public String createAndUpdateBook(@Valid BookDTO bookDTO,
                                      BindingResult bindingResult,
                                      @RequestParam("image") String image,
                                      RedirectAttributes redirectAttributes) throws IOException {

        if(bindingResult.hasErrors()) {
            return "books/createBook";
        }

        Book book = new Book();

        if(bookDTO.getId() != 0) {
            book = bookRepository.findById(bookDTO.getId()).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        } else {
            book = new Book();
        }

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setSynopsis(bookDTO.getSynopsis());
        book.setGenre(bookDTO.getGenre());
        book.setYear(bookDTO.getYear());

        if(bookDTO.getImages() != null && !bookDTO.getImages().isEmpty()) {
            String upload = "uploads/";
            List<Image> images = new ArrayList<>();

            for(MultipartFile file : bookDTO.getImages()) {
                String fileName = UUID.randomUUID()+ "_" + file.getOriginalFilename();
                Path filePath = Paths.get(upload + fileName);

                if(!Files.exists(Paths.get(upload))) {
                    Files.createDirectories(Paths.get(upload));
                }

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                Image newImage = new Image();
                newImage.setUrl(fileName);
                newImage.setBook(book);
                newImage.setPrincipal(fileName.endsWith(image));
                images.add(newImage);
            }

            book.getImages().addAll(images);
        }
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("message", "book created successfully");
        return "redirect:/books";
    }
}
